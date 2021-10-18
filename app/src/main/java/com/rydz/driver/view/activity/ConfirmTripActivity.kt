package com.rydz.driver.view.activity

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.PlacesEventObject
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.requests.VehicleRateDetails
import com.rydz.driver.model.socketResponse.Source
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls
import com.rydz.driver.viewModel.ConfirmTripViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_add_trip.tv_des
import kotlinx.android.synthetic.main.activity_add_trip.tv_source
import kotlinx.android.synthetic.main.activity_add_trip.tv_title
import kotlinx.android.synthetic.main.activity_confirm_trip.*
import kotlinx.android.synthetic.main.activity_confirm_trip.ccp
import kotlinx.android.synthetic.main.activity_confirm_trip.et_email
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.enabled
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class ConfirmTripActivity : BaseActivity(), SocketListener {


    var sourcePlace: Source? = null
    var destinationPlace: Source? = null
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ConfirmTripViewModel? = null
    var progressDialog: ProgressDialog? = null
    var totalFare: Double = 0.0
    var vehicleTypeId = ""
    var tripDistance: Double? = 0.0
    val CLASSTAG: String = ConfirmTripActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_trip)
        AppSocketListener.getInstance().setActiveSocketListener(this)
        // Restart Socket.io to avoid weird stuff ;-)
        AppSocketListener.getInstance().restartSocket()
        inits()
    }


    private fun inits() {

        progressDialog = Constant.getProgressDialog(this, "Please wait...")
        (application as App).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmTripViewModel::class.java!!)
        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
        tv_title.text = getString(R.string.cofirm_ride)


        ccp.setAutoDetectedCountry(true)
        ccp_wn.setAutoDetectedCountry(true)

        if (EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java) != null) {

            sourcePlace = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).src
            destinationPlace = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).destination
            tripDistance=EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).tripDistance
            EventBus.getDefault().removeStickyEvent(PlacesEventObject::class.java)
            tv_source.text = sourcePlace!!.name
            tv_des.text = destinationPlace!!.name

            if (!Constant.checkInternetConnection(this)) {
            } else {
                if (MainActivity.mainActivity.getUserDetail().driver != null) {
                    vehicleTypeId = MainActivity.mainActivity.getUserDetail()!!.driver!!.vehicleType._id
                    viewModel!!.hitGetVehicleRate(MainActivity.mainActivity.getUserDetail()!!.driver!!.vehicleType._id)
                }

            }

        }

    }

    /*
    * method to handle response
    * */
    private fun consumeResponse(apiResponse: ApiResponse) {
        tv_createRide.enabled=true
        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {

                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
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
        if (!response.isJsonNull) {
            val data: String = Utils.toJson(response)
            val gson1 = Gson();
            val apiResponse = gson1.fromJson(data, VehicleRateDetails::class.java)
            if (apiResponse.success.toString().equals(AppConstants.TRUE) && apiResponse.vehicletypedetail != null) {


                totalFare = estimateFare(
                    tripDistance!!.toString().replace(",", "").toDouble(),
                    apiResponse.vehicletypedetail.fareRate!!.toString().replace(",", "").toDouble(),
                    apiResponse.vehicletypedetail.baseFare!!.toString().replace(",", "").toDouble(),
                    apiResponse.vehicletypedetail.fareChangekm!!.toString().replace(",", "").toDouble(),
                    apiResponse.vehicletypedetail.fareRateAfter!!.toString().replace(",", "").toDouble()
                )

                Log.e("116", totalFare.toString())
            } else {
                showAlert(apiResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.tv_createRide -> {

                if (validate()) {
                    tv_createRide.enabled=false
                    progressDialog!!.show()
                    val myObject = JSONObject()
                    try {
                        var sourceObject = JSONObject()
                        sourceObject.put(AppConstants.LATITUDE, sourcePlace!!.latitude)
                        sourceObject.put(AppConstants.LONGITUDE, sourcePlace!!.longitude)
                        sourceObject.put(AppConstants.NAME, sourcePlace!!.name)

                        myObject.put(AppConstants.SOURCE, sourceObject) //added source

                        sourceObject = JSONObject()
                        sourceObject.put(AppConstants.LATITUDE, destinationPlace!!.latitude)
                        sourceObject.put(AppConstants.LONGITUDE, destinationPlace!!.longitude)
                        sourceObject.put(AppConstants.NAME, destinationPlace!!.name)

                        myObject.put(AppConstants.DESTINATION, sourceObject)  //added destination


                        myObject.put(AppConstants.DRIVER_ID, getUserId())
                        myObject.put(AppConstants.ADMIN_ID, getAdminId())
                        myObject.put(AppConstants.VEHICLE_TYPE, vehicleTypeId)

                        myObject.put(AppConstants.FARE, totalFare)
                        myObject.put(AppConstants.PAYMENT_MODE, "cash")
                        myObject.put(AppConstants.KEY_PHONE, ccp.selectedCountryCode+""+et_phone_number.text.toString())
                        myObject.put(AppConstants.FIRSTNAME, et_name.text.toString())
                        myObject.put(AppConstants.KEY_EMAIL, et_email.text.toString())
                        myObject.put(AppConstants.WTSAPPNUMBER, ccp_wn.selectedCountryCode+""+et_whatsappphone_number.text.toString())
                        myObject.put(AppConstants.STATE, "Punjab")


                        sendObjectToSocket(myObject, SocketUrls.DRIVERBOOKING)


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }


    //calculate rate as per distance, base fare and fare rate
    private fun estimateFare(
        distance: Double,
        fareRate: Double,
        baseFare: Double,
        baseDistance: Double,
        fareRateAfter: Double
    ): Double {
        var estimateFare:Double=0.0

        if (distance > baseDistance) {
            var additinalDistance= distance - baseDistance
            estimateFare=(baseDistance*fareRate)+(additinalDistance*fareRateAfter)


            // String.format("%1$.2f", fareRateAfter * distance).replace(",", ".").toDouble()
        } else {

            estimateFare=fareRate * distance

        }
        if (estimateFare<= baseFare)  //use of replace(",", ".") to handle double format issue in turkish language
            estimateFare=   String.format("%1$.2f", baseFare).replace(",", ".").toDouble()
       /* else
            estimateFare= String.format("%1$.2f", fareRate * distance).replace(",", ".").toDouble()
*/
        return estimateFare


        /*   if (distance < baseDistance) {
               if (fareRate * distance <= baseFare)  //use of replace(",", ".") to handle double format issue in turkish language
                   return String.format("%1$.2f", baseFare).replace(",", ".").toDouble()
               else
                   return String.format("%1$.2f", fareRate * distance).replace(",", ".").toDouble()
           } else {
               return String.format("%1$.2f", fareRateAfter * distance).replace(",", ".").toDouble()
           }*/
    }


    override fun onSocketConnected() {
        Log.e("173", "onSocketConnected")
        commitListeners()
    }

    private fun commitListeners() {
        try {
            if (AppSocketListener.getInstance().isSocketConnected) {
                AppSocketListener.getInstance().off("driverBooking", onGetBookingResponseListener)
                AppSocketListener.getInstance().addOnHandler("driverBooking", onGetBookingResponseListener)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }


    private val onGetBookingResponseListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            progressDialog!!.dismiss()
            Log.e(CLASSTAG, "call: onGetBookingResponseListener   " + args[0].toString())
            try {
                val jsonObject: JSONObject = args[0] as JSONObject
                if (jsonObject.getString("success").toString().equals(AppConstants.TRUE)) {


                    val intent = Intent(getApplicationContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent);
                }
                else {
                    showAlert(jsonObject.getString("message").toString())
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
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


    private fun sendObjectToSocket(jsonObject: JSONObject, type: String) {
        try {
            AppSocketListener.getInstance().emit(type, jsonObject)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }


    private fun validate(): Boolean {

        if(et_name.text.toString().trim().length<=0) {

            showMessage(getString(R.string.nameshouldnotempty))
            return false
        }
        if(et_email.text.toString().trim().length<=0   || !Utils.isValidEmail(et_email.getText().toString().trim()))
        {
            showMessage(getString(R.string.emailshouldnotempty))

            return false
        }
        if(et_phone_number.text.toString().trim().length<8 )
        {
            showMessage(getString(R.string.phone_validation))

            return false
        }


        return true
    }
}
