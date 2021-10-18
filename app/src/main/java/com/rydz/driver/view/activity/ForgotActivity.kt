package com.rydz.driver.view.activity


import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rydz.driver.R
import android.content.Intent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonElement

import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.forgot.EmailOtpRequest
import com.rydz.driver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.rydz.driver.model.requests.RegistrerCheckStatusRequest
import com.rydz.driver.viewModel.login.editProfile.EditProfileViewModel


import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_forgot.ccp
import kotlinx.android.synthetic.main.activity_forgot.et_email
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.json.JSONObject
import javax.inject.Inject

class ForgotActivity : BaseActivity() ,View.OnClickListener {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null

    var APPTAG:String=ForgotActivity::class.java.name

    var type:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        ccp.setAutoDetectedCountry(true)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        initViewS()




    }



    private fun initViewS() {
        val title:String=getString(R.string.forgot_password)
        tv_title_common.setText(title)


        rl_common.setBackgroundColor(resources.getColor(android.R.color.transparent))

        btn_reset.setOnClickListener(this)
        iv_back_common.setImageResource(R.drawable.ic_back_white)
        et_email.setOnClickListener(this)
        et_phonenumber.setOnClickListener(this)

        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length>0)
                {
                    et_phonenumber.setText("")
                }
            }


            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        et_phonenumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length>0)
                {
                    et_email.setText("")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun removeFocous() {
        justHideKeyboard(et_email)
        justHideKeyboard(et_phonenumber)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_reset->
            {

                if (et_phonenumber.text.toString().trim().isEmpty())
                {
                    if (!Utils.isValidEmail(et_email.text.toString().trim()))
                    {
                        showAlert(getString(R.string.emailshouldnotempty))
                        return
                    }
                    else
                    {
                       removeFocous()
                        type=KEY_EMAIL
                        val request=EmailOtpRequest()
                        request.adminId=getAdminId()
                        request.email=et_email.text.toString().trim()
                        viewModel!!.hitForgotEmailApi(request)
                        return
                    }
                }
                if (et_email.text.toString().isEmpty())
                {
                    if (et_phonenumber.text.toString().length<=6) {
                        showAlert(getString(R.string.enter_valid_mobile))
                        return
                    }
                    else
                    {
                        type= KEY_PHONE
                        removeFocous()
                        val checkRequest=RegistrerCheckStatusRequest()
                        checkRequest.adminId=getAdminId()
                        checkRequest.contryCode=ccp.selectedCountryCodeWithPlus.toString()
                        checkRequest.phone=et_phonenumber.text.toString().trim()
                        viewModel!!.hitCheckRegisterStatus(checkRequest)

                        return

                    }
                }

//                navigate(OtpActivity::class.java)
//                finish()
            }

            R.id.iv_back_common->
            {
                removeFocous()
                finish()
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
            if (type.toString().equals(KEY_PHONE)) {
                val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
                if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                    showAlert(getString(R.string.this_number_is_not_registered))
                } else {
                    val phonebookIntent = Intent(this@ForgotActivity, OtpActivity::class.java)
                    phonebookIntent.putExtra(AppConstants.KEY_TYPE, KEY_PHONE)
                    phonebookIntent.putExtra(AppConstants.SENDEDDATA, et_phonenumber.text.toString().trim())
                    phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                    startActivity(phonebookIntent)

                }
            }
            else if (type.toString().equals(KEY_EMAIL))
            {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.get(SUCCESS).toString().equals(AppConstants.TRUE)) {
                    val phonebookIntent = Intent(this@ForgotActivity, OtpActivity::class.java)
                    phonebookIntent.putExtra(AppConstants.KEY_TYPE,KEY_EMAIL)
                    phonebookIntent.putExtra(AppConstants.SENDEDDATA, et_email.text.toString().trim())
//                    phonebookIntent.putExtra(AppConstants.SENDCODE, ccp.selectedCountryCodeWithPlus.toString())
                    startActivity(phonebookIntent)
                } else {
                    showAlert(jsonObject.get(MESSAGE).toString())
                }
            }
        } else {
            showMessage(resources.getString(R.string.errorString))
        }
    }



}
