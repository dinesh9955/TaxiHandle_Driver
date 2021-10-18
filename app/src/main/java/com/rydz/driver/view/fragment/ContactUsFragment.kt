package com.rydz.driver.view.fragment

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rydz.driver.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.MESSAGE
import com.rydz.driver.CommonUtils.AppConstants.SUCCESS
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.requests.SupportRequest
import com.rydz.driver.view.activity.MainActivity

import com.rydz.driver.viewModel.login.editProfile.SupportViewModel
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import org.json.JSONObject
import javax.inject.Inject

class ContactUsFragment : BaseFragment() {
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: SupportViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=ContactUsFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_us, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.contact_us)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        et_title.setOnClickListener(this)
        et_description.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SupportViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        MainActivity.mainActivity.forceHideKeyboard(context!!)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.hideKeyboard(et_title)
                MainActivity.mainActivity.hideKeyboard(et_description)
                MainActivity.mainActivity.onReplaceFragment(SettingFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.btn_submit->
            {
                if (et_title.text.toString().trim().isEmpty())
                {
                    showAlert(getString(R.string.please_enter_your_title))
                    MainActivity.mainActivity.showKeyboard(et_title)
                }
                else if (et_description.text.toString().isEmpty())
                {
                    showAlert(getString(R.string.please_enter_your_description))
                    MainActivity.mainActivity.showKeyboard(et_description)
                }
                else
                {
                   MainActivity.mainActivity.hideKeyboard(et_title)
                    MainActivity.mainActivity.hideKeyboard(et_description)
                    val supportRequest=SupportRequest()
                    supportRequest.comment=et_description.text.toString()
                    supportRequest.subject=et_title.text.toString()
                    supportRequest.type="2"
                    supportRequest.userId=MainActivity.mainActivity.getUserId()
                    viewModel!!.hitSupportApi(supportRequest)
                }
            }

            R.id.et_title->
            {
                MainActivity.mainActivity.showKeyboard(et_title)
            }

            R.id.et_description->
            {
                MainActivity.mainActivity.showKeyboard(et_description)
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


            val jsonObject=JSONObject(response.toString())
            if (jsonObject.get(SUCCESS).toString().equals(AppConstants.TRUE))
            {
                showAlert(jsonObject.get(MESSAGE).toString())
                et_description.setText("")
                et_title.setText("")
            }

        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }
}
