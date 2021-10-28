package com.rydz.driver.view.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.Urls
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.requests.UserRatingRequest
import com.rydz.driver.model.socketResponse.Booking
import com.rydz.driver.model.socketResponse.DriverRideStatusResponse
import com.rydz.driver.model.userRating.UserRatingResponse
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls
import com.rydz.driver.socket.SocketUrls.DRIVER_CHANGE_STATUS
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.viewModel.login.editProfile.UserRatingViewModel
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_amount_paid.*
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.onClick
import org.json.JSONObject
import javax.inject.Inject

class PayFragment : BaseFragment(), SocketListener {

    val amount: String = "30.0"
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: UserRatingViewModel? = null
    var APPTAG: String = PayFragment::class.java.name
    private val CLASSTAG: String = PayFragment::class.java.simpleName
    var driverRideStatus: Booking? = null
    private var animShow: Animation? = null
    private var animHide: Animation? = null
    private var bookingId: String = ""
    private var user_id: String = ""
    private var returnValue = 1

    companion object {
       lateinit var paymentFragment: PayFragment
       var TAG = "PayFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_amount_paid, container, false)
        //MainActivity.mainActivity.FragmentTag=PAYSCREEN

            MainActivity.mainActivity.onChangingfragment(PAYSCREEN)
        AppSocketListener.getInstance().setActiveSocketListener(this)
        AppSocketListener.getInstance().restartSocket()
        paymentFragment = this

        Log.e(TAG, "PayFrmagcc")


        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserRatingViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        if (arguments != null) {
            driverRideStatus = arguments!!.getSerializable(AppConstants.SENDEDDATA) as Booking

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show)
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide)
        btn_paid.setOnClickListener(this)
        btn_rating.setOnClickListener(this)
        btn_skip.setOnClickListener(this)
        btn_cancelDelivery.setOnClickListener(this)

/*
        if (driverRideStatus!=null) {
            try {
                bookingId=driverRideStatus!!.id
                if(driverRideStatus!!.userId!=null) {
                    user_id = driverRideStatus!!.userId.id
                }
                else
                {
                    user_id=""
                }

                if(driverRideStatus!!.bookingType.equals("1"))
                {
                    btn_cancelDelivery.visibility=View.VISIBLE
                }
                else
                    btn_cancelDelivery.visibility=View.GONE

                tv_paid_amount.setText(getString(R.string.currencysign) + " " + driverRideStatus!!.fare)
                Log.e("FARE : ",driverRideStatus!!.fare.toString())
            }catch (e:Exception)
            {

                Log.e("104",e.printStackTrace().toString())
                e.printStackTrace()
            }

        }
*/

