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
import com.rydz.driver.CommonUtils.AppConstants.SENDEDDATA
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.model.requests.UpdateEmailRequest
import com.rydz.driver.viewModel.login.editProfile.ChangeEmailViewModel
import kotlinx.android.synthetic.main.activity_edit_email.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject

class EditEmailActivity : BaseActivity() {
    var recievedData:String=""


    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChangeEmailViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=EditEmailActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_email)
        //get data from intent
        recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)!!
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_email.setText(recievedData)
        et_email.setOnClickListener(this)
        btn_save.setOnClickListener(this)

// initialize a progress
        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangeEmailViewModel::class.java!!)
        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_save->
            {
                if (Utils.isValidEmail(et_email.getText().toString().trim()))
                {
                    if (!Constant.checkInternetConnection(this)) {
                    } else {
                        hideKeyboard(et_email)
                        val loginRequest = UpdateEmailRequest()
                        loginRequest.driverId=getUserId()
                        loginRequest.email = et_email.text.toString()
                        loginRequest.adminId=getAdminId()
                        viewModel!!.hitEmailApi(loginRequest)
                    }
                }
                else
                {
                    showAlert(getString(R.string.emailshouldnotempty))
                }
            }
            R.id.iv_back_common->
            {
                closeKeyBoard()
                finish()
            }

            R.id.et_email->
            {
                showKeyboard(et_email)
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
            Log.d(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)

                val myIntent = Intent()
                myIntent.putExtra(SENDEDDATA, et_email.getText().toString())
                setResult(RESULT_OK, myIntent)
                finish()
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }
}
