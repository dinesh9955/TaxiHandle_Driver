package com.rydz.driver.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.CommonUtils.locationMethods.LocationAddress
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.interfaces.DrawerView
import com.rydz.driver.model.socketResponse.SocketBookingResponse
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketUrls
import com.rydz.driver.view.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : BaseActivity(), DrawerView {

    public var FragmentTag: String = ""

    companion object
    {
        lateinit var mainActivity: MainActivity
        public var latitude: Double = 0.0
        public var longitude: Double = 0.0
        public var progressDialog: ProgressDialog? = null

    }

    //Handled bottom menus
    override fun onChangingfragment(type: String) {
        val grayColor: Int = ContextCompat.getColor(this@MainActivity, R.color.edittextline)
        val blackColor: Int = ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)
        if (type.toString().equals(RATINGSCREEN)) {
            iv_rating.setImageResource(R.drawable.ic_star_black_filled)
            tv_rating.setTextColor(blackColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_menu.visibility = View.VISIBLE


        } else if (type.toString().equals(EARNINGSCREEN)) {

            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_black_filled)
            tv_earning.setTextColor(blackColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_menu.visibility = View.VISIBLE

        } else if (type.toString().equals(ALERTSCREEN)) {

            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_black)
            tv_alert.setTextColor(blackColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_menu.visibility = View.VISIBLE

        } else if (type.toString().equals(HOMESCREEN)) {
            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_connected)
            ll_menu.visibility = View.VISIBLE
        } else if (type.toString().equals(ACCOUNTSCREEN)) {
            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_black_circle)
            tv_account.setTextColor(blackColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_menu.visibility = View.VISIBLE

        } else if (type.toString().equals(NOMAINSCREEN)) {
            iv_rating.setImageResource(R.drawable.ic_star_gray)
            tv_rating.setTextColor(grayColor)
            iv_earning.setImageResource(R.drawable.ic_earning_gray)
            tv_earning.setTextColor(grayColor)
            iv_alerts.setImageResource(R.drawable.ic_notification_gray)
            tv_alert.setTextColor(grayColor)
            iv_account.setImageResource(R.drawable.ic_user_icon_with_gray_circle)
            tv_account.setTextColor(grayColor)
            rl_home.setBackgroundResource(R.drawable.ic_seat_belt_not_connected)
            ll_menu.visibility = View.GONE
        } else if (type.toString().equals(ACCEPTSCREEN)) {
            ll_menu.visibility = View.GONE
        } else if (type.toString().equals(PAYSCREEN)) {
            ll_menu.visibility = View.GONE
        } else if (type.toString().equals(CHATSCREEN)) {
            ll_menu.visibility = View.GONE
        }
    }

    private var frame: FrameLayout? = null

    private var mFragmentManager: FragmentManager? = null

    override fun onReplaceFragment(fragment: Fragment, tag: String) {
        replaceFragment(fragment, tag)
    }

    override fun OpenDrawer() {

    }

    // To exit from App
    fun OnExit() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.exit))
            .setMessage(getString(R.string.areyousure))
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                override fun onClick(arg0: DialogInterface, arg1: Int) {
                    try {
                        AppSocketListener.getInstance().disconnect()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    finishAffinity()
                }
            }).create().show()
    }



    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }


  /*  // Change language if localization is changed
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        try {
            Utils(this@MainActivity).changeLanguage(getSelectedLanguage())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }*/

    //Handled on click method
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.ll_rating -> {
                if (!FragmentTag.equals(RATINGSCREEN)) {
                    onReplaceFragment(RatingFragment(), AppConstants.RATINGSCREEN)
                }

            }
            R.id.ll_earning -> {
                if (!FragmentTag.equals(EARNINGSCREEN)) {
                    onReplaceFragment(EarningFragment(), AppConstants.EARNINGSCREEN)
                }
            }

            R.id.ll_alert -> {
                if (!FragmentTag.equals(ALERTSCREEN)) {
                    onReplaceFragment(AlertFragment(), AppConstants.ALERTSCREEN)
                }
            }

            R.id.ll_account -> {
                if (!FragmentTag.equals(ACCOUNTSCREEN)) {
                    onReplaceFragment(AccountFragment(), AppConstants.ACCOUNTSCREEN)
                }

            }

            R.id.rl_home -> {

                if (!FragmentTag.equals(HOMESCREEN)) {

                    onReplaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                }

            }
        }
    }


    // On create method to hold a layout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = Constant.getProgressDialog(this@MainActivity, "Please wait...")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectCleartextNetwork()
                    .penaltyLog()
                    .build()
            )
        };
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        frame = findViewById<View>(R.id.main_container) as FrameLayout
        mFragmentManager = supportFragmentManager
        mainActivity = this
        addFragment(HomeFragment(), AppConstants.HOMESCREEN)


        //to handle push notificatios flow
        try {
            Log.e("intent : ", "null")
            if (intent != null) {
                Log.e("intent : ", "not null")
                if (intent.getStringExtra("fromNotification") != null && !intent.getStringExtra("fromNotification").equals(
                        "54"
                    )
                ) {

                    Handler().postDelayed({
                        sendRequesttoFragment(intent?.getStringExtra("fromNotification")!!)
                    }, 500)

                } else if (intent.getStringExtra("fromNotification") != null && intent.getStringExtra("fromNotification").equals(
                        "54"
                    )
                ) {
                    handleDisableAccount()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()

        }


        try { //Subscribe a topic for notification
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }


    // Handled on back pressed method according to screens using tag
    override fun onBackPressed() {

        if (!FragmentTag.isEmpty()) {

            if (FragmentTag.equals(AppConstants.HOMESCREEN)) {
                OnExit()
                return
            } else if (FragmentTag.equals(AppConstants.POOLSCREEN)) {
                OnExit()
                return
            } else if (FragmentTag.equals(AppConstants.EDITPROFILESCREEN)) {
                replaceFragment(AccountFragment(), AppConstants.ACCOUNTSCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.ACCOUNTSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.DOCUMENTSCREEN)) {
                replaceFragment(AccountFragment(), AppConstants.ACCOUNTSCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.SETTINGFRAGMENT)) {
                replaceFragment(AccountFragment(), AppConstants.ACCOUNTSCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.HISTORYDETAILSCREEN)) {
                replaceFragment(EarningFragment(), AppConstants.EARNINGSCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.TRIPHISTORYDAYSCREEN)) {
                replaceFragment(EarningFragment(), AppConstants.EARNINGSCREEN)
                return
            } /*else if (FragmentTag.equals(AppConstants.TRIPHISTORYVIEWALLSCREEN)) {
                replaceFragment(TripHistoryDayFragment(), AppConstants.TRIPHISTORYDAYSCREEN)
                return
            }*/ else if (FragmentTag.equals(AppConstants.CHATSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.ALERTSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.EARNINGSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.ACCOUNTSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.ALERTSCREEN)) {
                replaceFragment(HomeFragment(), AppConstants.HOMESCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.TRIPHISTORY)) {
                replaceFragment(EarningFragment(), AppConstants.EARNINGSCREEN)
                return
            } else if (FragmentTag.equals(AppConstants.NOTBACKPRESS)) {

                return
            } else if (FragmentTag.equals(AppConstants.ACCEPTSCREEN)) {

                return
            } else {
               super.onBackPressed()
                return
            }

        } else {
            OnExit()
        }
    }

    // Forcefully hide a keyboard
    fun forceHideKeyboard(context: Context) {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(context)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }



    // to replace a fragment
    fun replaceFragment(fragment: Fragment, tag: String) {
        FragmentTag = tag
        val transaction = mFragmentManager?.beginTransaction()
        transaction!!.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )

        transaction.replace(R.id.main_container, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }




    // Not in Use
    fun startAThread(lat: Double, lng: Double) {
        runOnUiThread(object : Runnable {
            override fun run() {
                val locationAddress = LocationAddress()
                locationAddress.getAddressFromLocation(
                    lat, lng,
                    this@MainActivity, GeocoderHandler()
                )
            }
        })
    }


    private inner class GeocoderHandler() : Handler() {
        override fun handleMessage(message: Message) {
            val locationAddress: String?
            when (message.what) {
                1 -> {
                    val bundle = message.getData()
                    locationAddress = bundle.getString("address")
                }
                else -> locationAddress = null
            }
        }
    }


    //add a fragment
    fun addFragment(fragment: Fragment, tag: String) {
        if (mFragmentManager != null) {
            FragmentTag = tag
            val transaction = (mFragmentManager as FragmentManager).beginTransaction()
            transaction.add(R.id.main_container, fragment)
            transaction.commitAllowingStateLoss()
        }
    }

    //got Accept fragment if click on notification request
    fun sendRequesttoFragment(string: String) {

        if(progressDialog!=null)
            progressDialog!!.dismiss()

        try {

            val gson1 = Gson();
            val loginResponse = gson1.fromJson(string, SocketBookingResponse::class.java)
            val moneyTransferFragment = AcceptFragment()
            val b = Bundle()
            b.putParcelable(SENDEDDATA, loginResponse)
            moneyTransferFragment.setArguments(b)
            replaceFragment(moneyTransferFragment, AppConstants.ACCEPTSCREEN)
        }

        catch (ex: Exception) {

            ex.printStackTrace()

        }


    }


    //got Accept fragment if click on notification request
    public fun handleDisableAccount() {

        try {

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Exit")
                .setCancelable(false)
                .setMessage(getString(R.string.disable_login))
                .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface, arg1: Int) {
                        MainActivity.mainActivity.getMyPreferences().clearSharedPrefrences()
                        navigate(LogInActivity::class.java)
                        finishAffinity()

                    }
                }).create().show()

        } catch (e: Exception) {
        }


    }
    //emit socket with parameters
    public fun sendObjectToSocket() {
        try {

            val params = JSONObject()
            params.put(DRIVER_ID, MainActivity.mainActivity.getUserId())
            params.put(BOOKING_ID, "")
            params.put("authToken", MainActivity.mainActivity.getAuthToken())
            Log.e("authToken", params.toString())
            if (AppSocketListener.getInstance().isSocketConnected) {
                AppSocketListener.getInstance().emit(SocketUrls.DRIVER_RIDE_STATUS, params)
            } else {
                AppSocketListener.getInstance().connect()
                AppSocketListener.getInstance().emit(SocketUrls.DRIVER_RIDE_STATUS, params)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

}
