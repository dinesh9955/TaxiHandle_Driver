package com.rydz.driver.view.fragment

import android.app.Dialog
import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.document.DriverDocumentResponse
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.adapters.DocumentAdapters
import com.rydz.driver.viewModel.login.GetDocumentViewModel
import kotlinx.android.synthetic.main.fragment_document.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import javax.inject.Inject

class DocumentFragment : BaseFragment() {


    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: GetDocumentViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String= DocumentFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_document, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.documents)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        rv_document.layoutManager= LinearLayoutManager(context)
        tv_no_data_found.visibility=View.VISIBLE
        rv_document.visibility=View.GONE

        iv_back_common.setOnClickListener(this)

        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)



        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GetDocumentViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel!!.hitGetDocumentApi(MainActivity.mainActivity.getUserId());
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
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
            var loginResponse = gson1.fromJson(data, DriverDocumentResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {

                rv_document.visibility=View.VISIBLE
                tv_no_data_found.visibility=View.GONE
                rv_document.adapter= DocumentAdapters(activity!!,loginResponse.docList,this)

            }
            else
            {
                showAlert(loginResponse.message.toString())
                rv_document.visibility=View.GONE
                tv_no_data_found.visibility=View.VISIBLE
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }



}
