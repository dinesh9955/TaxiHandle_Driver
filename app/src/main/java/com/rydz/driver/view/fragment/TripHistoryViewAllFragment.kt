package com.rydz.driver.view.fragment

import com.rydz.driver.R
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_triphistory.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class TripHistoryViewAllFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_triphistory, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.triphistory)
        rv_trip_history.layoutManager= LinearLayoutManager(context)
//        rv_trip_history.adapter= TripHistoryAdapters(context!!)
        iv_back_common.setOnClickListener(this)
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(TripHistoryDayFragment(),AppConstants.TRIPHISTORYDAYSCREEN)
            }

        }
    }
}
