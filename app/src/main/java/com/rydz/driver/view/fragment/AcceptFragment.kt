package com.rydz.driver.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Urls.POOLINGTYPE
import com.rydz.driver.model.socketResponse.SocketBookingResponse
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls.BOOKINGREQUESTRESPONSE
import com.rydz.driver.view.activity.MainActivity
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_accept.*
import org.json.JSONArray
import org.json.JSONObject

class AcceptFragment : BaseFragment(), OnMapReadyCallback, SocketListener {

    private val CLASSTAG: String = AcceptFragment::class.java.simpleName
    private var mMap: GoogleMap? = null
    private var latitude = -33.852
    private var longitude = 151.211
    private var myMarker: Marker? = null
    var bitmap: Bitmap? = null
    var mCountDownTimer: CountDownTimer? = null
    var gson = Gson()
    var i = 0
    val REJECT: Int = 2
    val ACCEPT: Int = 1
    var driverRideStatus: SocketBookingResponse? = null
    lateinit var notification: Uri
    lateinit var r: Ringtone


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_accept, container, false)

        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        r = RingtoneManager.getRingtone(context, notification)
        MainActivity.mainActivity.onChangingfragment(AppConstants.ACCEPTSCREEN)
        AppSocketListener.getInstance().setActiveSocketListener(this)
        // Restart Socket.io to avoid weird stuff ;-)
        AppSocketListener.getInstance().restartSocket()

        //acceptFragment = this
        try {
            if (arguments != null) {

                driverRideStatus = arguments!!.getParcelable<SocketBookingResponse>(AppConstants.SENDEDDATA) as SocketBookingResponse
            }
            bitmap = getBitmap(R.drawable.ic_green_dot_small)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return view
    }

    override fun onResume() {
        super.onResume()

        isAcceptScreen = true
    }

    private fun convertIntoJSONOBject(driverRideStatus: SocketBookingResponse, status: Int) {
        Log.e("97", "97")
        try {
            val myJSonObject = JSONObject()
            val source = JSONObject()
            val destination = JSONObject()
            val stopPoint = JSONArray()
            val stopPointObject = JSONObject()

            //Source
            source.put(LATITUDE, driverRideStatus.booking.source.latitude)
            source.put(LONGITUDE, driverRideStatus.booking.source.longitude)
            source.put(NAME, driverRideStatus.booking.source.name)

            //Destination
            destination.put(LATITUDE, driverRideStatus.booking.destination.latitude)
            destination.put(LONGITUDE, driverRideStatus.booking.destination.longitude)
            destination.put(NAME, driverRideStatus.booking.destination.name)

            /*    //Destination
                if (driverRideStatus.booking.stopPoints != null && driverRideStatus.booking.stopPoints.size > 0) {
                    for (i in 0 until driverRideStatus.booking.stopPoints.size) {
                        stopPointObject.put(LATITUDE, driverRideStatus.booking.stopPoints.get(i).latitude)
                        stopPointObject.put(LONGITUDE, driverRideStatus.booking.stopPoints.get(i).longitude)
                        stopPointObject.put(NAME, driverRideStatus.booking.stopPoints.get(i).name)
                        stopPoint.put(stopPointObject)
                    }
                }
    */
            myJSonObject.put(SOURCE, source)
            if (driverRideStatus.booking.date != null)
                myJSonObject.put("date", driverRideStatus.booking.date)
            myJSonObject.put(DESTINATION, destination)
            // myJSonObject.put(STOP_POINTS, stopPoint)
            myJSonObject.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
            myJSonObject.put(ADMIN_ID, MainActivity.mainActivity.getAdminId())
            myJSonObject.put(USER_ID_SOCKET, driverRideStatus.booking.userId)
            myJSonObject.put(FARE, driverRideStatus.booking.fare)
            myJSonObject.put(SUBTOTALFARE, driverRideStatus.booking.subtotalFare)
            myJSonObject.put(STATUS_SOCKET, status)
            myJSonObject.put("note", driverRideStatus.booking.note)


            if (driverRideStatus.booking.bookingType != null)
                myJSonObject.put("bookingType", driverRideStatus.booking.bookingType)

            if (driverRideStatus.booking._id != null)
                myJSonObject.put("_id", driverRideStatus.booking._id)


            try {
                myJSonObject.put("storeId", driverRideStatus.booking.storeId)
                myJSonObject.put("firstName", driverRideStatus.booking.firstName)
                myJSonObject.put("lastName", driverRideStatus.booking.lastName)
                myJSonObject.put("email", driverRideStatus.booking.email)
                myJSonObject.put("CountryCode", driverRideStatus.booking.CountryCode)
                myJSonObject.put("phone", driverRideStatus.booking.phone)
                myJSonObject.put("isPaid", driverRideStatus.booking.isPaid)
            } catch (e: Exception) {
                myJSonObject.put("isPaid", 0)
            }
            if (driverRideStatus.booking.scheduleId != null)
                myJSonObject.put("scheduleId", driverRideStatus.booking.scheduleId)

            myJSonObject.put(PAYMENT_MODE, driverRideStatus.booking.paymentMode)
            myJSonObject.put(VEHICLE_TYPE, driverRideStatus.booking.vehicleType)
            myJSonObject.put(SOCKET_ID, driverRideStatus.booking.socketId)
            myJSonObject.put("cardId", driverRideStatus.booking.cardId)
            try {
                myJSonObject.put("walletAmountUsed", driverRideStatus.booking.walletAmountUsed)
            }catch (e : java.lang.Exception)
            {
                myJSonObject.put("walletAmountUsed", 0.0)
            }

            try {
                myJSonObject.put("tax", driverRideStatus.booking.tax)
            }
                catch (e : java.lang.Exception)
                {
                    myJSonObject.put("tax", 0.0)
                }

            try {

                myJSonObject.put("distance", driverRideStatus.booking.distance)
            } catch (e: Exception) {

            }
          /*  try {
                myJSonObject.put("slabs", driverRideStatus.booking.slabs)
            } catch (e: Exception) {

            }*/
            try {
                myJSonObject.put("isPaidInitial", driverRideStatus.booking.isPaidInitial)
                myJSonObject.put("itemAmount", driverRideStatus.booking.itemAmount)


            } catch (e: Exception) {

                myJSonObject.put("isPaidInitial", 0)
                myJSonObject.put("itemAmount", 0)
            }
            Log.e("AcceptData", myJSonObject.toString())



            sendObjectToSocket(myJSonObject, BOOKINGREQUESTRESPONSE)
        } catch (ex: Exception) {
            ex.printStackTrace()


        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tv_accept.setOnClickListener(this)
        tv_reject.setOnClickListener(this)

        try {
            if (driverRideStatus != null) {


                try {
                    tv_user_address.text = activity!!.getString(R.string.pickup) + ": " + driverRideStatus!!.booking.source.name

                } catch (e: Exception) {

                }
                tv_destination.text = activity!!.getString(R.string.dropoff) + ": " + driverRideStatus!!.booking.destination.name
                if (driverRideStatus!!.booking.userRating != null) {
                    ratingBar_user.rating = driverRideStatus!!.booking.userRating.toFloat()
                } else {
                    ratingBar_user.rating = 0.0f

                }
                try {
                    if (driverRideStatus!!.booking.note != null && driverRideStatus!!.booking.note.length > 0)
                        tv_notes.text = activity!!.getString(R.string.note) + ": " + driverRideStatus!!.booking.note

                } catch (e: Exception) {

                }
                latitude = driverRideStatus!!.booking.source.latitude.toDouble()
                longitude = driverRideStatus!!.booking.source.longitude.toDouble()


                try {
                    if (driverRideStatus!!.cancelledRides != null)
                        tv_cancellationRate.text = activity!!.getString(R.string.cancellationRate) + ": " + driverRideStatus!!.cancelledRides + "%"
                    else
                        tv_cancellationRate.text = activity!!.getString(R.string.cancellationRate) + ": 0%"


                } catch (e: Exception) {
                    tv_cancellationRate.text = activity!!.getString(R.string.cancellationRate) + ": 0%"

                }
                tv_remaining_time.text = driverRideStatus!!.booking.triptime
                goToMYLocation()
                try {
                    setNoteValue(driverRideStatus!!.booking.note.toString().trim())
                } catch (e: Exception) {

                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            progressBar.progress = i


            mCountDownTimer = object : CountDownTimer(16000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    i++
                    try {
                        if (progressBar != null) {
                            progressBar.progress = i
                            r.play()

                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }

                override fun onFinish() {
                    r.stop()
                    MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                    mCountDownTimer = null
                }
            }
            mCountDownTimer!!.start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.tv_accept -> {
                r.stop()
                if (driverRideStatus != null) {
                    try {
                        convertIntoJSONOBject(driverRideStatus!!, ACCEPT)
                        if (mCountDownTimer != null) {
                            mCountDownTimer!!.cancel()
                        }
                        tv_accept.visibility = View.GONE
                        tv_reject.visibility = View.GONE


                        Handler().postDelayed(Runnable {

                            if (!driverRideStatus!!.booking!!.vehicleType.equals(POOLINGTYPE)) {


                                MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                            } else {

                                isPathSend = true
                                MainActivity.mainActivity.replaceFragment(PoolRideFragment(), POOLSCREEN)
                            }

                        }, 1000)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }

            }
            R.id.tv_reject -> {
                promptPopUp()
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.setAllGesturesEnabled(false)
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
        mMap!!.isMyLocationEnabled = false
        mMap!!.setMinZoomPreference(13f)
        mMap!!.setMaxZoomPreference(18f)
        goToMYLocation()

    }

    private fun goToMYLocation() {

        MainActivity.mainActivity.runOnUiThread(Runnable {
            if (MainActivity.mainActivity.checkGpsService()) {
                drawMarker(latitude, longitude)
            }
        })


    }

    private val onGetDriveRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {

            Log.e(CLASSTAG, "call: onGetDriveRideStatusListener   " + args[0] + "")
            try {
                val jsonObject: JSONObject = JSONObject(args[0].toString())
                if (jsonObject.get("success").toString().equals("false")) {
                    MainActivity.mainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            // change UI elements here
                            MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                            showAlert(jsonObject.get("message").toString())

                        }
                    })
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }

    }


    fun drawMarker(lat: Double?, lng: Double?) {

        try {
            if (mMap != null) {
                val gps = LatLng(lat!!, lng!!)
                latitude = lat
                longitude = lng
                if (gps != null) {
                    myMarker = mMap!!.addMarker(
                        MarkerOptions().position(gps)
                            .title("").icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f, 0.5f).draggable(
                                false
                            )
                    )
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13f))

                    MainActivity.mainActivity.startAThread(latitude, longitude)

                }
                mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                    override fun onMarkerClick(marker: Marker): Boolean {
                        return true
                    }
                })
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

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


    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun canAccessCoreLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {

        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context!!, perm)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("OnDestroy: ", "OnDestroy")
        try {
            if (mCountDownTimer != null) {
                mCountDownTimer!!.cancel()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun change(boolean: Boolean) {
        try {
            if (!boolean) {
                if (mCountDownTimer != null) {
                    mCountDownTimer!!.cancel()
                }
                MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun sendObjectToSocket(jsonObject: JSONObject, type: String) {
        AppSocketListener.getInstance().emit(type, jsonObject)
    }


    override fun onSocketConnected() {
        Log.e(CLASSTAG, "onSocketConnected")
        AppSocketListener.getInstance().off("bookingRequestResponse", onGetDriveRideStatusListener)
        AppSocketListener.getInstance().addOnHandler("bookingRequestResponse", onGetDriveRideStatusListener)
//        AppSocketListener.getInstance().off(BOOKINGREQUESTRESPONSE, onBookingRequestResponseListener)
//        AppSocketListener.getInstance().addOnHandler(BOOKINGREQUESTRESPONSE, onBookingRequestResponseListener)
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


    /**
     * To logout from app
     */
    fun promptPopUp() {
        val logoutDial = Dialog(MainActivity.mainActivity)
        //        logoutDial.setTitle(R.string.Logout);
        logoutDial.requestWindowFeature(Window.FEATURE_NO_TITLE)
        logoutDial.setContentView(R.layout.prompt_dialog)
        val cancel = logoutDial.findViewById<TextView>(R.id.tv_cancel)
        val ok = logoutDial.findViewById<TextView>(R.id.tv_ok)
        cancel.setOnClickListener(View.OnClickListener {
            logoutDial.dismiss()
        })
        ok.setOnClickListener(View.OnClickListener {
            logoutDial.dismiss()

            try {
                if (driverRideStatus != null) {
                    convertIntoJSONOBject(driverRideStatus!!, REJECT)
                    if (mCountDownTimer != null) {
                        mCountDownTimer!!.cancel()
                    }
                    MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }


        })
        logoutDial.show()
    }


}