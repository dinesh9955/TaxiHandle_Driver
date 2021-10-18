package com.rydz.driver.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.RETURN
import com.rydz.driver.CommonUtils.AppConstants.isPathSend
import com.rydz.driver.CommonUtils.locationMethods.LocationResultInterface
import com.rydz.driver.CommonUtils.locationMethods.MyLocation
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.fcm.MyFirebaseMessagingService
import com.rydz.driver.model.NewMessageResponse
import com.rydz.driver.model.googleMapApiresponse.GoogleApiResponse
import com.rydz.driver.model.socketResponse.Booking
import com.rydz.driver.model.socketResponse.DriverRideStatusResponse
import com.rydz.driver.model.socketResponse.SocketBookingResponse
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls
import com.rydz.driver.view.activity.LogInActivity
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.adapters.PoolRideAdapter
import com.rydz.driver.view.mapUtils.HttpConnection
import com.rydz.driver.view.mapUtils.PathJSONParser
import com.rydz.driver.viewModel.login.editProfile.ChangeDriverStatusViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_bottom_common_for_driver.*
import kotlinx.android.synthetic.main.layout_payment.*
import kotlinx.android.synthetic.main.layout_topnavigation.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.onClick
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PoolRideFragment : BaseFragment(), OnMapReadyCallback,
    LocationResultInterface, SocketListener {


    val tolerance: Double = 100.0 // meters
    private var myLocation: MyLocation? = null
    private var exceededTolerance: Boolean = false
    private var isRotaing = false
    private val CLASSTAG: String = PoolRideFragment::class.java.simpleName
    private var SOURCE_LATLNG = LatLng(0.0, 0.0)
    private var DESTINATION_LATLNG = LatLng(0.0, 0.0)
    private var isFirstTimeinApp: Boolean = true
    private val USER = "user"
    private val IMAGE = "image"
    private var requestType: String = USER
    private var isZoomable = true
    lateinit var tv_user_address: TextView
    private var poolRideAdapter: PoolRideAdapter? = null
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeDriverStatusViewModel? = null
    var APPTAG: String = PoolRideFragment::class.java.name
    private lateinit var rv_rides: RecyclerView
    private var isArriving = false
    private var isStarted = false
    private var mapLatlng = LatLng(MainActivity.latitude, MainActivity.longitude)
    private var myMarker: Marker? = null
    private var currentLocation: Location? = null
    var firstTime: Boolean = true
    var desbitmap: Bitmap? = null
    var sourcebitmap: Bitmap? = null
    var waypointbitmap: Bitmap? = null
    private var driverStatus: Int = 9
    private var driverRideSocketResponse: DriverRideStatusResponse? = null
    private var animShow: Animation? = null
    private var animHide: Animation? = null
    private var isTracking: Boolean = true
    private var bearingValue: Float = 90.56f
    var bookingList: List<Booking>? = null
    var pos = 0
    var paymentDialog: Dialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rv_rides = view.findViewById(R.id.rv_rides)
        tv_user_address = view.findViewById(R.id.tv_user_address)
        currentLocation = Location("")
        currentLocation!!.latitude = MainActivity.latitude
        currentLocation!!.longitude = MainActivity.longitude
        firstTime = true
        homeFragment = this
        //convert drawable to bitmap
        waypointbitmap = getBitmap(R.drawable.ic_green_dot_small)
        sourcebitmap = getBitmap(R.drawable.ic_green_dot_small)
        desbitmap = getBitmap(R.drawable.ic_red_dot_rectangle_small)
        // animation of views hide  or show
        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide)

        // check is google service available or not
        isGooglePlayServicesAvailable()

        (context!!.applicationContext as App).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeDriverStatusViewModel::class.java)
        myLocation = MyLocation()
        rv_rides.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper() // Or PagerSnapHelper
        snapHelper.attachToRecyclerView(rv_rides)


        rv_rides.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    var centerView: View = snapHelper.findSnapView(rv_rides.layoutManager)!!
                    pos = rv_rides.layoutManager!!.getPosition(centerView)
                    Log.e("Snapped Item Position:", "" + pos)

                    if (bookingList != null && bookingList!!.get(pos).bookingType.equals("1")) {
                        showNavigationAddress(pos)
                    }
                }
            }
        })

        // Check location permisions
        if (!canAccessLocation() || !canAccessCoreLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
            }

        } else {
            val networkPresent = myLocation!!.getLocation(context, this)
            if (!networkPresent) {
                MainActivity.mainActivity.checkGpsService()
            }
        }
        return view
    }

    // Check goole pay service
    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (ConnectionResult.SUCCESS == status)
            return true
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                showAlert("Please Install google play services to use this application")
        }
        return false
    }


    override fun onPause() {
        super.onPause()
        firstTime = false
    }


    // animate a camera on map
    private fun animateCamera(location: Location) {
        if (MainActivity.mainActivity.checkGpsService()) {
            val latLng = LatLng(location.latitude, location.longitude)
            if (mMap != null) {
                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))

            }
        }
    }

    // get camera position with bearing
    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder(mMap!!.cameraPosition).bearing(currentLocation!!.bearing).zoom(16f).target(latLng)
            .build()
    }


    //Getting current location
    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        handleOneLocation(currentLocation)
    }

    // Set on listeners of views
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_starttrip.setOnClickListener(this)
        ll_navigate.setOnClickListener(this)
        switch_addTrip.setOnClickListener(this)
        iv_current_location.setOnClickListener(this)
        tv_chat.setOnClickListener(this)
        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        switch_online.visibility = View.GONE

        switch_addTrip.visibility = View.GONE

        // Connect sockets
        AppSocketListener.getInstance().setActiveSocketListener(this)
        // Restart Socket.io to avoid weird stuff ;-)
        AppSocketListener.getInstance().restartSocket()
        try {
            newLogin()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    @SuppressLint("MissingPermission")
    private fun animateCameraMAp(location: LatLng) {
        if (MainActivity.mainActivity.checkGpsService()) {
            if (mMap != null) {
                try {
                    mMap!!.isMyLocationEnabled = false
                    val builder = LatLngBounds.Builder()
                    var latLngs = ArrayList<LatLng>()
                    if (polyline!!.points != null) {
                        latLngs = polyline!!.points as ArrayList<LatLng>
                        for (latLng in latLngs) {
                            builder.include(latLng)
                        }
                        val bounds = builder.build()
                        val padding = 100
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

                        mMap!!.animateCamera(cameraUpdate)
                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }


            }
        }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_starttrip -> {
                if (!Constant.checkInternetConnection(context)) {
                } else {
                    if (isCameraPermissions()) {
                        if (driverRideSocketResponse != null) {
                            Log.e("driverStatus", driverStatus.toString() + "")
                            if (driverStatus == 0) {
                            } else if (driverStatus == 1) {
                                requestType = IMAGE
                                animateCameraMAp(LatLng(MainActivity.latitude, MainActivity.longitude))
                                //  isZoomable = false
                                Handler().postDelayed(Runnable {
                                    if (driverRideSocketResponse!!.booking != null) {
                                        // Capture screen shot of route
                                        CaptureMapScreen(driverRideSocketResponse!!.booking!!.get(0)!!.id)
                                    }
                                }, 3000)
                            } else if (driverStatus == 2) {

                            }
                        }
                    }
                }
            }
            R.id.tv_chat -> {
                if (!Constant.checkInternetConnection(context)) {
                } else {
                    if (driverRideSocketResponse != null) {
                        val intent = Intent(context!!, ChatFargment::class.java)
                        intent.putExtra("fromNotification", "home")
                        intent.putExtra(AppConstants.KEYBOOKINGID, driverRideSocketResponse!!.booking!!.get(0)!!.id)
                        intent.putExtra(
                            AppConstants.KEYPROFILEPIC,
                            driverRideSocketResponse!!.booking!!.get(0)!!.userId.profilePic
                        )
                        intent.putExtra(AppConstants.KEYUSERID, driverRideSocketResponse!!.booking!!.get(0)!!.userId.id)
                        intent.putExtra(
                            AppConstants.KEYNAME,
                            driverRideSocketResponse!!.booking!!.get(0)!!.userId.firstName + " " + driverRideSocketResponse!!.booking!!.get(
                                0
                            )!!.userId.lastName
                        )
                        startActivity(intent)
                    }
                }
            }

            R.id.ll_navigate -> {


                if (MainActivity.mainActivity.checkGpsService()) {
                    if (bookingList != null && bookingList!!.get(pos).bookingType.equals("1")) {

                        openNavigationInGMap(pos)
                    } else
                        openMap(mapLatlng)
                }

            }

            R.id.iv_current_location -> {
                if (MainActivity.mainActivity.checkGpsService()) {
                    myLocation!!.getLocation(context, this)
                    isFirstTimeinApp = true
                    getCurrentLocation()
                }

            }
        }

    }


    //Hit Socket to change Driver status Arrived,start trip ,end trip etc...
    private fun hitSockettoChangeDriverStatus(s: String, bookingId: String) {
        MainActivity.progressDialog!!.show()
        try {
            val jsonObject = JSONObject()
            jsonObject.put(AppConstants.STATUS_SOCKET, s)
            jsonObject.put(AppConstants.BOOKING_ID, bookingId)
            sendObjectToSocket(jsonObject, SocketUrls.DRIVER_CHANGE_STATUS)


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }

    private fun hitSockettoChangeDriverStatusWithReturn(s: String, bookingId: String, returnValue: Int) {
        MainActivity.progressDialog!!.show()
        try {
            val jsonObject = JSONObject()
            jsonObject.put(AppConstants.STATUS_SOCKET, s)
            jsonObject.put(AppConstants.BOOKING_ID, bookingId)
            jsonObject.put(RETURN, returnValue)
            sendObjectToSocket(jsonObject, SocketUrls.DRIVER_CHANGE_STATUS)


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    // onMap ready method
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isMapToolbarEnabled = false
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        if (mMap != null) {
            try {
                mapbounds = RectangularBounds.newInstance(
                    mMap!!.projection.visibleRegion!!.farLeft,
                    mMap!!.projection.visibleRegion!!.farRight
                )
            } catch (e: Exception) {

            }
        }



        mMap!!.isMyLocationEnabled = false
        mMap!!.setMinZoomPreference(7f)
        mMap!!.setMaxZoomPreference(25f)

        try {
            handleOneLocation(currentLocation!!)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

        if (MainActivity.mainActivity.checkGpsService()) {
            goToMYLocation()
        }


    }


    //    handle to show car on marker when driver location update
    override fun handleCarLocation(location: Location?) {
        if (isZoomable) {
            if (MainActivity.mainActivity.checkGpsService()) {
                if (location != null) {
                    currentLocation = location
                    val oldLatlng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                    if (mMap != null) {
                        try {
                            mapbounds = RectangularBounds.newInstance(
                                mMap!!.projection.visibleRegion!!.farLeft,
                                mMap!!.projection.visibleRegion!!.farRight
                            )
                        } catch (e: Exception) {

                        }
                    }

                    try {

                        // val newLatlng = LatLng(myMarker!!.position.latitude, myMarker!!.position.longitude)'
                        val newLatlng = LatLng(location.latitude, location.longitude)
                        if (!newLatlng.toString().equals(oldLatlng.toString())) {
                            MainActivity.mainActivity.runOnUiThread(Runnable {
                                if (isTracking) {
                                    drawMarker(MainActivity.latitude, MainActivity.longitude)
                                    exceededTolerance = !PolyUtil.isLocationOnPath(
                                        LatLng(MainActivity.latitude, MainActivity.longitude),
                                        polyLine,
                                        false,
                                        tolerance
                                    )

                                    if (driverRideSocketResponse != null) {
                                        if (exceededTolerance) {
                                            if (isArriving) {
                                                val origin: LatLng =
                                                    LatLng(MainActivity.latitude, MainActivity.longitude)
                                                val source: LatLng = LatLng(
                                                    driverRideSocketResponse!!.booking!!.get(0)!!.source.latitude.toDouble(),
                                                    driverRideSocketResponse!!.booking!!.get(0)!!.source.longitude.toDouble()
                                                )
                                                mapLatlng = source
                                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                                list.add(origin)
                                                list.add(source)
                                                drawPolyLineOnMap(list)
                                            } else if (isStarted) {
                                                val origin: LatLng =
                                                    LatLng(MainActivity.latitude, MainActivity.longitude)
                                                val source: LatLng = LatLng(
                                                    driverRideSocketResponse!!.booking!!.get(0)!!.destination.latitude.toDouble(),
                                                    driverRideSocketResponse!!.booking!!.get(0)!!.destination.longitude.toDouble()
                                                )
                                                mapLatlng = source
                                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                                list.add(origin)
                                                list.add(source)
                                                drawPolyLineOnMap(list)
                                            }
                                        }
                                    }
                                }

                            })

                        }


                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }


    // Draw marker of driver location
    private fun goToMYLocation() {
        try {
            val params = JSONObject()
            params.put(AppConstants.DRIVER_ID, MainActivity.mainActivity.getUserId())
            params.put(AppConstants.DRIVERLATITUDE, MainActivity.latitude.toString())
            params.put(AppConstants.DRIVERLONGITUDE, MainActivity.longitude.toString())
            Log.e("TRACKING  : ", MainActivity.latitude.toString() + "   " + MainActivity.longitude.toString())

            sendObjectToSocket(params, SocketUrls.UPDATE_LATITUDE_LONGITUDE)
            if (isTracking) {
                tracking()
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            INITIAL_REQUEST -> if (canAccessLocation() && canAccessCoreLocation()) {
                val networkPresent = myLocation!!.getLocation(context!!, this)
                if (!networkPresent) {
                    MainActivity.mainActivity.checkGpsService()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        MainActivity.mainActivity.forceHideKeyboard(context!!)
        AppConstants.isAcceptScreen = false
        try {
            firstTime = true
            MyFirebaseMessagingService.inChatScreen = false
            commitListeners()
            isFirstTimeinApp = true

            if (MainActivity.mainActivity.checkGpsService()) {
                myLocation!!.getLocation(context, this)
            }
        } catch (Ex: java.lang.Exception) {
            Ex.printStackTrace()
        }

        try {
            if (tv_info_text != null) {
                if (!getNoteValue().isEmpty()) {
                    tv_info_text.text = getNoteValue() + ""
                } else {
                    tv_info_text.text = ""
                }
            }

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

    private fun commitListeners() {
        try {
            if (AppSocketListener.getInstance().isSocketConnected) {
                driverRideStatus()

                AppSocketListener.getInstance()
                    .off(MainActivity.mainActivity.getUserId() + "-booking", onBookingRequestListener)
                AppSocketListener.getInstance()
                    .addOnHandler(MainActivity.mainActivity.getUserId() + "-booking", onBookingRequestListener)

                AppSocketListener.getInstance().off("driverRideStatus", onGetDriveRideStatusListener)
                AppSocketListener.getInstance().addOnHandler("driverRideStatus", onGetDriveRideStatusListener)

                AppSocketListener.getInstance().off(MainActivity.mainActivity.getUserId() + "-change", onChangeStatus)
                AppSocketListener.getInstance()
                    .addOnHandler(MainActivity.mainActivity.getUserId() + "-change", onChangeStatus)

                AppSocketListener.getInstance().off("driverChangeStatus", onDriverChangeRideStatusListener)
                AppSocketListener.getInstance().addOnHandler("driverChangeStatus", onDriverChangeRideStatusListener)

                AppSocketListener.getInstance()
                    .off(MainActivity.mainActivity.getUserId() + "-message", onNewMessgaeListener)
                AppSocketListener.getInstance()
                    .addOnHandler(MainActivity.mainActivity.getUserId() + "-message", onNewMessgaeListener)


                AppSocketListener.getInstance().off(SocketUrls.ISREADMESSAGE, onReadMsgListener)
                AppSocketListener.getInstance().addOnHandler(SocketUrls.ISREADMESSAGE, onReadMsgListener)


                AppSocketListener.getInstance().off(SocketUrls.BOOKINGREQUESTRESPONSE, onDriverChangeRideStatusListener)
                AppSocketListener.getInstance()
                    .addOnHandler(SocketUrls.BOOKINGREQUESTRESPONSE, onDriverChangeRideStatusListener)

                AppSocketListener.getInstance().off(SocketUrls.SAVEBOOKINGPATH, onDriverChangeRideStatusListener)
                AppSocketListener.getInstance()
                    .addOnHandler(SocketUrls.SAVEBOOKINGPATH, onDriverChangeRideStatusListener)

                AppSocketListener.getInstance()
                    .off(MainActivity.mainActivity.getUserId() + "-" + SocketUrls.NEW_LOGIN, onNewLogin)
                AppSocketListener.getInstance()
                    .addOnHandler(MainActivity.mainActivity.getUserId() + "-" + SocketUrls.NEW_LOGIN, onNewLogin)
            }

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    // draw marker onMap
    fun drawMarker(lat: Double?, lng: Double?) {
        try {
            if (mMap != null) {
                val gps = LatLng(lat!!, lng!!)
                MainActivity.latitude = lat
                MainActivity.longitude = lng
                if (gps != null) {
                    MainActivity.mainActivity.runOnUiThread(Runnable {
                        if (currentLocation != null) {
                            try {
                                if (isFirstTimeinApp) {
                                    animateCamera(currentLocation!!)
                                }
                            } catch (ex: java.lang.Exception) {
                                ex.printStackTrace()
                            }

                        }
                    })

                }

            }

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

    //Create a bitmap from drawable
    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }


    //Handle map location when location updates
    override fun handleNewLocation(location: Location?) {
        Log.e("655", "696")

        if (isZoomable) {
            Log.e("696", "696")
            try {
                if (MainActivity.mainActivity.checkGpsService()) {
                    if (location != null) {
                        currentLocation = location
                        MainActivity.latitude = location.latitude
                        MainActivity.longitude = location.longitude
                        goToMYLocation()
                    }
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

    }


    //Handle when user first time arrive in app
    override fun handleOneLocation(location: Location?) {
        if (isZoomable) {
            try {
                if (isFirstTimeinApp) {
                    if (MainActivity.mainActivity.checkGpsService()) {
                        if (location != null) {
                            Log.e("handleOneLocation", location.latitude.toString() + "  " + location.longitude)
                            currentLocation = location
                            MainActivity.mainActivity.runOnUiThread(Runnable {
                                if (currentLocation != null) {
                                    try {
                                        animateCamera(currentLocation!!)

                                    } catch (ex: java.lang.Exception) {
                                        ex.printStackTrace()
                                    }
                                }
                            })
                        }
                    }
                    Handler().postDelayed(Runnable {
                        isFirstTimeinApp = false
                    }, 10000)

                }

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun canAccessCoreLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {

        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context!!, perm)
    }


    companion object {
        lateinit var mapbounds: RectangularBounds
        private var poolBookingId: String? = null

        private val TAG = PoolRideFragment::class.java.simpleName
        lateinit var homeFragment: PoolRideFragment
        var mMap: GoogleMap? = null
        var polyLine: List<LatLng>? = ArrayList<LatLng>()
        private var polyline: Polyline? = null


        private val INITIAL_PERMS =
            arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        private val INITIAL_REQUEST = 13
        fun savePaths(list: JSONArray) {
            isPathSend = false
            try {

                val params = JSONObject()
                val geometry = JSONObject()
                geometry.put("type", "LineString")
                geometry.put("coordinates", list)
                params.put("geometry", geometry)
                params.put("bookingId", poolBookingId)
                params.put("driverId", MainActivity.mainActivity.getUserId())
                params.put("seats", 1)
                params.put("totalSeats", 4)
                try {
                    if (AppSocketListener.getInstance().isSocketConnected) {
                        AppSocketListener.getInstance().emit(SocketUrls.SAVEBOOKINGPATH, params)
                    } else {
                        AppSocketListener.getInstance().connect()
                        AppSocketListener.getInstance().emit(SocketUrls.SAVEBOOKINGPATH, params)
                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }


    fun updateDriverStatus(loginResponse: DriverRideStatusResponse) {
        driverRideStatus()
    }


    // 0 for on way, 1 = reached source, 2= start, 3 = complete, 4 = paid 5 = rating done, 6 = skip rating, 9=no ride, 11 for cancel
    @SuppressLint("MissingPermission")
    fun checkDriverStatus(loginResponse: DriverRideStatusResponse) {
        Log.e("818","818")
        try {
            driverRideSocketResponse = loginResponse

            if (loginResponse != null && loginResponse.booking != null && loginResponse.booking.size > 0) {
                readMsgStatus(loginResponse.booking.get(0).id)
                isTracking = true
            }


            if (loginResponse != null && loginResponse.booking != null && loginResponse.booking.size > 0) {
                driverStatus = loginResponse.booking.get(loginResponse.booking.size - 1).status.toInt()
            } else {
                driverStatus = loginResponse.status.toInt()
            }



            Log.e("checkDriverStatus", driverStatus.toString())
            try {
                if (paymentDialog != null)
                    paymentDialog!!.dismiss()
            } catch (e: java.lang.Exception) {

            }

            when (driverStatus) {
                0 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                        try {
                            if (loginResponse.booking != null) {

                                if (driverRideSocketResponse!!.booking != null) {

                                    ll_user_bottom.visibility = View.GONE
                                    ll_top_navigation.visibility = View.VISIBLE
                                    poolBookingId =
                                        driverRideSocketResponse!!.booking.get(driverRideSocketResponse!!.booking.size - 1)
                                            .id
                                    bookingList = emptyList()

                                    /*   if (driverRideSocketResponse!!.booking.get(driverRideSocketResponse!!.booking.size - 1).bookingType.equals(
                                               "1"
                                           )
                                       )*/
                                    bookingList = driverRideSocketResponse!!.booking
                                    /*   else
                                           bookingList =
                                               listOf(driverRideSocketResponse!!.booking.get(driverRideSocketResponse!!.booking.size - 1))*/

                                    poolRideAdapter = PoolRideAdapter(activity!!, this, bookingList!!)


                                    rv_rides.adapter = poolRideAdapter
                                }

                                isTracking = true
                                isArriving = true
                                isStarted = false

                                ll_top_navigation.visibility = View.VISIBLE
                                switch_addTrip.visibility = View.GONE

                                if (tv_chat != null) {
                                    cl_chat.visibility = View.VISIBLE
                                    0
                                    tv_user_address.text =
                                        loginResponse.booking!!.get(loginResponse.booking!!.size - 1)!!.source.name
                                    btn_starttrip.text = getString(R.string.arrived)
                                }

                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)


                                val origin: LatLng = LatLng(
                                    loginResponse.booking!!.get(0)!!.source.latitude.toDouble(),
                                    loginResponse.booking!!.get(0)!!.source.longitude.toDouble()
                                )
                                val source: LatLng = LatLng(
                                    loginResponse.booking!!.get(0)!!.destination.latitude.toDouble(),
                                    loginResponse.booking!!.get(0)!!.destination.longitude.toDouble()
                                )

                                mapLatlng = source
                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                list.add(origin)
                                list.add(source)
                                drawPolyLineOnMap(list)

                            }
                        } catch (ex: Exception) {
                            Log.e("1035", "Status 0 - Exception")
                            ex.printStackTrace()
                        }

                    }


                }
                1 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                        try {
                            if (loginResponse.booking != null) {

                                if (driverRideSocketResponse!!.booking != null) {

                                    ll_user_bottom.visibility = View.GONE
                                    ll_top_navigation.visibility = View.VISIBLE
                                    poolBookingId =
                                        driverRideSocketResponse!!.booking.get(driverRideSocketResponse!!.booking.size - 1)
                                            .id
                                    bookingList = emptyList()

                                    bookingList = driverRideSocketResponse!!.booking
                                    poolRideAdapter = PoolRideAdapter(activity!!, this, bookingList!!)

                                    rv_rides.adapter = poolRideAdapter
                                }



                                isArriving = false
                                isStarted = true
                                isTracking = true
                                if (tv_chat != null) {
                                    cl_chat.visibility = View.VISIBLE



                                    ll_top_navigation.visibility = View.VISIBLE

                                    ll_waiting_rider.visibility = View.VISIBLE
                                    ll_waiting_rider.startAnimation(animHide)
                                    btn_starttrip.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            R.color.button_green
                                        )
                                    )
                                    if (loginResponse.booking!!.get(0)!!.userId != null) {
                                        tv_user_name.text =
                                            loginResponse.booking!!.get(0)!!.userId.firstName + " " + loginResponse.booking!!.get(
                                                0
                                            )!!.userId.lastName
                                        try {
                                            MainActivity.mainActivity.getImageRequest(loginResponse.booking!!.get(0)!!.userId.profilePic)
                                                .into(iv_user_image)
                                        } catch (e: Exception) {

                                        }
                                    } else {
                                        tv_user_name.text =
                                            loginResponse.booking!!.get(loginResponse.booking!!.size - 1)!!.firstName
                                    }

                                    tv_user_address.text =
                                        loginResponse.booking!!.get(loginResponse.booking!!.size - 1)!!.source.name
                                }
                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                val origin: LatLng = LatLng(MainActivity.latitude, MainActivity.longitude)
                                val source: LatLng = LatLng(
                                    loginResponse.booking!!.get(0)!!.destination.latitude.toDouble(),
                                    loginResponse.booking!!.get(0)!!.destination.longitude.toDouble()
                                )
                                if (mMap != null) {
                                    mMap!!.isMyLocationEnabled = false
                                }
                                mapLatlng = source
                                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                list.add(origin)
                                list.add(source)
                                drawPolyLineOnMap(list)
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                2 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                        try {
                            if (loginResponse.booking != null) {

                                if (driverRideSocketResponse!!.booking != null) {

                                    ll_user_bottom.visibility = View.GONE
                                    ll_top_navigation.visibility = View.VISIBLE
                                    poolBookingId = driverRideSocketResponse!!.booking.get(0).id
                                    bookingList = emptyList()
                                    bookingList = driverRideSocketResponse!!.booking
                                    poolRideAdapter = PoolRideAdapter(activity!!, this, bookingList!!)

                                    rv_rides.adapter = poolRideAdapter

                                    val origin: LatLng = LatLng(MainActivity.latitude, MainActivity.longitude)
                                    val source: LatLng = LatLng(
                                        loginResponse.booking!!.get(0)!!.destination.latitude.toDouble(),
                                        loginResponse.booking!!.get(0)!!.destination.longitude.toDouble()
                                    )

                                    mapLatlng = source
                                    val list: ArrayList<LatLng> = ArrayList<LatLng>()
                                    list.add(origin)
                                    for (j in 0 until bookingList!!.size) {

                                        list.add(
                                            LatLng(
                                                bookingList!!.get(j).source.latitude.toDouble(),
                                                bookingList!!.get(j).source.longitude.toDouble()
                                            )
                                        )

                                    }

                                    list.add(source)


                                    drawPolyLineOnMapForPool(list)
                                }


                                isTracking = true
                                isArriving = false
                                isStarted = true
                                if (tv_chat != null) {

                                    cl_chat.visibility = View.GONE


                                    ll_top_navigation.visibility = View.VISIBLE


                                    ll_waiting_rider.visibility = View.GONE
                                    btn_starttrip.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            R.color.button_red
                                        )
                                    )
                                    btn_starttrip.text = getString(R.string.complete_trip)
                                    if (loginResponse.booking!!.get(0)!!.userId != null) {
                                        tv_user_name.text =
                                            loginResponse.booking!!.get(0)!!.userId.firstName + " " + loginResponse.booking!!.get(
                                                0
                                            )!!.userId.lastName
                                        try {
                                            MainActivity.mainActivity.getImageRequest(loginResponse.booking!!.get(0)!!.userId.profilePic)
                                                .into(iv_user_image)
                                        } catch (e: Exception) {

                                        }
                                    } else {
                                        tv_user_name.text = loginResponse.booking!!.get(0)!!.firstName
                                    }
                                }
                                if (mMap != null) {
                                    mMap!!.isMyLocationEnabled = false
                                }

                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)

                            }

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                3 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isArriving = false
                                isStarted = true
                                isTracking = true
                                if (mMap != null) {
                                    mMap!!.clear()
                                    mMap!!.isMyLocationEnabled = false
                                    switch_addTrip.visibility = View.GONE

                                }
                                if (tv_chat != null) {
                                    cl_chat.visibility = View.GONE
                                }
                                if (driverRideSocketResponse!!.booking != null) {


                                    ll_user_bottom.visibility = View.GONE
                                    ll_top_navigation.visibility = View.VISIBLE
                                    poolBookingId =
                                        driverRideSocketResponse!!.booking.get(driverRideSocketResponse!!.booking.size - 1)
                                            .id
                                    bookingList = emptyList()
                                    bookingList = driverRideSocketResponse!!.booking


                                    poolRideAdapter = PoolRideAdapter(activity!!, this, bookingList!!)

                                    rv_rides.adapter = poolRideAdapter

                                }

                            }

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                4 -> {
                    try {
                        isTracking = true
                        isArriving = false
                        isStarted = false
                        switch_addTrip.visibility = View.GONE

                        if (mMap != null) {
                            mMap!!.clear()
                            mMap!!.isMyLocationEnabled = false
                        }

                        ll_top_navigation.visibility = View.GONE
                        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }


                }
                5 -> {

                }
                6 -> {

                    driverRideStatus()

                }
                7 -> {

                }
                8 -> {

                }
                9 -> {
                    try {
                        isTracking = true
                        isArriving = false
                        isStarted = false
                        switch_addTrip.visibility = View.GONE

                        if (mMap != null) {
                            mMap!!.clear()
                            mMap!!.isMyLocationEnabled = false
                        }

                        ll_top_navigation.visibility = View.GONE
                        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                11 -> {
                    isTracking = true
                    isArriving = false
                    isStarted = false

                    try {
                        if (mMap != null) {
                            mMap!!.clear()
                            mMap!!.isMyLocationEnabled = false
                            switch_addTrip.visibility = View.GONE

                        }
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                        showAlert(getString(R.string.canceled_by_user))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }

                14 -> {

                    if (mMap != null) {
                        mMap!!.clear()
                        mMap!!.isMyLocationEnabled = false
                    }
                    MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    //Logout a user if already login on other device
    fun onAlreadyLogin() {
        try {
            AlertDialog.Builder(context!!)
                .setTitle("Exit")
                .setCancelable(false)
                .setMessage(getString(R.string.already_login))
                .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface, arg1: Int) {
                        navigate(LogInActivity::class.java)
                        activity!!.finishAffinity()

                    }
                }).create().show()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }


    //Send status to server Driver is login
    private fun newLogin() {
        try {
            val params = JSONObject()
            params.put("userId", MainActivity.mainActivity.getUserId())
            sendObjectToSocket(params, SocketUrls.NEW_LOGIN)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //Check driver ride status
    private fun driverRideStatus() {
        try {
            val params = JSONObject()
            params.put(AppConstants.DRIVER_ID, MainActivity.mainActivity.getUserId())
            params.put(AppConstants.BOOKING_ID, "")
            params.put("authToken", MainActivity.mainActivity.getAuthToken())
            Log.e("authToken", params.toString())
            sendObjectToSocket(params, SocketUrls.DRIVER_RIDE_STATUS)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }


    // Tracking a driver
    private fun tracking() {
        try {
            MainActivity.mainActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    try {
                        isRotaing = true
                        animateCar(currentLocation!!)
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }


                }
            })


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    // Lister of socket when change a driver statuse
    fun onChangeStatus(loginResponse: DriverRideStatusResponse) {
        checkDriverStatus(loginResponse)
    }


    // to change navigation address in acse of merchant pooling
    fun showNavigationAddress(pos: Int) {


        if (bookingList!!.get(pos).status.toInt() == 2 || bookingList!!.get(pos).status.toInt() == 1) {
            tv_user_address.text = bookingList!!.get(pos).destination.name
        } else if (bookingList!!.get(pos).status.toInt() == 0) {
            tv_user_address.text = bookingList!!.get(pos).source.name
        }


    }

    // to change navigation address in acse of merchant pooling
    fun openNavigationInGMap(pos: Int) {
        if (bookingList!!.get(pos).status.toInt() == 2 || bookingList!!.get(pos).status.toInt() == 1) {
            var navigation =
                Uri.parse("google.navigation:q=" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude + "")
            var navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
            navigationIntent.setPackage("com.google.android.apps.maps")
            startActivity(navigationIntent)


        } else if (bookingList!!.get(pos).status.toInt() == 0) {

            var navigation =
                Uri.parse("google.navigation:q=" + SOURCE_LATLNG.latitude + "," + SOURCE_LATLNG.longitude + "")
            var navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
            navigationIntent.setPackage("com.google.android.apps.maps")
            startActivity(navigationIntent)


        }


    }


    //open google map using intent
    fun openMap(latLng: LatLng) = try {


        if (driverStatus == 2) {
            var wayPoints = ""
            var srcAdd: String =
                "&origin=" + bookingList!!.get(0).source.latitude + "," + bookingList!!.get(0).source.longitude
            var desAdd: String = "&destination=" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude
            var wayPointsList: MutableList<LatLng>? = mutableListOf<LatLng>()
            for (j in 0 until bookingList!!.size) {


                if (j != 0 && j != bookingList!!.size) {
                    if (!wayPointsList!!.contains(
                            LatLng(
                                bookingList!!.get(j).source.latitude.toDouble(),
                                bookingList!!.get(j).source.longitude.toDouble()
                            )
                        )
                    ) {
                        wayPointsList.add(
                            LatLng(
                                bookingList!!.get(j).source.latitude.toDouble(),
                                bookingList!!.get(j).source.longitude.toDouble()
                            )
                        )
                    }
                    if (!wayPointsList.contains(
                            LatLng(
                                bookingList!!.get(j).destination.latitude.toDouble(),
                                bookingList!!.get(j).destination.longitude.toDouble()
                            )
                        )
                    ) {
                        wayPointsList.add(
                            LatLng(
                                bookingList!!.get(j).destination.latitude.toDouble(),
                                bookingList!!.get(j).destination.longitude.toDouble()
                            )
                        )
                    }

                }

            }

            for (k in 0 until wayPointsList!!.size) {
                wayPoints =
                    wayPoints + (if (wayPoints == "") "" else "%7C") + wayPointsList.get(k).latitude + "," + wayPointsList.get(
                        k
                    ).longitude
            }
            wayPoints = "&waypoints=" + wayPoints
            var link: String =
                "https://www.google.com/maps/dir/?api=1&travelmode=driving" + srcAdd + desAdd + wayPoints

            var intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(Intent.createChooser(intent, "Select an application"))

        } else {

            try {
                var path_intent: Intent? = null

                if (bookingList != null && bookingList!!.size > 0 && bookingList!!.get(bookingList!!.size - 1).status.toInt() == 0) {
                    path_intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "http://maps.google.com/maps?q=loc:" + bookingList!!.get(bookingList!!.size - 1).source.latitude + "," + bookingList!!.get(
                                bookingList!!.size - 1
                            ).source.longitude
                        )
                    )
                } else if (bookingList != null && bookingList!!.size > 0 && bookingList!!.get(bookingList!!.size - 1).status.toInt() == 1) {
                    path_intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "http://maps.google.com/maps?q=loc:" + bookingList!!.get(bookingList!!.size - 1).destination.latitude + "," + bookingList!!.get(
                                bookingList!!.size - 1
                            ).destination.longitude
                        )
                    )

                }


                path_intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Only if initiating from a Broadcast Receiver
                path_intent.setPackage("com.google.android.apps.maps")
                if (path_intent.resolveActivity(context!!.packageManager) != null) {
                    path_intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                    path_intent.setPackage("com.google.android.apps.maps")
                }
                startActivity(path_intent)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }


    // Draw polyline on map
    fun drawPolyLineOnMap(list: List<LatLng>) {
        try {
            if (mMap != null) {
                mMap!!.clear()
            }

            SOURCE_LATLNG = list.get(0)
            DESTINATION_LATLNG = list.get(1)
            val options = MarkerOptions()
            options.position(SOURCE_LATLNG)
            val options1 = MarkerOptions()
            options1.position(DESTINATION_LATLNG)
            mMap!!.addMarker(options).setIcon(BitmapDescriptorFactory.fromBitmap(sourcebitmap))
            mMap!!.addMarker(options1).setIcon(BitmapDescriptorFactory.fromBitmap(desbitmap))
            val url = getMapsApiDirectionsUrl()
            Log.e("1404 ", url.toString())
            val downloadTask = ReadTask()
            downloadTask.execute(url)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    // Draw polyline on map
    fun drawPolyLineOnMapForPool(list: List<LatLng>) {
        try {
            if (mMap != null) {
                mMap!!.clear()
            }

            SOURCE_LATLNG = list.get(0)
            DESTINATION_LATLNG = list.get(list.size - 1)
            val options = MarkerOptions()
            options.position(SOURCE_LATLNG)
            val options1 = MarkerOptions()
            options1.position(DESTINATION_LATLNG)

            mMap!!.addMarker(options).setIcon(BitmapDescriptorFactory.fromBitmap(sourcebitmap))
            for (i in 1..list.size - 2) {
                if (i != 1) {
                    val options1 = MarkerOptions()
                    options1.position(list.get(i))
                    mMap!!.addMarker(options1).setIcon(BitmapDescriptorFactory.fromBitmap(waypointbitmap))
                }
            }
            mMap!!.addMarker(options1).setIcon(BitmapDescriptorFactory.fromBitmap(desbitmap))
            val url = getMapsApiDirectionsUrlforPool()

            val downloadTask = ReadTask()
            downloadTask.execute(url)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()

        }


    }


    //get map direction url with key
    private fun getMapsApiDirectionsUrl(): String {
        try {
            val origin = "origin=" + SOURCE_LATLNG.latitude + "," + SOURCE_LATLNG.longitude
            val dest = "destination=" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude
            val sensor = "sensor=false"
            val params = "$origin&$dest&$sensor&mode=driving&key=" + getString(R.string.google_directions_key)
            return "https://maps.googleapis.com/maps/api/directions/json?$params"
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    private fun getMapsApiDirectionsUrlforPool(): String {
        try {
            val origin = "origin=" + bookingList!!.get(0).source.latitude + "," + bookingList!!.get(0).source.longitude
            var waypoints = "waypoints=optimize:true|"
            val destination =
                "destination=" + bookingList!!.get(0).destination.latitude + "," + bookingList!!.get(0).destination.longitude
            for (i in 0..bookingList!!.size - 1) {
                waypoints =
                    waypoints + bookingList!!.get(i).source.latitude + "," + bookingList!!.get(i).source.longitude + "|"
                waypoints =
                    waypoints + bookingList!!.get(i).destination.latitude + "," + bookingList!!.get(i).destination.longitude + "|"
            }

            val sensor = "sensor=false"
            val params =
                origin + "&" + waypoints + "&" + destination + "&" + sensor + "&mode=driving&key=" + getString(R.string.google_directions_key)
            val output = "json"
            val url = ("https://maps.googleapis.com/maps/api/directions/" + output + "?" + params)
            val downloadTask = ReadTask()
            downloadTask.execute(url)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

        return ""
    }


    //async tast to draw polyline
    private class ReadTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg url: String): String {
            var data = ""
            try {
                val http = HttpConnection()
                data = http.readUrl(url[0])
                Log.e("1464", data.toString())
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val gson1 = Gson()

                val loginResponse = gson1.fromJson(result.toString(), GoogleApiResponse::class.java)

                if (loginResponse != null) {

                    if (loginResponse.routes != null && loginResponse.routes.size > 0) {
                        if (loginResponse.routes.get(0).legs != null && loginResponse.routes.get(0).legs.size > 0) {
                            val text: String = loginResponse.routes.get(0).legs.get(0).duration.text
                            val pathsPointslist = JSONArray()
                            if (isPathSend) {


                                for (i in 0 until loginResponse.routes.get(0).legs.get(0).steps.size) {

                                    val startLocObj = JSONObject()
                                    startLocObj.put(
                                        "lat",
                                        loginResponse.routes.get(0).legs.get(0).steps!!.get(i)!!.startLocation!!.lat
                                    )
                                    startLocObj.put(
                                        "lng",
                                        loginResponse.routes.get(0).legs.get(0).steps!!.get(i)!!.startLocation!!.lng
                                    )
                                    pathsPointslist.put(startLocObj)
                                }
                                savePaths(pathsPointslist)

                            }
                            Log.e("LatLng List", pathsPointslist.toString())
                            PoolRideFragment.homeFragment.setValueToText(text)
                        }
                    }


                }


                ParserTask().execute(result)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()


            }


        }
    }


    //    set a value to text if view is not null
    private fun setValueToText(text: String) {
        try {
            tv_user_wait_time.text = text + ""
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
        override fun doInBackground(
            vararg jsonData: String
        ): List<List<HashMap<String, String>>> {
            val jObject: JSONObject

            var routes: List<List<HashMap<String, String>>>? = null
            try {
                if (jsonData!=null && jsonData[0] != null && !jsonData.isEmpty() && !jsonData[0].isEmpty()) {
                    jObject = JSONObject(jsonData[0])


                    val parser = PathJSONParser()
                    routes = parser.parse(jObject)

                    Log.e("routes", routes.toString())
                    return routes!!
                }
                return emptyList()
            } catch (e: Exception) {
                e.printStackTrace()


            }
            return ArrayList<List<HashMap<String, String>>>()
        }

        override fun onPostExecute(routes: List<List<HashMap<String, String>>>) {

            try {
                var points: ArrayList<LatLng>? = null
                var polyLineOptions: PolylineOptions? = null
                // traversing through routes
                for (i in 0 until routes.size) {
                    points = ArrayList()
                    polyLineOptions = PolylineOptions()
                    val path = routes[i]
                    for (j in 0 until path.size) {
                        val point = path.get(j)
                        val lat = java.lang.Double.parseDouble(point?.get("lat"))
                        val lng = java.lang.Double.parseDouble(point?.get("lng"))
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }
                    polyLineOptions.addAll(points)
                    polyLineOptions.width(6f)
                    polyLineOptions.color(Color.BLACK)

                }
                if (mMap != null) {
                    if (polyLineOptions != null) {
                        polyline = mMap!!.addPolyline(polyLineOptions)
                        polyLine = ArrayList<LatLng>()
                        polyLine = points
                    }
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }

    // animate a car
    private fun animateCar(destination: Location) {
        try {


            if (driverRideSocketResponse != null) {
                bearingValue = mMap!!.cameraPosition.bearing

                Log.e("1692", driverRideSocketResponse!!.booking!!.size.toString())
                if (!AppConstants.OLDLATITUDE.toString().equals(MainActivity.latitude.toString()) && driverRideSocketResponse!!.booking!!.size>0 ) {
                    AppConstants.OLDLATITUDE = MainActivity.latitude.toString()
                    val params = JSONObject()
                    params.put(AppConstants.BOOKING_ID, driverRideSocketResponse!!.booking!!.get(0)!!.id)
                    params.put(AppConstants.LATITUDE, MainActivity.latitude)
                    params.put(AppConstants.LONGITUDE, MainActivity.longitude)
                    params.put(AppConstants.STATUS_SOCKET, driverRideSocketResponse!!.booking!!.get(0)!!.status)
                    params.put(AppConstants.DRIVER_ID, MainActivity.mainActivity.getUserId())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        params.put(AppConstants.BEARING, bearingValue)
                    } else {
                        params.put(AppConstants.BEARING, bearingValue)

                    }
                    Log.e("TRACKING 1", params.toString())
                    sendObjectToSocket(params, SocketUrls.TRACKING)
                }
            }


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //Booking request listener response
    private val onBookingRequestListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onBookingRequestListener   " + args[0].toString())
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {
                        try {
                            val gson1 = Gson()
                            val loginResponse = gson1.fromJson(args[0].toString(), SocketBookingResponse::class.java)
                            if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                                // change UI elements here
                                val moneyTransferFragment = AcceptFragment()
                                val b = Bundle()
                                b.putParcelable(AppConstants.SENDEDDATA, loginResponse)
                                moneyTransferFragment.arguments = b
                                MainActivity.mainActivity.replaceFragment(
                                    moneyTransferFragment,
                                    AppConstants.ACCEPTSCREEN
                                )

                            }
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }


                    }
                })

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }
    //Get driver ride status response
    private val onGetDriveRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            try {
                Log.e(CLASSTAG, "call: onGetDriveRideStatusListener   " + args[0].toString())
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                Log.e("818","1763")
                                checkDriverStatus(loginResponse)
                            }
                        })

                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

    }
    // onDriver change status response
    private val onChangeStatus = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            try {
                Log.e(CLASSTAG, "call: onChangeStatus   " + args[0].toString())
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (!loginResponse.status.toString().isEmpty()) {

                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                Log.e("818","1763")

                                onChangeStatus(loginResponse)
                            }
                        })
                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }
    //OnNew login Respose
    private val onNewLogin = object : Emitter.Listener {
        override fun call(vararg args: Any) {

            try {
                Log.e(CLASSTAG, "call: onNewLogin   " + args[0].toString())
                try {
                    val jsonObject = args[0] as JSONObject
                    if (jsonObject.get("success").toString().equals("true")) {

                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                // change UI elements here
                                MainActivity.mainActivity.getMyPreferences().clearSharedPrefrences()
                                onAlreadyLogin()
                            }
                        })
                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }
    }

    // on driver Change ride satus listener
    private val onDriverChangeRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onDriverChangeRideStatusListener   " + args[0].toString())
            MainActivity.progressDialog!!.dismiss()

            try {
                val gson1 = Gson()
                val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                    MainActivity.mainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            updateDriverStatus(loginResponse)
                        }
                    })
                } else if (loginResponse.success.toString().equals("false")) {
                    val myObject = JSONObject(args[0].toString())
                    if (myObject.get("success").toString().equals("false")) {
                        if (myObject.get("status").toString().equals("9")) {
                            isArriving = false
                            isStarted = false
                            try {
                                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                                    override fun run() {
                                        // change UI elements here
                                        if (mMap != null) {
                                            mMap!!.clear()
                                        }
                                        MainActivity.mainActivity.replaceFragment(
                                            HomeFragment(),
                                            AppConstants.HOMESCREEN
                                        )
                                        showAlert(loginResponse.message.toString() + "")
                                    }
                                })
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                            return
                        }
                    }


                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }


    //emit socket with parameters
    fun sendObjectToSocket(jsonObject: JSONObject, type: String) {

        try {
            if (AppSocketListener.getInstance().isSocketConnected) {
                AppSocketListener.getInstance().emit(type, jsonObject)
            } else {
                AppSocketListener.getInstance().connect()
                AppSocketListener.getInstance().emit(type, jsonObject)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }


    // onSocket connected
    override fun onSocketConnected() {
        Log.e(CLASSTAG, "onSocketConnected")
        try {
            commitListeners()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }

    override fun onSocketDisconnected() {
        Log.e(CLASSTAG, "onSocketDisconnected")
    }

    override fun onSocketConnectionError() {
        Log.e(CLASSTAG, "onSocketConnectionError")
    }

    override fun onSocketConnectionTimeOut() {
        Log.e(CLASSTAG, "onSocketConnectionTimeOut")
    }

    // Capture map screen
    fun CaptureMapScreen(id: String) {
        val callback = object : GoogleMap.SnapshotReadyCallback {
            var bitmap: Bitmap? = null
            override fun onSnapshotReady(snapshot: Bitmap) {
                bitmap = snapshot
                try {

                    val file = File(
                        Environment.getExternalStorageDirectory().absolutePath
                                + "/MyMapScreen" + ".png"
                    )
                    val out = FileOutputStream(file)
                    // above "/mnt ..... png" => is a storage path (where image will be stored) + name of image you can customize as per your Requirement
                    bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, out)
                    val reqFileDriverLicence = RequestBody.create(MediaType.parse("image/*"), file)
                    val body = MultipartBody.Part.createFormData("image", file.absolutePath, reqFileDriverLicence)
                    val bookingid = RequestBody.create(MediaType.parse("text/plain"), id)
                    viewModel!!.hitScreenCapture(bookingid, body)
                    Log.e("FILE : ", file.absolutePath)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (mMap != null) {
            mMap!!.snapshot(callback)
        }

    }

    fun isCameraPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to capture image denied")
            makeCameraRequest()
            return false

        } else {
            return true
        }
    }

    fun makeCameraRequest() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            5368
        )
    }


    fun OnstartTrip(bookingId: String, driverStatus: Int, pos: Int) {
        if (!Constant.checkInternetConnection(context)) {
        } else {
            if (isCameraPermissions()) {
                if (driverRideSocketResponse != null) {
                    Log.e("driverStatus", driverStatus.toString() + "")
                    if (driverStatus == 0) {
                        hitSockettoChangeDriverStatus("1", bookingId)
                    } else if (driverStatus == 1) {
                        requestType = IMAGE
                        animateCameraMAp(LatLng(MainActivity.latitude, MainActivity.longitude))
                        // isZoomable = false

                        hitSockettoChangeDriverStatus("2", bookingId)
                    } else if (driverStatus == 2) {
                        if (driverRideSocketResponse!!.booking != null && driverRideSocketResponse!!.booking.size > 0 && driverRideSocketResponse!!.booking.get(
                                pos
                            ).isPaid != 1
                        )
                            hitSockettoChangeDriverStatus("3", bookingId)
                        else
                            hitSockettoChangeDriverStatus("3", bookingId)

                    } else if (driverStatus == 3) {

                        if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                            if (driverRideSocketResponse!!.booking != null && driverRideSocketResponse!!.booking.size > 0 && driverRideSocketResponse!!.booking.get(
                                    pos
                                ).isPaid != 1
                            ) {
                                try {

                                    val fragment = PayFragment()
                                    val b = Bundle()
                                    b.putSerializable(AppConstants.SENDEDDATA, bookingList!!.get(pos))
                                    fragment.arguments = b
                                    MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)


                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            } else {
                                showPaymentDoneDialog(
                                    bookingList!!.get(pos).fare,
                                    bookingId,
                                    bookingList!!.get(pos).bookingType
                                )
                            }
                        }
                    } else if (driverStatus == 4) {
                        if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {
                            driverRideStatus()
                        }
                    }
                }

            }

        }
    }

    //chat  listener response
    private val onNewMessgaeListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onNewMessgaeListener   " + args[0].toString())
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {
                        try {
                            val gson1 = Gson()


                            val reqResponse = gson1.fromJson(args[0].toString(), NewMessageResponse::class.java)
                            if (reqResponse.success) {
                                if (reqResponse.msg != null && reqResponse.msg.opponentReadStatus == 0) {
                                    if (poolRideAdapter != null && bookingList != null) {
                                        for (i in 0 until bookingList!!.size) {
                                            Log.e(
                                                "2016 ",
                                                reqResponse.msg.user.id + " : " + bookingList!!.get(i).userId.id
                                            )
                                            if (reqResponse.msg.user.id.equals(bookingList!!.get(i).userId.id)) {
                                                poolRideAdapter!!.showNotificationIndicatore(i)

                                                break
                                            }
                                        }
                                    }

                                }


                            }
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }


                    }
                })

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }


    //to get msg read status
    private fun readMsgStatus(bookingId: String) {
        try {
            val params = JSONObject()
            params.put("bookingId", bookingId)

            params.put(AppConstants.DRIVER_ID, MainActivity.mainActivity.getUserId())
            sendObjectToSocket(params, SocketUrls.ISREADMESSAGE)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //to check is there any unread msg
    private val onReadMsgListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(PoolRideFragment.TAG, "call: onReadMsgListener   " + args[0].toString())
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {

                        try {
                            val gson1 = Gson()


                            val reqResponse = gson1.fromJson(args[0].toString(), NewMessageResponse::class.java)
                            if (reqResponse.success) {
                                if (reqResponse.result == 1)
                                    iv_chatIndicator.visibility = View.VISIBLE
                                else
                                    iv_chatIndicator.visibility = View.GONE


                            }
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }


                    }
                })

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }


    fun notifyItemOfRecycle(pos: Int) {

        try {
            if (!rv_rides.isComputingLayout) {
                rv_rides.adapter!!.notifyItemChanged(pos)
            }
        } catch (e: Exception) {

        }


    }

    fun showPaymentDoneDialog(fareAmount: Double, bookingId: String, bookType: String) {

        paymentDialog = Dialog(context!!, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        paymentDialog!!.setCancelable(false)
        paymentDialog!!.setContentView(R.layout.layout_payment)
        if (paymentDialog != null) {
            paymentDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        try {
            paymentDialog!!.tv_paid_amount.text = activity!!.getString(R.string.currencysign) + " " + fareAmount


            try {
                if (bookType.equals("1")) {
                    paymentDialog!!.btn_cancel.visibility = View.VISIBLE
                    paymentDialog!!.tv_paid_amount.text = activity!!.getString(R.string.currencysign) + " 0.0"

                } else {
                    paymentDialog!!.btn_cancel.visibility = View.GONE
                    paymentDialog!!.tv_paid_amount.text = activity!!.getString(R.string.currencysign) + " " + fareAmount
                }

            } catch (e: Exception) {

            }

            paymentDialog!!.btn_cancel.setOnClickListener {

                reportDeliveryIssue(1, bookingId, paymentDialog!!)


            }
            paymentDialog!!.btn_continue.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    paymentDialog!!.cancel()
                    // rv_rides.visibility = View.GONE
                    hitSockettoChangeDriverStatusWithReturn("6", bookingId, 1)
                }
            })

            paymentDialog!!.show()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    fun reportDeliveryIssue(returnVal: Int, bookingId: String, payDialog: Dialog) {
        var returnTempVal = returnVal
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.deliveryissue_dialog)
        val window = dialog.window
        val wlp = window?.attributes
        wlp?.gravity = Gravity.CENTER
        wlp?.width = LinearLayout.LayoutParams.WRAP_CONTENT
        window?.attributes = wlp
        val cb_notAvailable = dialog.findViewById(R.id.cb_notAvailable) as androidx.appcompat.widget.AppCompatCheckBox
        val cb_returnedpackage =
            dialog.findViewById(R.id.cb_returnedpackage) as androidx.appcompat.widget.AppCompatCheckBox
        val cb_paymentNotDone =
            dialog.findViewById(R.id.cb_paymentNotDone) as androidx.appcompat.widget.AppCompatCheckBox
        val tv_submit = dialog.findViewById(R.id.tv_submit) as TextView
        val tv_paymentNotDone = dialog.findViewById(R.id.tv_paymentNotDone) as TextView
        cb_paymentNotDone.visibility = View.GONE

        tv_paymentNotDone.visibility = View.GONE
        cb_returnedpackage.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_notAvailable.isChecked = false
                cb_paymentNotDone.isChecked = false
                returnTempVal = 2
            }

        }

        cb_notAvailable.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_returnedpackage.isChecked = false
                cb_paymentNotDone.isChecked = false
                returnTempVal = 0
            }

        }


/*
        cb_paymentNotDone.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_notAvailable.isChecked = false
                cb_returnedpackage.isChecked = false
                returnTempVal = 2

            }

        }
*/

        tv_submit.onClick {
            if (returnTempVal == 1) {
                Toast.makeText(activity, "Please select an option", Toast.LENGTH_SHORT).show()


            } else {
                payDialog.cancel()
                dialog.dismiss()
                hitSockettoChangeDriverStatusWithReturn("6", bookingId, returnTempVal)
            }
        }



        dialog.show()


    }


}
