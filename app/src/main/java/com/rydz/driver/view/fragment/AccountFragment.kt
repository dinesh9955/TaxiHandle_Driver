package com.rydz.driver.view.fragment

import android.content.DialogInterface
import android.content.Intent
import android.hardware.usb.UsbDevice.getDeviceId
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.DEVICETOKEN
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.Urls
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.MenuName
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.model.requests.ChangeDriverStatusRequest
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.view.activity.EditProfileActivity
import com.rydz.driver.view.activity.LogInActivity
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.adapters.AccountItemAdapter
import com.rydz.driver.viewModel.login.editProfile.ChangeDriverStatusViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_common_toolbar_black.*
import javax.inject.Inject


class AccountFragment : BaseFragment() {
    var list: ArrayList<MenuName>? = null

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeDriverStatusViewModel? = null
    var APPTAG: String = HomeFragment::class.java.name

    //var progressDialog: ProgressDialog?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // progressDialog = Constant.getProgressDialog(context, "Please wait...")
        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeDriverStatusViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = ArrayList<MenuName>()
        list!!.add(MenuName(R.drawable.ic_doc_gray, getString(R.string.documents)))

        list!!.add(MenuName(R.drawable.ic_setting_gray, getString(R.string.settings)))
        MainActivity.mainActivity.onChangingfragment(AppConstants.ACCOUNTSCREEN)
        tv_title_common_black.text = getString(R.string.profile)
        rv_profile_item.layoutManager = LinearLayoutManager(context)
        rv_profile_item.adapter = AccountItemAdapter(context!!, list!!)

        tv_title_common_black.setTextColor(resources.getColor(R.color.white))


        iv_back_common_black.visibility = View.GONE
        tv_edit.setOnClickListener(this)
        tv_logout.setOnClickListener(this)

        if (MainActivity.mainActivity.getUserDetail().driver != null) {
            val myRespose = MainActivity.mainActivity.getUserDetail().driver
            tv_name.text = myRespose.firstName + " " + myRespose.lastName
            try {
                MainActivity.mainActivity.getImageRequest(myRespose.profilePic.toString()).into(iv_userImage)
            } catch (e: Exception) {

            }
        }

    }

    override fun onResume() {
        super.onResume()

        try
        {
            if (MainActivity.mainActivity.getUserDetail().driver != null) {
                val myRespose = MainActivity.mainActivity.getUserDetail().driver
                tv_name.text = myRespose.firstName + " " + myRespose.lastName
                try{
                    Glide.with(this).asBitmap().load(Urls.BASEIMAGEURL +myRespose.profilePic.toString()).into(iv_userImage)

                    //   MainActivity.mainActivity.getImageRequest(myRespose.profilePic.toString()).into(iv_userImage)
                }catch (e:Exception)
                {

                }
            }

        }catch (e:Exception)
        {

        }
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {


            R.id.tv_edit -> {
                startActivity(Intent(activity, EditProfileActivity::class.java))
            }

            R.id.tv_logout -> {

                handleLogout()


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
    private fun renderSuccessResponse(response: JsonElement) = if (!response.isJsonNull) {
        Log.e(APPTAG + "response=", response.toString())
        val data: String = Utils.toJson(response)
        val gson1 = Gson()
        val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
        if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
            try {
                AppSocketListener.getInstance().disconnect()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            var deviceToken =  MainActivity.mainActivity.getDeviceId()
            MainActivity.mainActivity.getMyPreferences().clearSharedPrefrences()
            MainActivity.mainActivity.getMyPreferences().setStringValue(DEVICETOKEN, deviceToken)

            val intent = Intent(context, LogInActivity::class.java)
            startActivity(intent)
            activity!!.finishAffinity()
        } else {
            showAlert(loginResponse.message.toString())
        }
    } else {
        showAlert(resources.getString(R.string.errorString))
    }


    fun handleLogout() {

        try {
            AlertDialog.Builder(activity!!)
                .setTitle("Logout")
                .setCancelable(false)
                .setMessage(getString(R.string.confirm_logout))
                .setNegativeButton(android.R.string.no, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface, arg1: Int) {
                        arg0.dismiss()

                    }
                })
                .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface, arg1: Int) {

                        val changeStausRequest = ChangeDriverStatusRequest()
                        changeStausRequest.driverId = MainActivity.mainActivity.getUserId()
                        changeStausRequest.status = "0"
                        viewModel!!.hitDriverStatusApi(changeStausRequest)


                    }
                }).create().show()

        } catch (e: Exception) {
        }


    }
}
