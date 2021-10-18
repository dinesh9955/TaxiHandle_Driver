package com.rydz.driver.view.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.rydz.driver.CommonUtils.PathResponse
import com.rydz.driver.CommonUtils.PermissionUtils
import com.rydz.driver.CommonUtils.PlacesEventObject
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.model.socketResponse.Source
import kotlinx.android.synthetic.main.activity_add_trip.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.async
import org.jetbrains.anko.enabled
import org.jetbrains.anko.uiThread
import java.net.URL
import java.util.*

class AddTripActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {


    //will be intialized after hiting api of google maps directions
    var tripDistance: Double? = 0.0
    var tripTime: String? = null
    var mMap: GoogleMap? = null
    lateinit var placesClient: PlacesClient
    lateinit var location: Location
    lateinit var apiKey: String
    lateinit var request: FindAutocompletePredictionsRequest
    lateinit var token: AutocompleteSessionToken
    var isSetPinLocation = false
    var sourcePlace: Source? = null
    var destinationPlace: Source? = null
    lateinit var gson: Gson
    var bitmap: Bitmap? = null
    var isFocusOnSourse = false
    var progressDialog: ProgressDialog? = null
    var countryCode: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)
        inits()

    }


    override fun onCameraIdle() {
        if (isSetPinLocation) {

            if (isFocusOnSourse) {

                tv_source.text = ""
                tv_source.text = "Loading..."
                try {
                    val geocoder: Geocoder
                    val addresses: List<Address>
                    geocoder = Geocoder(this@AddTripActivity, Locale.getDefault())
                    addresses = geocoder.getFromLocation(
                        mMap!!.cameraPosition!!.target.latitude,
                        mMap!!.cameraPosition!!.target.longitude,
                        1
                    ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    // Log.e("2042", addresses.get(0).toString())

                    val address = addresses.get(0)
                        .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    sourcePlace =
                        Source(addresses.get(0).latitude, addresses.get(0).longitude, address)
                    tv_source.text = address


                } catch (e: Exception) {
                    Toast.makeText(this@AddTripActivity, R.string.offline_note, Toast.LENGTH_SHORT).show()
                }

            } else {


                tv_des.text = ""
                tv_des.text = "Loading..."



                try {
                    val geocoder: Geocoder
                    val addresses: List<Address>
                    geocoder = Geocoder(this@AddTripActivity, Locale.getDefault())
                    addresses = geocoder.getFromLocation(
                        mMap!!.cameraPosition!!.target.latitude,
                        mMap!!.cameraPosition!!.target.longitude,
                        1
                    ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.e("125", addresses.get(0).toString())

                    val address = addresses.get(0)
                        .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    destinationPlace =
                        Source(addresses.get(0).latitude, addresses.get(0).longitude, address)
                    tv_des.text = address


                } catch (e: Exception) {
                    Toast.makeText(this@AddTripActivity, R.string.offline_note, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun inits() {
        tv_title.text = getString(R.string.addTrip)

        bitmap = getBitmap(R.drawable.car_marker)
        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //places api
        apiKey = getString(R.string.places_api_key)
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        placesClient = Places.createClient(this)
        token = AutocompleteSessionToken.newInstance()
        setLocation()

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.iv_back -> {

                clearEventBusData()
                finish()
            }

            R.id.tv_confirm -> {

              /*  if (sourcePlace != null && destinationPlace != null && !sourcePlace!!.latitude!!.isEmpty() && !destinationPlace!!.latitude!!.isEmpty() && sourcePlace!!.latitude!!.toDouble() > 0.0 && destinationPlace!!.latitude!!.toDouble() > 0.0) {
                    progressDialog!!.show()
                    tv_confirm.enabled=false
                    findEstimation()

                } else {
                    Toast.makeText(
                        this@AddTripActivity,
                        "Please make sure you have selected both source and destination location",
                        Toast.LENGTH_SHORT
                    ).show()
                }*/

            }
            R.id.tv_des -> {
                isFocusOnSourse = false
                val location_Intent = Intent(this@AddTripActivity, LocationSetActivity::class.java)
                location_Intent.putExtra("FROM", "DESTINATION")
                location_Intent.putExtra("COUNTRYCODE", countryCode)

                startActivityForResult(location_Intent, PermissionUtils.LOCATIONPERMISSIONCODE)

            }
            R.id.tv_source -> {
                isFocusOnSourse = true
                val location_Intent = Intent(this@AddTripActivity, LocationSetActivity::class.java)
                location_Intent.putExtra("FROM", "SOURCE")
                location_Intent.putExtra("COUNTRYCODE", countryCode)

                startActivityForResult(location_Intent, PermissionUtils.LOCATIONPERMISSIONCODE)
            }
        }
    }

    override fun onMapReady(mMap: GoogleMap?) {
        if (mMap != null) {
            this.mMap = mMap


            this.mMap!!.isMyLocationEnabled = false
            drawMarker(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0))


        }

    }


    /**
     * To automatically set current location
     */
    private fun setLocation() {

        val placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.builder(placeFields).build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result


                    if (response != null && response.placeLikelihoods.size > 0) {
                        tv_source.text = response.placeLikelihoods[0].place.name + "," + response.placeLikelihoods.get(
                            0
                        ).place.address
                        Log.e(
                            "2566", response.placeLikelihoods[0].place.plusCode.toString()
                        )



                        sourcePlace = Source(
                            response.placeLikelihoods[0].place.latLng!!.latitude,
                            response.placeLikelihoods[0].place.latLng!!.longitude,
                            response.placeLikelihoods[0].place.name + "," + response.placeLikelihoods.get(0).place.address
                        )
                        getCountryCode(
                            response.placeLikelihoods[0].place.latLng!!.latitude,
                            response.placeLikelihoods[0].place.latLng!!.longitude
                        )

                    }
                    for (placeLikelihood in response!!.placeLikelihoods) {
                        Log.e(
                            "TAGGGGG", String.format(
                                "Place '%s' has likelihood: %f",
                                placeLikelihood.place.name,
                                placeLikelihood.likelihood
                            )
                        )
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        val apiException = exception as ApiException?
                        Log.e("TAGGGGG", "Place not found: " + apiException!!.statusCode)
                    }
                }
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == PermissionUtils.LOCATIONPERMISSIONCODE) {
            if (EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java) != null) {


                isSetPinLocation = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).isPin
                if (isFocusOnSourse) {
                    sourcePlace = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).src
                    getCountryCode(sourcePlace!!.latitude.toDouble(), sourcePlace!!.longitude.toDouble())

                    //countryCode=sourcePlace.
                } else
                    destinationPlace = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).destination


                setPinLocation(EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).isFocusOnSource)


            }

        }

    }


    //set location using pin functionality
    fun setPinLocation(isFocusOnSrc: Boolean) {


        if (mMap != null) {
            // mMap!!.clear()
            iv_loc_pin.visibility = View.VISIBLE
            mMap!!.setOnCameraIdleListener(this@AddTripActivity)


            if (isFocusOnSrc) {

                tv_source.text = sourcePlace!!.name
                // drawMarker(sourcePlace!!.latitude!!.toDouble(), sourcePlace!!.longitude!!.toDouble())

                try {
                    val gps = LatLng(sourcePlace!!.latitude!!.toDouble(), sourcePlace!!.longitude!!.toDouble())

                    if (gps != null) {

                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13f))
                    }
                } catch (e: java.lang.Exception) {
                }
                try{
                Glide.with(this).load(R.drawable.blue_pin).into(iv_loc_pin)
                }catch (e:Exception)
                {

                }
            } else {
                tv_des.text = destinationPlace!!.name

try{
                Glide.with(this).load(R.drawable.blue_pin).into(iv_loc_pin)
}catch (e:Exception)
{

}

                try {
                    val gps =
                        LatLng(destinationPlace!!.latitude!!.toDouble(), destinationPlace!!.longitude!!.toDouble())

                    if (gps != null) {

                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13f))
                    }
                } catch (e: java.lang.Exception) {
                }


            }


        }
    }

    fun drawMarker(lat: Double?, lng: Double?) {


        // animate a camera on map
        try {
            if (mMap != null) {
                val gps = LatLng(lat!!, lng!!)

                if (gps != null) {

                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13f))
                    mMap!!.addMarker(
                        MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).anchor(
                            1.2f,
                            1.2f
                        ).position(gps).flat(true)
                    )

                }
                mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                    override fun onMarkerClick(marker: Marker): Boolean {
                        return true
                    }
                })
            }
        } catch (e: Exception) {

        }
    }


    private fun findEstimation() {
        val url = Utils(this@AddTripActivity).getURL(
            LatLng(
                sourcePlace!!.latitude!!.toDouble(),
                sourcePlace!!.longitude!!.toDouble()
            ), LatLng(destinationPlace!!.latitude!!.toDouble(), destinationPlace!!.longitude!!.toDouble())
        )
        async {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            gson = Gson()
            var pathResponse: PathResponse = gson.fromJson(result, PathResponse::class.java)

            progressDialog!!.dismiss()
            uiThread {

                if (pathResponse.routes != null && pathResponse.routes!!.size > 0) {
                    //set time estimation
                    tripTime = pathResponse.routes!!.get(0)!!.legs!!.get(0)!!.duration!!.text.toString()

                    tripDistance = pathResponse.routes!!.get(0)!!.legs!!.get(0)!!.distance!!.text!!.replace(
                        ",",
                        ""
                    ).split(" ")[0].toDouble()
                    Log.e("317", tripTime + tripDistance!!)

                    val confirm_Intent = Intent(this@AddTripActivity, ConfirmTripActivity::class.java)
                    clearEventBusData()
                    val placesEventObject =
                        PlacesEventObject(sourcePlace!!, destinationPlace!!, true, tripDistance!!, true)


                    EventBus.getDefault().postSticky(placesEventObject)
                    startActivityForResult(confirm_Intent, 11)


                } else {

                    Toast.makeText(
                        this@AddTripActivity,
                        "Make sure you have added correct locations!",
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }
        }

    }

    //to clear event bus data
    private fun clearEventBusData() {
        try {
            EventBus.getDefault().removeStickyEvent(PlacesEventObject::class.java)
        } catch (e: Exception) {

        }

    }


    override fun onResume() {
        super.onResume()

        tv_confirm.enabled=true
    }

    //Create a bitmap from drawable
    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }


    private fun getCountryCode(lat: Double, lng: Double) {
        var geocoder = Geocoder(this@AddTripActivity, Locale.getDefault())
        var addresses = geocoder.getFromLocation(lat, lng, 1)
        var countryName = addresses.get(0).countryCode
        Log.e("503", countryName)
        countryCode = countryName
    }
}
