package com.rydz.driver.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rydz.driver.R
import com.rydz.driver.model.userRating.RatingList
import com.rydz.driver.view.adapters.feedbackAdapters
import kotlinx.android.synthetic.main.fragment_feedback.*
import java.util.*

class FeedbackFragment : BaseFragment() {
    companion object {
       lateinit var feedbackFragment: FeedbackFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        feedbackFragment=this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_no_data_found.visibility = View.GONE
        rv_feedback.visibility = View.VISIBLE
        rv_feedback.layoutManager = LinearLayoutManager(context)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {

        }
    }

    override fun setFeedBack(ratingListArrayList: ArrayList<RatingList>?) {
        super.setFeedBack(ratingListArrayList)
        if (ratingListArrayList != null && !ratingListArrayList.isEmpty()) {
            tv_no_data_found.visibility = View.GONE
            rv_feedback.visibility = View.VISIBLE
            rv_feedback.adapter = feedbackAdapters(context!!, ratingListArrayList)
        } else {
            tv_no_data_found.visibility = View.VISIBLE
            rv_feedback.visibility = View.GONE
        }
    }
}
