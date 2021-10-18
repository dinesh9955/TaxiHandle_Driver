package com.rydz.driver.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.maps.android.PolyUtil
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.CommonUtils.locationMethods.LocationResultInterface
import com.rydz.driver.CommonUtils.locationMethods.MyLocation
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.Urls.POOLINGTYPE
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.fcm.MyFirebaseMessagingService
import com.rydz.driver.model.NewMessageResponse
import com.rydz.driver.model.googleMapApiresponse.GoogleApiResponse
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.model.requests.ChangeDriverStatusRequest
import com.rydz.driver.model.socketResponse.DriverRideStatusResponse
import com.rydz.driver.model.socketResponse.SocketBookingResponse
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls
import com.rydz.driver.socket.SocketUrls.*
import com.rydz.driver.view.activity.LogInActivity
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.mapUtils.HttpConnection
import com.rydz.driver.view.mapUtils.PathJSONParser
import com.rydz.driver.viewModel.login.editProfile.ChangeDriverStatusViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.dialog_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_bottom_common_for_driver.*
import kotlinx.android.synthetic.main.layout_topnavigation.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(), OnMapReadyCallback, LocationResultInterface, SocketListener {


    val tolerance: Double = 100.0 // meters
    private var myLocation: MyLocation? = null
    private var exceededTolerance: Boolean = false
    private var isRotaing = false
    private val CLASSTAG: String = HomeFragment::class.java.simpleName
    lateinit var locationButton: View
    lateinit var mMapView: View
    private var SOURCE_LATLNG = LatLng(0.0, 0.0)
    private var DESTINATION_LATLNG = LatLng(0.0, 0.0)
    private var isFirstTimeinApp: Boolean = true
    private val USER = "user"
    private val IMAGE = "image"
    private var requestType: String = USER
    private var isZoomable = true
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeDriverStatusViewModel? = null
    var APPTAG: String = HomeFragment::class.java.name
    var progressDialog: ProgressDialog? = null
    private lateinit var mapLatlng: LatLng
    private lateinit var rv_rides: RecyclerView
    private var isArriving = false
    private var isStarted = false
    private var myMarker: Marker? = null
    private var currentLocation: Location? = null
    var firstTime: Boolean = true
    var bitmap: Bitmap? = null
    var sourcebitmap: Bitmap? = null
    var tempbitmap: Bitmap? = null
    private var driverStatus: Int = 9
    private var driverRideSocketResponse: DriverRideStatusResponse? = null
    private var animShow: Animation? = null
    private var animHide: Animation? = null
    private var isTracking: Boolean = false
    private var bearingValue: Float = 90.56f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mapLatlng = LatLng(MainActivity.latitude, MainActivity.longitude)

        rv_rides = view.findViewById(R.id.rv_rides)
        progressDialog = Constant.getProgressDialog(activity, "Please wait...")
        currentLocation = Location("")
        currentLocation!!.latitude = MainActivity.latitude
        currentLocation!!.longitude = MainActivity.longitude
        firstTime = true
        homeFragment = this
        //convert drawable to bitmap
        bitmap = getBitmap(R.drawable.car_marker)
        sourcebitmap = getBitmap(R.drawable.ic_green_dot_small)
        tempbitmap = getBitmap(R.drawable.ic_red_dot_small)
        // animation of views hide  or show
        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide)


        // check is google service available or not
        isGooglePlayServicesAvailable()

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeDriverStatusViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
        myLocation = MyLocation()


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
            if (mMap != null && latLng.latitude != 0.0) {

                if (driverStatus == 14) {
                    if (myMarker != null)
                        myMarker!!.remove()
                    myMarker = mMap!!.addMarker(
                        MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(latLng).flat(true)
                    )
                }
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
       // switch_addTrip.setOnClickListener(this)
        iv_current_location.setOnClickListener(this)
        tv_chat.setOnClickListener(this)
        iv_note.setOnClickListener(this)
        tv_cancelRide.setOnClickListener(this)
        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)


        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mMapView = mapFragment.view!!

        switch_online.visibility = View.GONE


        // Connect sockets
        AppSocketListener.getInstance().setActiveSocketListener(this)
        // Restart Socket.io to avoid weird stuff ;-)
        AppSocketListener.getInstance().restartSocket()
        try {
            newLogin()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


        if (MainActivity.mainActivity.getUserDetail().driver != null) {
            if (switch_online != null) {
                if (MainActivity.mainActivity.getUserDetail().driver.isAvailable.toString().equals("1")) {
                    switch_online.isChecked = true
                    changeSwithColor(true)
                    Utils.switchColor(true, switch_online)
                } else {
                    switch_online.isChecked = false
                    changeSwithColor(false)
                    Utils.switchColor(false, switch_online)
                }

          /*      if (MainActivity.mainActivity.getUserDetail().driver.vehicleType.type!=0   ) {
                    switch_addTrip.visibility = View.GONE
                }
*/



            }


        }
/*
        switch_addTrip.setOnClickListener {

            switch_addTrip.visibility = View.GONE
            var addTripIntent = Intent(activity, AddTripActivity::class.java)
            addTripIntent.putExtra("lat", MainActivity.latitude)
            addTripIntent.putExtra("lng", MainActivity.longitude)
            activity!!.startActivity(addTripIntent)

        }*/
        switch_online.setOnCheckedChangeListener(
            object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                    requestType = USER

                    progressDialog!!.show()
                    if (isChecked) {
                        val changeStausRequest = ChangeDriverStatusRequest()
                        changeStausRequest.driverId = MainActivity.mainActivity.getUserId()
                        changeStausRequest.status = "1"
                        viewModel!!.hitDriverStatusApi(changeStausRequest)
                    } else {
                        val changeStausRequest = ChangeDriverStatusRequest()
                        changeStausRequest.driverId = MainActivity.mainActivity.getUserId()
                        changeStausRequest.status = "0"
                        viewModel!!.hitDriverStatusApi(changeStausRequest)
                    }
                }
            })
    }

    private fun changeSwithColor(checked: Boolean) {
        if (checked) {
            switch_online.setTextColor(Color.GREEN)
            switch_online.text = getString(R.string.onlile)
        } else {
            switch_online.setTextColor(Color.RED)
            switch_online.text = getString(R.string.offline)
        }
    }


    /*
    * method to handle response
    */

    private fun consumeResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {

            Status.LOADING -> {
            }

            Status.SUCCESS -> {
                if (requestType.toString().equals(USER)) {
                    renderSuccessResponse(apiResponse.data!!)
                } else if (requestType.toString().equals(IMAGE)) {
                    renderSuccessResponseImage(apiResponse.data!!)
                }
            }

            Status.ERROR -> {
                isZoomable = true
                showAlert(resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }

    /*
    * method to handle success response
    * */
    private fun renderSuccessResponse(response: JsonElement) {
        progressDialog!!.dismiss()

        if (!response.isJsonNull) {
            Log.e(APPTAG + "response=", response.toString())
            val data: String = Utils.toJson(response)
            val gson1 = Gson()
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                MainActivity.mainActivity.getMyPreferences()
                    .setStringValue(AppConstants.USER_ID, loginResponse.driver.id.toString())
                MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.USERDETAIL, data)
                if (loginResponse.driver.isAvailable.toString().equals("1")) {
                    if (loginResponse.driver.vehicleType._id.equals(POOLINGTYPE)) {

                    }
                    switch_online.isChecked = true
                    changeSwithColor(true)
                    Utils.switchColor(true, switch_online)
                } else {
                    switch_online.isChecked = false
                    changeSwithColor(false)
                    Utils.switchColor(false, switch_online)
                }
            } else {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    /*
   * method to handle success response
   * */
    private fun renderSuccessResponseImage(response: JsonElement) {
        isZoomable = true
        if (!response.isJsonNull) {
            Log.e(APPTAG + "response=", response.toString())
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }


    @SuppressLint("MissingPermission")
    private fun animateCameraForScreenShot(srcLoc: LatLng, desLoc: LatLng) {
        if (MainActivity.mainActivity.checkGpsService()) {
            if (mMap != null) {

                try {
                    mMap!!.isMyLocationEnabled = false
                    if (myMarker != null)
                        myMarker!!.remove()
                    val builder = LatLngBounds.Builder()
                    builder.include(srcLoc)
                    var latLngs = ArrayList<LatLng>()
                    if (polyline!!.points != null) {
                        latLngs = polyline!!.points as ArrayList<LatLng>

                        for (latLng in latLngs) {
                            builder.include(latLng)
                        }
                        builder.include(desLoc)
                        val bounds = builder.build()
                        val padding = 60
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        Log.e("5199", "animateCameraForScreenShot")
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
                            ll_user_bottom.visibility = View.GONE
                            progressDialog!!.show()
                            if (driverStatus == 0) {
                                hitSockettoChangeDriverStatus("1")
                            } else if (driverStatus == 1) {
                                requestType = IMAGE
                                if (driverRideSocketResponse!!.booking != null)
                                    animateCameraForScreenShot(
                                        LatLng(
                                            driverRideSocketResponse!!.booking!!.get(0).source.latitude.toDouble(),
                                            driverRideSocketResponse!!.booking!!.get(0).source.longitude.toDouble()
                                        ),
                                        LatLng(
                                            driverRideSocketResponse!!.booking!!.get(0).destination.latitude.toDouble(),
                                            driverRideSocketResponse!!.booking!!.get(0).destination.longitude.toDouble()
                                        )
                                    )
                                isZoomable = false
                                Handler().postDelayed(Runnable {
                                    if (driverRideSocketResponse!!.booking != null) {
                                        // Capture screen shot of route
                                        CaptureMapScreen(driverRideSocketResponse!!.booking.get(0)!!.id)
                                    }
                                    hitSockettoChangeDriverStatus("2")
                                }, 3000)

                            } else if (driverStatus == 2) {

                                if(driverRideSocketResponse?.booking?.get(0)?.paymentMode.equals("card",true))
                                hitSockettoChangeDriverStatus("4")
                                else
                                    hitSockettoChangeDriverStatus("3")


                            }
                        }
                    } else {
                        if (driverRideSocketResponse != null) {
                            ll_user_bottom.visibility = View.GONE
                            progressDialog!!.show()
                            if (driverStatus == 0) {


                                hitSockettoChangeDriverStatus("1")
                            } else if (driverStatus == 1) {
                                requestType = IMAGE
                                if (driverRideSocketResponse!!.booking != null)
                                    animateCameraForScreenShot(
                                        LatLng(
                                            driverRideSocketResponse!!.booking!!.get(0).source.latitude.toDouble(),
                                            driverRideSocketResponse!!.booking!!.get(0).source.longitude.toDouble()
                                        ),
                                        LatLng(
                                            driverRideSocketResponse!!.booking!!.get(0).destination.latitude.toDouble(),
                                            driverRideSocketResponse!!.booking!!.get(0).destination.longitude.toDouble()
                                        )
                                    )

                                isZoomable = false
                                Handler().postDelayed(Runnable {
                                    if (driverRideSocketResponse!!.booking != null) {
                                        // Capture screen shot of route

                                        CaptureMapScreen(driverRideSocketResponse!!.booking.get(0)!!.id)
                                    }
                                    hitSockettoChangeDriverStatus("2")
                                }, 3000)

                            } else if (driverStatus == 2) {

                                hitSockettoChangeDriverStatus("3")


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
                        intent.putExtra(KEYBOOKINGID, driverRideSocketResponse!!.booking.get(0)!!.id)
                        if (driverRideSocketResponse!!.booking.get(0)!!.userId != null) {
                            try {
                                intent.putExtra(
                                    KEYPROFILEPIC,
                                    driverRideSocketResponse!!.booking.get(0)!!.userId.profilePic
                                )
                                intent.putExtra(KEYUSERID, driverRideSocketResponse!!.booking.get(0)!!.userId.id)
                                intent.putExtra(
                                    KEYNAME,
                                    driverRideSocketResponse!!.booking.get(0)!!.userId.firstName + " " + driverRideSocketResponse!!.booking.get(
                                        0
                                    )!!.userId.lastName
                                )
                            } catch (e: java.lang.Exception) {


                            }

                            intent.putExtra(
                                KEYNAME,
                                driverRideSocketResponse!!.booking.get(0)!!.userId.firstName + " " + driverRideSocketResponse!!.booking.get(
                                    0
                                )!!.userId.lastName
                            )
                            startActivity(intent)
                        }

                    }
                }
            }

            R.id.ll_navigate -> {
                if (MainActivity.mainActivity.checkGpsService()) {
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
            R.id.iv_note -> {
                showNotesDialog(driverRideSocketResponse!!.booking.get(0)!!.note.toString())

            }
            R.id.tv_cancelRide -> {
                Log.e("6161", "6161")
                hitSockettoChangeDriverStatus("11")

            }
        }

    }


    //Hit Socket to change Driver status Arrived,start trip ,end trip etc...
    private fun hitSockettoChangeDriverStatus(s: String) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(STATUS_SOCKET, s)
            jsonObject.put(BOOKING_ID, driverRideSocketResponse!!.booking.get(0)!!.id)
            sendObjectToSocket(jsonObject, DRIVER_CHANGE_STATUS)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }

    // onMap ready method
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isMapToolbarEnabled = false
        mMap!!.uiSettings.isCompassEnabled = false


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


        //map location button
        locationButton =
            (mMapView.findViewById<View>(Integer.parseInt("1"))!!.parent as View).findViewById(Integer.parseInt("2"))
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 1150, 20, 0)

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
                    }

                    try {
                        if (myMarker != null) {
                            val newLatlng = LatLng(myMarker!!.position.latitude, myMarker!!.position.longitude)
                            if (!newLatlng.toString().equals(oldLatlng.toString())) {
                                MainActivity.mainActivity.runOnUiThread(Runnable {
                                    if (isTracking) {
                                        drawMarker(MainActivity.latitude, MainActivity.longitude)
                                        exceededTolerance = !PolyUtil.isLocationOnPath(
                                            LatLng(
                                                MainActivity.latitude,
                                                MainActivity.longitude
                                            ), polyLine, false, tolerance
                                        )

                                        if (driverRideSocketResponse != null) {
                                            if (exceededTolerance) {
                                                if (isArriving) {
                                                    val origin: LatLng =
                                                        LatLng(MainActivity.latitude, MainActivity.longitude)
                                                    val source: LatLng = LatLng(
                                                        driverRideSocketResponse!!.booking.get(0)!!.source.latitude.toDouble(),
                                                        driverRideSocketResponse!!.booking.get(0)!!.source.longitude.toDouble()
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
                                                        driverRideSocketResponse!!.booking.get(0)!!.destination.latitude.toDouble(),
                                                        driverRideSocketResponse!!.booking.get(0)!!.destination.longitude.toDouble()
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
            params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
            params.put(DRIVERLATITUDE, MainActivity.latitude.toString())
            params.put(DRIVERLONGITUDE, MainActivity.longitude.toString())
            sendObjectToSocket(params, UPDATE_LATITUDE_LONGITUDE)
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

        isAcceptScreen = false
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

                AppSocketListener.getInstance().off(ISREADMESSAGE, onReadMsgListener)
                AppSocketListener.getInstance().addOnHandler(ISREADMESSAGE, onReadMsgListener)

                AppSocketListener.getInstance().off(BOOKINGREQUESTRESPONSE, onDriverChangeRideStatusListener)
                AppSocketListener.getInstance().addOnHandler(BOOKINGREQUESTRESPONSE, onDriverChangeRideStatusListener)

                AppSocketListener.getInstance().off(MainActivity.mainActivity.getUserId() + "-" + NEW_LOGIN, onNewLogin)
                AppSocketListener.getInstance()
                    .addOnHandler(MainActivity.mainActivity.getUserId() + "-" + NEW_LOGIN, onNewLogin)
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
        if (isZoomable) {
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
                    }, 9000)

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

        private val TAG = HomeFragment::class.java.simpleName
       lateinit var homeFragment: HomeFragment
        var mMap: GoogleMap? = null
        var polyLine: List<LatLng>? = ArrayList<LatLng>()
        private var polyline: Polyline? = null
        private val INITIAL_PERMS =
            arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private val INITIAL_REQUEST = 13
    }

    // 0 for on way, 1 = reached source, 2= start, 3 = complete, 4 = paid 5 = rating done, 6 = skip rating, 9=no ride, 11 for cancel
    @SuppressLint("MissingPermission")
    fun checkDriverStatus(loginResponse: DriverRideStatusResponse) {
        try {
            driverRideSocketResponse = loginResponse
         Log.e(TAG,"952")


            try {


                if (loginResponse != null && loginResponse.isDisabled == 0) {

                    (activity as MainActivity).handleDisableAccount()
                }
                if (loginResponse.booking != null && loginResponse.booking.size > 0) {

                    readMsgStatus(loginResponse.booking.get(0).id)
                    driverStatus = loginResponse.booking.get(0).status.toInt()
                    if (loginResponse.booking.get(loginResponse.booking.size - 1).bookingType.equals("2") && loginResponse.booking.get(
                            loginResponse.booking.size - 1
                        ).vehicleType.equals(POOLINGTYPE)
                    ) {
                        MainActivity.mainActivity.replaceFragment(PoolRideFragment(), POOLSCREEN)
                    } else if (loginResponse.booking.get(loginResponse.booking.size - 1).bookingType.equals("1")) {
                        MainActivity.mainActivity.replaceFragment(PoolRideFragment(), POOLSCREEN)
                    }
                } else {
                    driverStatus = loginResponse.status.toInt()
                }
            } catch (e: Exception) {

            }

            Log.e("checkDriverStatus", driverStatus.toString()+"")
            when (driverStatus) {
                0 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {

                                isTracking = true
                                isArriving = true
                                isStarted = false

                                if (tv_chat != null) {
                                    cl_chat.visibility = View.VISIBLE
                                    switch_online.visibility = View.GONE
                                    ll_top_navigation.visibility = View.VISIBLE
                                   // switch_addTrip.visibility = View.GONE
                                    ll_user_bottom.visibility = View.VISIBLE
                                    ll_user_bottom.startAnimation(animShow)
                                    ll_waiting_rider.visibility = View.GONE
                                    btn_starttrip.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            R.color.button_yellow
                                        )
                                    )
                                    if (mMap != null) {
                                        mMap!!.isMyLocationEnabled = false
                                        if (myMarker != null)
                                            myMarker!!.remove()
                                    }
                                    btn_starttrip.text = getString(R.string.arrived)
                                    if (loginResponse.booking.get(0)!!.userId != null) {
                                        tv_user_name.text = loginResponse.booking.get(0)!!.userId.firstName + " " + loginResponse.booking.get(0)!!.userId.lastName
                                        try{
                                        MainActivity.mainActivity.getImageRequest(loginResponse.booking.get(0)!!.userId.profilePic)
                                            .into(iv_user_image)
                                        }catch (e:Exception)
                                        {

                                        }
                                    } else {
                                        cl_chat.visibility = View.GONE

                                        tv_user_name.text = loginResponse.booking.get(0)!!.firstName
                                        tv_cancelRide.visibility = View.VISIBLE
                                    }
                                    tv_user_address.text = loginResponse.booking.get(0)!!.source.name


                                    try {

                                        if (loginResponse.booking.get(0)!!.note != null && loginResponse.booking.get(
                                                0
                                            )!!.note.toString().length > 0
                                        ) {
                                            iv_note.visibility = View.VISIBLE

                                        } else
                                            iv_note.visibility = View.GONE


                                    } catch (ex: java.lang.Exception) {
                                        ex.printStackTrace()
                                    }

                                }

                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                val origin: LatLng = LatLng(MainActivity.latitude, MainActivity.longitude)
                                val source: LatLng = LatLng(
                                    loginResponse.booking.get(0)!!.source.latitude.toDouble(),
                                    loginResponse.booking.get(0)!!.source.longitude.toDouble()
                                )
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
                1 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isArriving = false
                                isStarted = true
                                isTracking = true
                                if (tv_chat != null) {
                                    cl_chat.visibility = View.VISIBLE

                                    switch_online.visibility = View.GONE
                                  //  switch_addTrip.visibility = View.GONE

                                    ll_top_navigation.visibility = View.VISIBLE
                                    ll_user_bottom.visibility = View.VISIBLE
                                    ll_user_bottom.startAnimation(animShow)
                                    ll_waiting_rider.visibility = View.VISIBLE
                                    ll_waiting_rider.startAnimation(animHide)
                                    btn_starttrip.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            R.color.button_green
                                        )
                                    )
                                    if (mMap != null) {
                                        mMap!!.isMyLocationEnabled = false
                                        if (myMarker != null)
                                            myMarker!!.remove()
                                    }
                                    btn_starttrip.text = getString(R.string.starttrip)
                                    if (loginResponse.booking.get(0)!!.userId != null) {
                                        tv_user_name.text = loginResponse.booking.get(0)!!.userId.firstName + " " + loginResponse.booking.get(0)!!.userId.lastName
                                        try{
                                        MainActivity.mainActivity.getImageRequest(loginResponse.booking.get(0)!!.userId.profilePic)
                                            .into(iv_user_image)
                                        }catch (e:Exception)
                                        {

                                        }
                                    } else {
                                        cl_chat.visibility = View.GONE

                                        tv_user_name.text = loginResponse.booking.get(0)!!.firstName
                                        tv_cancelRide.visibility = View.VISIBLE
                                    }
                                    tv_user_address.text = loginResponse.booking.get(0)!!.source.name
                                }

                                try {

                                    if (loginResponse.booking.get(0)!!.note != null && loginResponse.booking.get(0)!!.note.toString().length > 0) {
                                        iv_note.visibility = View.VISIBLE

                                    } else
                                        iv_note.visibility = View.GONE


                                } catch (ex: java.lang.Exception) {
                                    ex.printStackTrace()
                                }


                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                val origin: LatLng = LatLng(MainActivity.latitude, MainActivity.longitude)
                                val source: LatLng = LatLng(
                                    loginResponse.booking.get(0)!!.destination.latitude.toDouble(),
                                    loginResponse.booking.get(0)!!.destination.longitude.toDouble()
                                )
                                if (mMap != null) {
                                    mMap!!.isMyLocationEnabled = false
                                    if (myMarker != null)
                                        myMarker!!.remove()
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
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isTracking = true
                                isArriving = false
                                isStarted = true
                                if (tv_chat != null) {
                                    cl_chat.visibility = View.GONE

                                    switch_online.visibility = View.GONE
                                  //  switch_addTrip.visibility = View.GONE

                                    ll_top_navigation.visibility = View.VISIBLE
                                    ll_user_bottom.visibility = View.VISIBLE
                                    ll_user_bottom.startAnimation(animShow)
                                    iv_note.visibility = View.GONE

                                    ll_waiting_rider.visibility = View.GONE
                                    btn_starttrip.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            R.color.button_red
                                        )
                                    )
                                    btn_starttrip.text = getString(R.string.complete_trip)
                                    if (loginResponse.booking.get(0)!!.userId != null) {
                                        tv_user_name.text = loginResponse.booking.get(0)!!.userId.firstName + " " + loginResponse.booking.get(0)!!.userId.lastName
                                        try{
                                        MainActivity.mainActivity.getImageRequest(loginResponse.booking.get(0)!!.userId.profilePic)
                                            .into(iv_user_image)}catch (e:Exception)
                                        {

                                        }

                                    } else {
                                        cl_chat.visibility = View.GONE

                                        tv_cancelRide.visibility = View.GONE
                                        tv_user_name.text = loginResponse.booking.get(0)!!.firstName

                                    }
                                    tv_user_address.text = loginResponse.booking.get(0)!!.destination.name
                                }
                                if (mMap != null) {
                                    mMap!!.isMyLocationEnabled = false
                                    if (myMarker != null)
                                        myMarker!!.remove()
                                }
                                MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
                                val origin: LatLng = LatLng(MainActivity.latitude, MainActivity.longitude)
                                val source: LatLng = LatLng(
                                    loginResponse.booking.get(0)!!.destination.latitude.toDouble(),
                                    loginResponse.booking.get(0)!!.destination.longitude.toDouble()
                                )
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
                3 -> {
                    if (MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        try {
                            if (loginResponse.booking != null) {
                                isArriving = false
                                isStarted = true
                                isTracking = false
                                if (mMap != null) {
                                    mMap!!.clear()
                                    mMap!!.isMyLocationEnabled = false
                                    if (myMarker != null)
                                        myMarker!!.remove()
                                   // switch_addTrip.visibility = View.GONE

                                }
                                if (tv_chat != null) {
                                    cl_chat.visibility = View.GONE
                                }

                                    val fragment = PayFragment()
                                    val b = Bundle()
                                    b.putSerializable(SENDEDDATA, loginResponse.booking.get(0))
                                    fragment.arguments = b
                                    MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)

                            }

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                4 -> {


                    if(loginResponse?.booking?.get(0)?.paymentMode.equals("card",true))
                    {

                        val fragment = PayFragment()
                        val b = Bundle()
                        b.putSerializable(SENDEDDATA, loginResponse.booking.get(0))
                        fragment.arguments = b
                        MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)
                    }
                    isTracking = false
                    isArriving = false
                    isStarted = false

                }
                5 -> {

                }
                6 -> {

                }
                7 -> {

                }
                8 -> {

                }
                9 -> {
                    try {
                        isTracking = false
                        isArriving = false
                        isStarted = false
                      /*  if (driverRideSocketResponse!!.type==0   ) {
                          //  switch_addTrip.visibility = View.VISIBLE
                        }*/


                        if (mMap != null) {
                            mMap!!.clear()

                            if (currentLocation != null) {
                                try {
                                    animateCamera(currentLocation!!)

                                } catch (ex: java.lang.Exception) {
                                    ex.printStackTrace()
                                }
                            }


                        }


                        switch_online.visibility = View.VISIBLE
                        ll_top_navigation.visibility = View.GONE
                        ll_user_bottom.visibility = View.GONE
                        MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                11 -> {
                    isTracking = false
                    isArriving = false
                    isStarted = false

                    try {
                        if (mMap != null) {
                            mMap!!.clear()

                            if (currentLocation != null) {
                                try {
                                    animateCamera(currentLocation!!)

                                } catch (ex: java.lang.Exception) {
                                    ex.printStackTrace()
                                }
                            }


                          /*  if (driverRideSocketResponse!!.type==0   ) {
                                switch_addTrip.visibility = View.VISIBLE
                            }*/

                        }
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)

                        showAlert(getString(R.string.canceled_by_user))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }

                14 -> {
                    Log.e("HomeFragment","14")
                    if (mMap != null) {
                        mMap!!.clear()
                        if (currentLocation != null) {
                            try {


                                animateCamera(currentLocation!!)

                            } catch (ex: java.lang.Exception) {
                                ex.printStackTrace()
                            }
                        }


                    }

                    try {
                        ll_top_navigation.visibility = View.GONE
                        ll_user_bottom.visibility = View.GONE
                        switch_online.visibility = View.VISIBLE
                    }
                    catch (e:java.lang.Exception)
                    {

                    }
                   /* if (MainActivity.mainActivity.getUserDetail().driver.vehicleType.type==0   ) {
                        switch_addTrip.visibility = View.VISIBLE
                    }*/
                 /*   if (driverRideSocketResponse!!.type==0   ) {
                        switch_addTrip.visibility = View.VISIBLE
                    }*/
                    if (!MainActivity.mainActivity.FragmentTag.toString().equals(HOMESCREEN)) {
                        Log.e("14","14")
                        MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                    }
                    MainActivity.mainActivity.onChangingfragment(AppConstants.HOMESCREEN)
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("1354","1354")
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
            sendObjectToSocket(params, NEW_LOGIN)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //Check driver ride status
    private fun driverRideStatus() {
        try {
            val params = JSONObject()
            params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
            params.put(BOOKING_ID, "")
            params.put("authToken", MainActivity.mainActivity.getAuthToken())
            Log.e("authToken", params.toString())
            sendObjectToSocket(params, DRIVER_RIDE_STATUS)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }



        /*try {
            val params = JSONObject()
            params.put(DRIVER_ID, "_"+MainActivity.mainActivity.getUserId())
            sendObjectToSocket(params, ONLINEDRIVER)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

*/

    }


    // Tracking   adriver
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


    //open google map using intent
    fun openMap(latLng: LatLng) {

        var navigationIntentUri = Uri.parse("google.navigation:q=" + DESTINATION_LATLNG.latitude +"," + DESTINATION_LATLNG.longitude);//creating intent with latlng
var mapIntent =  Intent(Intent.ACTION_VIEW, navigationIntentUri);
mapIntent.setPackage("com.google.android.apps.maps");
activity!!.startActivity(mapIntent);

    }


    // Draw polyline on map
    fun drawPolyLineOnMap(list: List<LatLng>) {

        var builder: LatLngBounds.Builder


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
            if (mMap != null) {
                mMap!!.isMyLocationEnabled = false
                if (myMarker != null)
                    myMarker!!.remove()
            }

            builder = LatLngBounds.Builder()
            mMap!!.addMarker(options).setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
            mMap!!.addMarker(options1).setIcon(BitmapDescriptorFactory.fromBitmap(sourcebitmap))


            builder.include(SOURCE_LATLNG)
            builder.include(DESTINATION_LATLNG)
            try {
                var bounds: LatLngBounds = builder.build()
                Log.e("1612", "1612")
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 190))
            } catch (e: java.lang.IllegalStateException) {

            }


            val url = getMapsApiDirectionsUrl()
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
            val params = "$origin&$dest&$sensor&mode=driving&key=" + getString(R.string.google_map_key)

            return "https://maps.googleapis.com/maps/api/directions/json?$params"
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
                            HomeFragment.homeFragment.setValueToText(text)
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
                jObject = JSONObject(jsonData[0])


                val parser = PathJSONParser()
                routes = parser.parse(jObject)


                return routes!!
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

                bearingValue = destination.bearing



                if (!OLDLATITUDE.toString().equals(MainActivity.latitude.toString())) {
                    OLDLATITUDE = MainActivity.latitude.toString()
                    val params = JSONObject()
                    if (driverRideSocketResponse!!.booking != null && driverRideSocketResponse!!.booking.size > 0) {
                        params.put(BOOKING_ID, driverRideSocketResponse!!.booking.get(0)!!.id)
                        params.put(LATITUDE, MainActivity.latitude)
                        params.put(LONGITUDE, MainActivity.longitude)
                        params.put(STATUS_SOCKET, driverRideSocketResponse!!.booking.get(0)!!.status)
                        params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            params.put(BEARING, bearingValue)
                        } else {
                            params.put(BEARING, bearingValue)

                        }
                        Log.e("TRACKING", params.toString())
                        sendObjectToSocket(params, SocketUrls.TRACKING)
                    }
                }
            }


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //Booking request listener response
    private val onBookingRequestListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onBookingRequestListener   " + args[0]+"")
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {
                        try {
                            val gson1 = Gson()
                            val loginResponse = gson1.fromJson(args[0].toString(), SocketBookingResponse::class.java)
                            if (loginResponse.success.toString().equals(TRUE)) {
                                // change UI elements here
                                val moneyTransferFragment = AcceptFragment()
                                val b = Bundle()
                                b.putParcelable(SENDEDDATA, loginResponse)
                                moneyTransferFragment.arguments = b
                                MainActivity.mainActivity.replaceFragment(
                                    moneyTransferFragment, AppConstants.ACCEPTSCREEN
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


    //chat  listener response
    private val onNewMessgaeListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onNewMessgaeListener   " + args[0]+"")
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {
                        try {
                            val gson1 = Gson()
                            val reqResponse = gson1.fromJson(args[0].toString(), NewMessageResponse::class.java)
                            if (reqResponse.success) {
                                if (reqResponse.msg != null && reqResponse.msg.opponentReadStatus == 0)
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


    //Get driver ride status response
    private val onGetDriveRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            /*try {*/
                Log.e(CLASSTAG, "call: onGetDriveRideStatusListener   " + args[0]+"")
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (loginResponse.success.toString().equals(TRUE)) {
                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                // change UI elements here
                                checkDriverStatus(loginResponse)
                            }
                        })

                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
           /* } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }*/
        }

    }
    // onDriver change status response
    private val onChangeStatus = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            try {
                Log.e(CLASSTAG, "call: onChangeStatus   " + args[0]+"")
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (!loginResponse.status.toString().isEmpty()) {

                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {
                                // change UI elements here
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
                Log.e(CLASSTAG, "call: onNewLogin   " + args[0]+"")
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
            progressDialog!!.dismiss()

            Log.e(CLASSTAG, "call: onDriverChangeRideStatusListener   " + args[0] + "")
            if (!driverRideSocketResponse?.booking?.get(0)?.paymentMode.equals(
                    "card",
                    true
                ) && driverRideSocketResponse?.status.equals("4")
            )
                driverRideStatus()

            try {
                val gson1 = Gson()
                val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                if (loginResponse.success.toString().equals(TRUE)) {
                    MainActivity.mainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            // change UI elements here
                            onChangeStatus(loginResponse)

                        }
                    })
                } else if (loginResponse.success.toString().equals("false")) {
                    val myObject = JSONObject(args[0].toString())
                    if (myObject.get("success").toString().equals("false")) {
                        if (myObject.get("status").toString().equals("9")) {
                            isTracking = false
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
        if (isCameraPermissions()) {
            val callback = object : GoogleMap.SnapshotReadyCallback {
                var bitmap: Bitmap? = null
                override fun onSnapshotReady(snapshot: Bitmap) {
                    bitmap = snapshot
                    try {

                        val file = File(
                            Environment.getExternalStorageDirectory().absolutePath + "/MyMapScreen" + ".png"
                        )
                        val out = FileOutputStream(file)
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)
                        val reqFileDriverLicence = RequestBody.create(MediaType.parse("image/*"), file)
                        val body =
                            MultipartBody.Part.createFormData("image", file.absolutePath, reqFileDriverLicence)
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
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 5368)
    }


    fun showNotesDialog(notes: String) {

        var noteDialog: Dialog? = null
        noteDialog = Dialog(context!!)
        noteDialog.setCancelable(false)
        noteDialog.setContentView(R.layout.dialog_note)
        if (noteDialog != null) {
            noteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        try {
            noteDialog.tv_note.text = notes
            noteDialog.tv_okay.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (noteDialog.isShowing)
                        noteDialog.dismiss()
                }
            })
            if (!noteDialog.isShowing)
                noteDialog.show()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    //to get msg read status
    private fun readMsgStatus(bookingId: String) {
        try {
            val params = JSONObject()
            params.put("bookingId", bookingId)
            params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
            sendObjectToSocket(params, ISREADMESSAGE)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    //to check is there any unread msg
    private val onReadMsgListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(TAG, "call: onReadMsgListener   " + args[0]+"")
            try {
                MainActivity.mainActivity.runOnUiThread(object : Runnable {
                    override fun run() {

                        try {
                            val gson1 = Gson()
                            val reqResponse = gson1.fromJson(args[0].toString(), NewMessageResponse::class.java)
                            if (reqResponse.success) {
                                try {
                                    if (reqResponse.result == 1)
                                        iv_chatIndicator.visibility = View.VISIBLE
                                    else
                                        iv_chatIndicator.visibility = View.GONE
                                }catch (e:java.lang.Exception)
                                {

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


}
