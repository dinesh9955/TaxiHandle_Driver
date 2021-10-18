package com.rydz.driver.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.model.userRating.RatingList
import kotlinx.android.synthetic.main.item_feedback.view.*
import java.util.ArrayList

class feedbackAdapters(
    var context: Context,
   var ratingListArrayList: ArrayList<RatingList>
) : RecyclerView.Adapter<feedbackAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_feedback,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return ratingListArrayList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.titleFeedback.setText(ratingListArrayList.get(p1).review)
        viewHolder.rating.rating= ratingListArrayList.get(p1).rating.toFloat()
        viewHolder.dateFeedback.text=Utils.getDate(ratingListArrayList.get(p1).date.toLong())

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var titleFeedback=itemView.tv_title_feedback
        var dateFeedback=itemView.tv_date_feedback
        var rating=itemView.ratingBar

    }
}

