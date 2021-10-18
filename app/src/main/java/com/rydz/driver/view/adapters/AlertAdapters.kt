package com.rydz.driver.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.model.alert.Alert
import kotlinx.android.synthetic.main.item_alert.view.*

class AlertAdapters(var context:Context,var list: List<Alert>) : RecyclerView.Adapter<AlertAdapters.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_alert,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.tv_title_feedback.setText(list.get(p1).message+"")
        viewHolder.tv_time.setText(Utils.getDateWithtime(list.get(p1).date))

    }

    fun updateList(updatedlist: List<Alert>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var iv_alert_icon=itemView.iv_alert_icon
        var tv_title_feedback=itemView.tv_title_feedback
        var tv_time=itemView.tv_time

    }
}

