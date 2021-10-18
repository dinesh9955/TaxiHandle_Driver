package com.rydz.driver.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import com.rydz.driver.R
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import android.util.Log
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.CommonUtils.locationMethods.LocationResultInterface
import com.rydz.driver.CommonUtils.locationMethods.MyLocation
import com.rydz.driver.model.googleMapApiresponse.GoogleApiResponse
import com.rydz.driver.model.tripHistory.BookingHistory
import com.rydz.driver.view.mapUtils.HttpConnection
import com.rydz.driver.view.mapUtils.PathJSONParser
import kotlinx.android.synthetic.main.fragment_history_detail.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.json.JSONObject
import java.util.ArrayList


class HistoryDetailsActivity : BaseActivity(), OnMapReadyCallback,
    LocationResultInterface {

    private var mMap: GoogleMap? = null
    var bitmap: Bitmap?=null
    var sourcebitmap: Bitmap?=null
    var bookingData: BookingHistory?=null

    private var latitude = -33.852
    private var longitude = 151.211

    private var myLocation: MyLocation? = null

    private var SOURCE_LATLNG = LatLng(40.722543,
        -73.998585)
    private var DESTINATION_LATLNG = LatLng(40.7057, -73.9964)

    companion object {
       lateinit var historyDetailsActivity: HistoryDetailsActivity
    }
    private lateinit var nv_container: NestedScrollView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_history_detail)
        inits()
    }

   private fun inits()
   {
       historyDetailsActivity = this

       nv_container = findViewById(R.id.nv_container)

       bitmap=getBitmap(R.drawable.ic_original_car)
       sourcebitmap=getBitmap(R.drawable.ic_green_dot_small)
       myLocation = MyLocation()

       val mapFragment = supportFragmentManager
           .findFragmentById(R.id.maphistory) as SupportMapFragment
       mapFragment.getMapAsync(this)

       if (intent !=null) {
           bookingData = intent?.getParcelableExtra<BookingHistory>(AppConstants.SENDEDDATA)  as BookingHistory

       }


       tv_title_common.text=getString(R.string.triphistory)
       iv_back_common.setOnClickListener(this)
       iv_back_common.setImageResource(R.drawable.ic_back_black)


       if (bookingData!=null) {
           nv_container.visibility = View.VISIBLE
           tv_estimated_payment.setText(" "+   String.format("%1$.2f", bookingData!!.subtotalFare))
           tv_source.setText(bookingData!!.source.name.toString())
           tv_destination.setText(bookingData!!.destination.name.toString())
           tv_ride_fare.setText(getString(R.string.currencysign) + " " + String.format("%1$.2f",bookingData!!.subtotalFare - bookingData!!.tax))
           tv_taxes.setText(getString(R.string.currencysign) + " " + String.format("%1$.2f",bookingData!!.tax))
           tv_total_bill.setText(getString(R.string.currencysign) + " " +String.format("%1$.2f", bookingData!!.subtotalFare))
           if(bookingData!!.tripStartTime.compareTo(0)!=0)
           tv_source_time.setText(Utils.getTime(bookingData!!.tripStartTime))
           if(bookingData!!.tripEndTime.compareTo(0)!=0)
               tv_destination_time.setText(Utils.getTime(bookingData!!.tripEndTime))
           if (bookingData!!.vehicleType != null)
               tv_vehicle_type.setText(bookingData!!.vehicleType.name)
           latitude = bookingData!!.source.latitude.toDouble()
           longitude = bookingData!!.source.longitude.toDouble()
           SOURCE_LATLNG =
               LatLng(bookingData!!.source.latitude.toDouble(), bookingData!!.source.longitude.toDouble())
           DESTINATION_LATLNG =
               LatLng(bookingData!!.destination.latitude.toDouble(), bookingData!!.destination.longitude.toDouble())

           goToMYLocation()




           try {

               if (bookingData?.refund != null &&   bookingData!!.refund?.refundAmount?.compareTo(0.0)!! > 0 ) {
                   tv_refund.setText(
                       getString(R.string.currencysign) + " " + String.format(
                           "%1$.2f",
                           bookingData!!.refund?.refundAmount
                       )
                   )


                   ll_refund.visibility = View.VISIBLE
               } else
                   ll_refund.visibility = View.GONE
           }
           catch ( e: java.lang.Exception)
           {

           }
       }
       }

    override fun handleCarLocation(location: Location?) {
        goToMYLocation()
    }

    override fun handleNewLocation(location: Location?) {
        goToMYLocation()
    }

    override fun handleOneLocation(location: Location?) {
        goToMYLocation()
    }



    private fun goToMYLocation() {

        if (MainActivity.mainActivity.checkGpsService()) {
            drawMarker(latitude, longitude)
        }

    }


    fun drawMarker(lat: Double?, lng: Double?) {
        if (mMap != null) {
//            mMap!!.clear()
            val gps = LatLng(lat!!, lng!!)
            latitude = lat!!
            longitude = lng!!
            if (gps != null) {
                mMap!!.addMarker(
                    MarkerOptions().position(gps)
                        .title("").icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f,0.5f).draggable(true)
                )
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 10f))

                val list: ArrayList<LatLng> = ArrayList<LatLng>()
                list.add(SOURCE_LATLNG)
                list.add(DESTINATION_LATLNG)
                drawPolyLineOnMap(list)


            }
            mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    return true
                }
            })
        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap!!.uiSettings.setAllGesturesEnabled(false)
        mMap!!.getUiSettings().setMapToolbarEnabled(false)
        if (ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap!!.setMyLocationEnabled(false)
        mMap!!.setMinZoomPreference(10f)

        goToMYLocation()

    }

    private fun getBitmap(drawableRes:Int): Bitmap {
        val drawable = getResources().getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
        drawable.draw(canvas)
        return bitmap
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
finish()            }

        }
    }



    // Draw polyline on map
    fun drawPolyLineOnMap(list:List<LatLng>) {
        SOURCE_LATLNG=list.get(0)
        DESTINATION_LATLNG=list.get(1)
        val options = MarkerOptions()
        options.position(SOURCE_LATLNG)
        options.position(DESTINATION_LATLNG)
//        if (mMap!=null) {
//            mMap!!.addMarker(options)
//        }
        val url = getMapsApiDirectionsUrl()
        val downloadTask = ReadTask()
        downloadTask.execute(url)
        addMarkers()
    }

    private fun getMapsApiDirectionsUrl():String {
        val origin = "origin=" + SOURCE_LATLNG.latitude + "," + SOURCE_LATLNG.longitude
        val dest = "destination=" + DESTINATION_LATLNG.latitude + "," + DESTINATION_LATLNG.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor&mode=driving&key="+getString(R.string.google_map_key)
//        val params = "$origin&$dest&$sensor&mode=driving&key=AIzaSyB_EHv3Hz6o0E2pmX-WovIgZDVmU26Y6p0"
//        Log.e("797","https://maps.googleapis.com/maps/api/directions/json?$params")
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun addMarkers() {
        if (mMap != null)
        {
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(0.5f,0.5f).position(SOURCE_LATLNG))
            mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(sourcebitmap)).anchor(0.5f,0.5f).position(DESTINATION_LATLNG))
            val latLng:LatLng= LatLng(SOURCE_LATLNG.latitude,SOURCE_LATLNG.longitude)
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
        }
    }

    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(11f).build()
    }

    private class ReadTask: AsyncTask<String, Void, String>() {
        override protected fun doInBackground(vararg url:String):String {
            var data = ""
            try
            {
                val http = HttpConnection()
                data = http.readUrl(url[0])
            }
            catch (e:Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }
        override  protected fun onPostExecute(result:String) {
            super.onPostExecute(result)

            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(result.toString(), GoogleApiResponse::class.java)
            if (loginResponse!=null)
            {
                try {
                    if (loginResponse.routes!=null && loginResponse.routes.size>0)
                    {
                        if (loginResponse.routes.get(0).legs!=null && loginResponse.routes.get(0).legs.size>0)
                        {
                            val text:String=loginResponse.routes.get(0).legs.get(0).duration.text
                            val text2= loginResponse.routes.get(0).legs.get(0).distance.text
                            historyDetailsActivity.setValuetoText(text,text2)

                        }
                    }
                }
                catch (ex:Exception)
                {
                    ex.printStackTrace()
                }


            }

            Log.e("READ TASK : ",result+"")
            ParserTask().execute(result)
        }
    }

    public fun setValuetoText(text: String, text2: String?) {
        try {
            if (tv_travel_time!=null) {
                tv_travel_time.setText(text)
                tv_distance.setText(text2)
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    public  class ParserTask : AsyncTask<String, Int,  List<List<HashMap<String, String>>>>() {
        override protected fun doInBackground(
            vararg jsonData: String
        ):  List<List<HashMap<String, String>>> {
            val jObject: JSONObject
            var routes:  List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = PathJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes!!
        }

        override protected fun onPostExecute(routes:  List<List<HashMap<String, String>>>) {

            var points: ArrayList<LatLng>? = null
            var polyLineOptions: PolylineOptions? = null
            // traversing through routes
            for (i in 0 until routes.size) {
                points = ArrayList()
                polyLineOptions = PolylineOptions()
                val path = routes[i]
                for (j in 0 until path.size) {
                    val point = path.get(j)
                    val lat = java.lang.Double.parseDouble(point.get("lat"))
                    val lng = java.lang.Double.parseDouble(point.get("lng"))
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                polyLineOptions.addAll(points)
                polyLineOptions.width(4f)
                polyLineOptions.color(Color.BLACK)

            }
            if (historyDetailsActivity.mMap!=null) {
                if (polyLineOptions!=null) {
                    historyDetailsActivity.mMap!!.addPolyline(polyLineOptions)
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
