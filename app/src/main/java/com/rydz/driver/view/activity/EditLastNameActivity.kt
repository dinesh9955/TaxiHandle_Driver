package com.rydz.driver.view.activity

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.rydz.driver.model.requests.EditProfilerequest
import com.rydz.driver.viewModel.login.editProfile.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_last_name.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject


class EditLastNameActivity : BaseActivity() {
    var recievedData:String=""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: EditProfileViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=EditLastNameActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_last_name)
        recievedData=intent.getStringExtra(AppConstants.SENDEDDATA)!!
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        et_last_name.setText(recievedData)
        et_last_name.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_save->
            {
                if (!et_last_name.text.toString().isNullOrEmpty())
                {
                    if (!Constant.checkInternetConnection(this)) {
                    } else {
                        val loginRequest = EditProfilerequest()
                        loginRequest.driverId=getUserId()
                        loginRequest.lastName = et_last_name.text.toString()
                        viewModel!!.hitEditProfileApi(loginRequest)
                    }
                }
                else
                {
                    showAlert(getString(R.string.nameshouldnotempty))
                }
            }

            R.id.iv_back_common->
            {
                closeKeyBoard()
                finish()
            }

            R.id.et_last_name->
            {
                et_last_name.isFocusable=true
                et_last_name.isFocusableInTouchMode=true
                et_last_name.requestFocus()
                MainActivity.mainActivity.showKeyboard(et_last_name)
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
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString()+"")
                getMyPreferences().setStringValue(AppConstants.USER_ID,loginResponse.driver.id.toString())
                getMyPreferences().setStringValue(AppConstants.USERDETAIL,data)

                val myIntent = Intent()
                myIntent.putExtra(SENDEDDATA, et_last_name.getText().toString())
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
