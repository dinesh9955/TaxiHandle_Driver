package com.rydz.driver.view.fragment

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
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.alert.Alert
import com.rydz.driver.model.alert.AlertRequest
import com.rydz.driver.model.alert.AlertResponse
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.adapters.AlertAdapters
import com.rydz.driver.view.adapters.PaginationScrollListener

import com.rydz.driver.viewModel.login.editProfile.AlertViewModel
import kotlinx.android.synthetic.main.fragment_alert.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.util.ArrayList
import javax.inject.Inject

class AlertFragment : BaseFragment() {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: AlertViewModel?= null
    var APPTAG:String=AlertFragment::class.java.name

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var skip:Int=0
    var list: ArrayList<Alert> = ArrayList<Alert>()
    var alertAdapters:AlertAdapters?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        initViews(view)
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.ALERTSCREEN)
        tv_title_common.text=getString(R.string.alert)
        val layoutManager: LinearLayoutManager =
            LinearLayoutManager(context)
        rv_alert.layoutManager=layoutManager
        alertAdapters= AlertAdapters(context!!,list)
        rv_alert.adapter=alertAdapters
      //  iv_back_common.setOnClickListener(this)

        iv_back_common.visibility = View.GONE

        (context!!.applicationContext as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlertViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        rv_alert?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (list.size>15) {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    skip = skip + 1
                    getMoreItems(skip)
                }
            }
        })

        getMoreItems(skip)


    }

    private fun getMoreItems(skip: Int) {
        val request=AlertRequest()
        request.driverId=MainActivity.mainActivity.getUserId()
        request.skip=skip
        viewModel!!.hitalertApi(request)
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(HomeFragment(),AppConstants.HOMESCREEN)
            }

        }
    }


    /*
  * method to handle response
  * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING ->  MainActivity.progressDialog!!.show()

            Status.SUCCESS -> {
             MainActivity.progressDialog!!.dismiss()
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                 MainActivity.progressDialog!!.dismiss()
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
            isLoading = false
            val loginResponse = gson1.fromJson(data, AlertResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                if (skip==0) {
                    list = loginResponse.alerts
                    alertAdapters!!.updateList(list)
                    if(list.size==0)
                    {
                        tv_no_data_found.visibility = View.VISIBLE
                        rv_alert.visibility = View.GONE
                    }
                    else
                    {
                        tv_no_data_found.visibility = View.GONE
                        rv_alert.visibility = View.VISIBLE
                    }
                }
                else
                {
                    list.addAll(loginResponse.alerts)
                    alertAdapters!!.updateList(list)
                    tv_no_data_found.visibility = View.GONE
                    rv_alert.visibility = View.VISIBLE

                }
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
