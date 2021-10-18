package com.rydz.driver.view.activity

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.requests.LoginRequest
import com.rydz.driver.viewModel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import javax.inject.Inject
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.R
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.model.requests.LoginWithMobileRequest

public class LogInActivity : BaseActivity() ,View.OnClickListener{

    var iswithEmail:Boolean=false

    @set:Inject
     var viewModelFactory: ViewModelFactory? = null

     var viewModel: LoginViewModel?= null

     var progressDialog: ProgressDialog?=null
    var APPTAG:String=LogInActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)



        ccp.setAutoDetectedCountry(true)

        setLoginWith()
        progressDialog = Constant.getProgressDialog(this, "Please wait...")
        (application as App).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

    }




    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_login ->
            {

                login()

            }

            R.id.tv_forgot ->
            {
                val intent=Intent(this@LogInActivity, ForgotActivity::class.java)
                startActivity(intent)
            }


            R.id.tv_login_with->
            {
                setLoginWith()
            }

            R.id.tv_register->
            {
                navigatewithFinish(RegisterActivity::class.java)
            }
        }
    }

    private fun setLoginWith() {
        if (iswithEmail)
        {
            iswithEmail=false
            ll_email.visibility=View.VISIBLE
            ll_phone.visibility=View.GONE
            tv_login_with.text=getString(R.string.loginwithphone)
            et_phone.setText("")
            et_Phone_password.setText("")
        }
        else
        {
            iswithEmail=true
            ll_email.visibility=View.GONE
            ll_phone.visibility=View.VISIBLE
            tv_login_with.text=getString(R.string.loginwithemail)
            et_email.setText("")
            et_password.setText("")
        }
    }

    private fun login() {
        Log.e("isValid : ",iswithEmail.toString()+"")
        if (isValid()) {
            if (!Constant.checkInternetConnection(this)) {
            } else {
                if (!iswithEmail) {
                    val loginRequest = LoginRequest()
                    loginRequest.adminId = getAdminId()
                    loginRequest.deviceId = getDeviceId()
                    loginRequest.deviceType = getDeviceType()
                    loginRequest.email = et_email.text.toString().trim()
                    loginRequest.password = et_password.text.toString()
                    viewModel!!.hitLoginApi(loginRequest)
                }
                else
                {
                    val loginRequest = LoginWithMobileRequest()
                    loginRequest.adminId = getAdminId()
                    loginRequest.deviceId = getDeviceId()
                    loginRequest.deviceType = getDeviceType()
                    loginRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                    loginRequest.phone = et_phone.text.toString().trim()
                    loginRequest.password = et_Phone_password.text.toString()
                    viewModel!!.hitMobileLoginApi(loginRequest)
                }
            }

        }

    }




    /*
     * method to validate $(mobile number) and $(password)
     * */
    private fun isValid(): Boolean {
        if (!iswithEmail) {
            if (!Utils.isValidEmail(et_email.getText().toString().trim())) {
                showAlert(resources.getString(R.string.emailshouldnotempty))
                return false
            } else if (et_password.getText().toString().trim().isEmpty()) {
                showAlert(resources.getString(R.string.enter_your_password))
                return false
            }
        }
        else
        {
            if (et_phone.getText().toString().trim().length<6) {
                showAlert(resources.getString(R.string.enter_valid_mobile))
                return false
            } else if (et_Phone_password.getText().toString().trim().isEmpty()) {
                showAlert(resources.getString(R.string.enter_your_password))
                return false
            }
        }

        return true
    }


    /*
     * method to handle response
     */

    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                Toast.makeText(this@LogInActivity, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
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
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(AUTH_TOKEN,loginResponse.driver.authToken.toString())
                getMyPreferences().setStringValue(USERDETAIL,data)
                navigatewithFinish(MainActivity::class.java)
            }
            else
            {
                showAlert(loginResponse.message.toString())
            }
        } else {
            Toast.makeText(this@LogInActivity, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }
}

