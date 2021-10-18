package com.rydz.driver.view.activity


import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rydz.driver.R
import android.content.Intent

import android.os.Bundle
import android.util.Log

import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement

import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.rydz.driver.model.requests.RegistrerCheckStatusRequest
import com.rydz.driver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.ccp
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject


class RegisterActivity : BaseActivity()  ,View.OnClickListener{
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null

    var APPTAG:String=RegisterActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ccp.setAutoDetectedCountry(true)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })


        initViewS()
    }


    private fun initViewS() {

        val title:String=getString(R.string.register)
        tv_title_common.setText(title)

        iv_back_common.setImageResource(R.drawable.ic_back_white)
        rl_common.setBackgroundColor(resources.getColor(android.R.color.transparent))


    }



    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {
                if (et_phonenumber.text.toString().length<=6) {
                    showMessage(getString(R.string.enter_valid_mobile))
                }
                else
                {

                    val registerStatusRequest=RegistrerCheckStatusRequest()
                    registerStatusRequest.adminId=getAdminId()
                    registerStatusRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                    registerStatusRequest.phone=et_phonenumber.text.toString()
                    viewModel!!.hitCheckRegisterStatus(registerStatusRequest)

                }
            }

            R.id.iv_back_common->
            {

               navigatewithFinish(LogInActivity::class.java)
            }



            R.id.tv_login->
            {
                navigatewithFinish(LogInActivity::class.java)
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
                showMessage( resources.getString(R.string.errorString))
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
            val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                val phonebookIntent = Intent(this@RegisterActivity, OtpRegisterActivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,et_phonenumber.text.toString().trim())
                phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                startActivity(phonebookIntent)
            }
            else
            {
                showMessage(loginResponse.message.toString())
            }
        } else {
            showMessage(resources.getString(R.string.errorString))
        }
    }



}
