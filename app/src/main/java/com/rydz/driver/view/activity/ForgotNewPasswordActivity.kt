package com.rydz.driver.view.activity

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.KEY_EMAIL
import com.rydz.driver.CommonUtils.AppConstants.KEY_PHONE
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.forgot.ForgotPasswordRequest
import com.rydz.driver.viewModel.login.editProfile.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_new_password.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.json.JSONObject

import javax.inject.Inject

class ForgotNewPasswordActivity : BaseActivity() {

    var recievedData:String=""
    var recievedCode:String=""
    private var type:String=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangePasswordViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=ForgotNewPasswordActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        btn_save.setOnClickListener(this)
        tv_title_common.setText(getString(R.string.new_password))
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_new_password.setOnClickListener(this)
        et_confirm_password.setOnClickListener(this)

        if (intent!=null)
        {
            type=intent.getStringExtra(AppConstants.KEY_TYPE)!!
            if (type.toString().equals(KEY_EMAIL))
            {
                recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)!!

            }
            else if (type.toString().equals(KEY_PHONE))
            {
                recievedData=intent?.getStringExtra(AppConstants.SENDEDDATA)!!
                recievedCode=intent.getStringExtra(AppConstants.SENDCODE)!!
            }
        }

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePasswordViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

    }


    override fun onClick(v: View?) {
        super.onClick(v)

        when(v!!.id)
        {
            R.id.iv_back_common->
            {
               navigatewithFinish(LogInActivity::class.java)
            }
            R.id.btn_save->
            {
                changePassword()
            }
            R.id.et_new_password->
            {
                showKeyboard(et_new_password)
            }
            R.id.et_confirm_password->
            {
                showKeyboard(et_confirm_password)
            }
        }
    }

    private fun changePassword() {
         if (et_new_password.text.length<6)
        {
            et_new_password.requestFocus()
            showAlert(getString(R.string.passwordshouldnotempty))
        }
        else if (!et_confirm_password.text.toString().equals(et_new_password.text.toString()))
        {
            et_confirm_password.requestFocus()
            showAlert(getString(R.string.confirmpasswordshouldmatch))
        }
        else
        {
            if (!Constant.checkInternetConnection(this)) {
            } else {
                val loginRequest = ForgotPasswordRequest()
                loginRequest.adminId=getAdminId()
                if (type.toString().equals(KEY_PHONE))
                {
                    loginRequest.contryCode=recievedCode
                    loginRequest.phone=recievedData
                }
                else if (type.toString().equals(KEY_EMAIL))
                {
                    loginRequest.email=recievedData
                }
                loginRequest.newPassword = et_new_password.text.toString()
                viewModel!!.hitForgotPhoneApi(loginRequest)
            }
        }

    }


    /*
 * method to handle response
 * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                showAlert( resources.getString(R.string.errorString))
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
            Log.e(APPTAG+"response=", response.toString())
            val loginResponse = JSONObject(response.toString())
            if (loginResponse.get(AppConstants.SUCCESS).toString().equals(AppConstants.TRUE))
            {
                val phonebookIntent = Intent(this@ForgotNewPasswordActivity, LogInActivity::class.java)
                startActivity(phonebookIntent)
            }
            else
            {
                showAlert(loginResponse.get(AppConstants.MESSAGE).toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        navigatewithFinish(LogInActivity::class.java)
    }
}