        if (driverRideStatus != null) {
            try {
                bookingId = driverRideStatus!!.id
                if (driverRideStatus!!.userId != null) {
                    user_id = driverRideStatus!!.userId.id
//                    tv_paid_amount.text = getString(R.string.currencysign) + " " + String.format(
//                        "%1$.2f",
//                        driverRideStatus!!.fare
//                    ).replace(",", ".").toDouble()
                    tv_paid_amount.text = getString(R.string.currencysign) + " " + String.format("%1$.2f", driverRideStatus!!.subtotalFare).replace(",", ".").toDouble()
                } else {
                    user_id = ""
                    if (driverRideStatus!!.bookingType.equals("1")) {
//                        tv_paid_amount.text = getString(R.string.currencysign) + " " + String.format("%1$.2f", driverRideStatus!!.itemAmount).replace(",", ".").toDouble()
                        tv_paid_amount.text = getString(R.string.currencysign) + " " + String.format("%1$.2f", driverRideStatus!!.subtotalFare).replace(",", ".").toDouble()
                    }
                    else
                    {
                        tv_paid_amount.text = getString(R.string.currencysign) + " " + String.format("%1$.2f", driverRideStatus!!.subtotalFare).replace(",", ".").toDouble()

                    }
                }



                if(driverRideStatus?.paymentMode.equals("card",true)) {
                   // btn_paid.visibility=View.GONE

                    btn_paid.visibility = View.GONE
                    btn_paid.startAnimation(animHide)
                    tv_has_been_paid.visibility = View.VISIBLE
                    tv_has_been_paid.startAnimation(animShow)
                    ll_rating.visibility = View.VISIBLE
                    ll_rating.startAnimation(animShow)

                }
                else
                    btn_paid.visibility=View.VISIBLE
                if (driverRideStatus!!.bookingType.equals("1")) {
                    btn_cancelDelivery.visibility = View.VISIBLE
                } else
                    btn_cancelDelivery.visibility = View.GONE



            } catch (e: Exception) {


                e.printStackTrace()
            }

        }


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_rating -> {
                if (ratingBar_amount_paid != null && ratingBar_amount_paid.rating < 1) {
                    showAlert(getString(R.string.minimumrating))
                } else {
                    try {
                        if (driverRideStatus != null) {
                            val userrequest = UserRatingRequest()
                            userrequest.bookingId = bookingId
                            userrequest.driverId = MainActivity.mainActivity.getUserId()
                            userrequest.rating = ratingBar_amount_paid.rating.toString()
                            userrequest.review = et_comment.text.toString() + ""
                            userrequest.userId = user_id
                            viewModel!!.hittoUserRating(userrequest)
                            MainActivity.mainActivity.forceHideKeyboard(context!!)
                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            R.id.btn_paid -> {
                returnValue = 1
                hitPaymentStatus()
/*
                try {
                    btn_paid.visibility=View.GONE
                    val jsonObject= JSONObject()
                    jsonObject.put(AppConstants.STATUS_SOCKET,"4")
                    jsonObject.put(BOOKING_ID,bookingId)
                     jsonObject.put(RETURN,returnValue)
                    sendObjectToSocket(jsonObject,DRIVER_CHANGE_STATUS)
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }
*/

            }
            R.id.btn_cancelDelivery -> {

                reportDeliveryIssue()
/*
                try {
                    btn_paid.visibility=View.GONE
                    val jsonObject= JSONObject()
                    jsonObject.put(AppConstants.STATUS_SOCKET,"4")
                    jsonObject.put(BOOKING_ID,bookingId)
                    jsonObject.put(RETURN,0)

                    sendObjectToSocket(jsonObject,DRIVER_CHANGE_STATUS)
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }
*/

            }

            R.id.btn_skip -> {
                try {
                    MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                    MainActivity.mainActivity.forceHideKeyboard(context!!)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }


            }

        }
    }

    override fun onResume() {
        super.onResume()
        commitListeners()

    }

    private fun commitListeners() {
        if (AppSocketListener.getInstance().isSocketConnected) {
            AppSocketListener.getInstance().off("driverChangeStatus", onDriverChangeRideStatusListener)
            AppSocketListener.getInstance().addOnHandler("driverChangeStatus", onDriverChangeRideStatusListener)

            AppSocketListener.getInstance().off("driverRideStatus", onGetDriveRideStatusListener)
            AppSocketListener.getInstance().addOnHandler("driverRideStatus", onGetDriveRideStatusListener)


            AppSocketListener.getInstance().off("customerPaymentDoneOK", onCustomerPaymentDoneListener)
            AppSocketListener.getInstance().addOnHandler("customerPaymentDoneOK", onCustomerPaymentDoneListener)

            try {
                val params = JSONObject()
                params.put(DRIVER_ID, "_"+MainActivity.mainActivity.getUserId())
                sendObjectToSocket(params, SocketUrls.ONLINEDRIVER)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }

    /*
 * method to handle response
 * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> {
                MainActivity.progressDialog!!.show()
            }

            Status.SUCCESS -> {
                MainActivity.progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                MainActivity.progressDialog!!.dismiss()
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
            Log.e(APPTAG + "response=", response.toString())
            val data: String = Utils.toJson(response)
            val gson1 = Gson()
            val loginResponse = gson1.fromJson(data, UserRatingResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                MainActivity.mainActivity.replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
            } else {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    fun onPaidAmmount(status: Int) {
        try {
            activity!!.runOnUiThread(object : Runnable {
                override fun run() {
                    // change UI elements here

                    if (driverRideStatus != null && driverRideStatus!!.userId != null) {
                        if (status.toString().equals("4")) {
                            btn_paid.visibility = View.GONE
                            btn_paid.startAnimation(animHide)
                            tv_has_been_paid.visibility = View.VISIBLE
                            tv_has_been_paid.startAnimation(animShow)
                            ll_rating.visibility = View.VISIBLE
                            ll_rating.startAnimation(animShow)
                        }
                    } else {
                        try {
                            MainActivity.mainActivity.replaceFragment(HomeFragment(), HOMESCREEN)
                            MainActivity.mainActivity.forceHideKeyboard(context!!)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    fun sendObjectToSocket(jsonObject: JSONObject, type: String) {
        try {
            AppSocketListener.getInstance().emit(type, jsonObject)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

    private val onDriverChangeRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onDriverChangeRideStatusListener   " + args[0].toString())
            try {
                val gson1 = Gson()
                val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                if (!loginResponse.status.toString().isEmpty()) {

                    MainActivity.mainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            // change UI elements here
                            if (loginResponse.success.equals("true") && loginResponse.status.toString().equals("4")) {
                                onPaidAmmount(loginResponse.status.toInt())
                            }
                            else {
                                onPaidAmmount(loginResponse.status.toInt())
                            }
//                            onChangeStatus(loginResponse)
                        }
                    })
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }

    private val onGetDriveRideStatusListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            try {
                Log.e(CLASSTAG, "call: onGetDriveRideStatusListener   " + args[0].toString())
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (loginResponse.success.toString().equals(TRUE)) {
                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {

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
    private val onCustomerPaymentDoneListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            try {
                Log.e(CLASSTAG, "call: onCustomerPaymentDoneListener   " + args[0].toString())

                    btn_paid.visibility = View.GONE
                    btn_paid.startAnimation(animHide)
                    tv_has_been_paid.visibility = View.VISIBLE
                    tv_has_been_paid.startAnimation(animShow)
                    ll_rating.visibility = View.VISIBLE
                    ll_rating.startAnimation(animShow)





                /*
                try {
                    val gson1 = Gson()
                    val loginResponse = gson1.fromJson(args[0].toString(), DriverRideStatusResponse::class.java)
                    if (loginResponse.success.toString().equals(TRUE)) {
                        MainActivity.mainActivity.runOnUiThread(object : Runnable {
                            override fun run() {

                                checkDriverStatus(loginResponse)
                            }
                        })

                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }*/
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

    }
    @SuppressLint("MissingPermission")
    fun checkDriverStatus(loginResponse: DriverRideStatusResponse) {

         var driverStatus: Int = 9
        try {


            try {


                if (loginResponse != null && loginResponse.isDisabled == 0) {

                    (activity as MainActivity).handleDisableAccount()
                }
                if (loginResponse.booking != null && loginResponse.booking.size > 0) {

                    driverStatus = loginResponse.booking.get(0).status.toInt()
                    if (loginResponse.booking.get(loginResponse.booking.size - 1).bookingType.equals("2") && loginResponse.booking.get(
                            loginResponse.booking.size - 1
                        ).vehicleType.equals(Urls.POOLINGTYPE)
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


            when (driverStatus) {

                14 -> {
                    Log.e("HomeFragment","14")

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

    override fun onSocketConnected() {
        Log.e(CLASSTAG, "onSocketConnected")
        commitListeners()
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


    fun reportDeliveryIssue() {

        returnValue=1
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


        cb_returnedpackage.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_notAvailable.isChecked = false
                cb_paymentNotDone.isChecked = false
                returnValue = 2
            }

        }

        cb_notAvailable.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_returnedpackage.isChecked = false
                cb_paymentNotDone.isChecked = false
                returnValue = 0
            }

        }


        cb_paymentNotDone.onCheckedChange { compoundButton, b ->
            if (b) {
                cb_notAvailable.isChecked = false
                cb_returnedpackage.isChecked = false
                returnValue = 3

            }

        }

        tv_submit.onClick {
            if (returnValue == 1) {
                Toast.makeText(activity, "Please select an option", Toast.LENGTH_SHORT).show()


            } else
            {
                dialog.dismiss()
                hitPaymentStatus()
            }
        }



        dialog.show()


    }

    fun hitPaymentStatus() {
        try {
            btn_paid.visibility = View.GONE
            btn_cancelDelivery.visibility=View.GONE
            val jsonObject = JSONObject()
            jsonObject.put(AppConstants.STATUS_SOCKET, "4")
            jsonObject.put(BOOKING_ID, bookingId)
            jsonObject.put(RETURN, returnValue)
            sendObjectToSocket(jsonObject, DRIVER_CHANGE_STATUS)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}
