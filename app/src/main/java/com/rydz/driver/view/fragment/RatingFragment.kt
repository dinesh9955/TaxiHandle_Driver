package com.rydz.driver.view.fragment

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rydz.driver.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.userRating.RatingList
import com.rydz.driver.model.userRating.UserRatingResponse
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.fragment.FeedbackFragment.Companion.feedbackFragment
import com.rydz.driver.viewModel.login.editProfile.UserRatingViewModel
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.util.ArrayList
import javax.inject.Inject

class RatingFragment : BaseFragment()/*, RatingBar.OnRatingBarChangeListener*/ {

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: UserRatingViewModel?= null
    var progressDialog: ProgressDialog?=null
    var APPTAG:String=RatingFragment::class.java.name

    companion object {
         var ratingList: List<RatingList> = ArrayList<RatingList>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingList = ArrayList<RatingList>()
        MainActivity.mainActivity.onChangingfragment(AppConstants.RATINGSCREEN)
        setupViewPager(viewpager!!)
        tabs!!.setupWithViewPager(viewpager)
        viewpager.beginFakeDrag();
        tv_title_common.text=getString(R.string.ratings)
        iv_back_common.visibility = View.GONE
        progressDialog = Constant.getProgressDialog(context, "Please wait...")

        (context!!.applicationContext as App).getAppComponent().doInjection(this)
        
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserRatingViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel!!.hitUserRatingApi(MainActivity.mainActivity.getUserId())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupViewPager(viewpager!!)
        tabs!!.setupWithViewPager(viewpager)

    }






    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager!!)
        adapter.addFragment(FeedbackFragment(), getString(R.string.feedback))
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
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
            val loginResponse = gson1.fromJson(data, UserRatingResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {

                ratingList=loginResponse.ratingList
                tv_rating.setText(loginResponse.totalRating.toString())
                tv_rating_value.setText(loginResponse.totalRating.toString())
                tv_accepted_value.setText(loginResponse.accepted.toString()+"%")
                tv_cancelled_value.setText(loginResponse.canceled.toString()+"%")
                ratingBar.rating= loginResponse.totalRating.toFloat()

                feedbackFragment.setFeedBack(ratingList as ArrayList<RatingList>?)

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
