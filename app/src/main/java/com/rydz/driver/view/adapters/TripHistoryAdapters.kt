package com.rydz.driver.view.adapters

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.model.tripHistory.BookingHistory
import com.rydz.driver.view.activity.HistoryDetailsActivity
import kotlinx.android.synthetic.main.item_triphistory.view.*

class TripHistoryAdapters(var context:Activity,var list:ArrayList<BookingHistory>) : RecyclerView.Adapter<TripHistoryAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_triphistory,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {

        if (list.get(p1) != null) {
            viewHolder.tv_amount.setText(context.getString(R.string.currencysign) + " " +  String.format("%1$.2f", list.get(p1).subtotalFare))
            viewHolder.tv_source.setText(list.get(p1).source.name)
            viewHolder.tv_destination.setText(list.get(p1).destination.name)
            if (list.get(p1).vehicleType != null && list.get(p1).vehicleType.name != null)
                viewHolder.tv_vehicle_type.setText(list.get(p1).vehicleType.name)
            viewHolder.tv_holder_date.setText(Utils.getDateWithtime(list.get(p1).date))

            if (list.get(p1).status.toString().equals("11")) {
                viewHolder.tv_cancelled.visibility = View.VISIBLE
            } else {
                viewHolder.tv_cancelled.visibility = View.GONE
            }
            viewHolder.itemView.id = p1
            viewHolder.itemView.setOnClickListener(View.OnClickListener {

                val pos: Int = it.id
                val bookingHistory: BookingHistory = list.get(pos) as BookingHistory

                val historyDetail = Intent(context, HistoryDetailsActivity::class.java)
                historyDetail.putExtra(AppConstants.SENDEDDATA, bookingHistory)
                context.startActivity(historyDetail)


            })


        }
    }
    fun updateData(updatedlist:ArrayList<BookingHistory>)
    {
        list=updatedlist
        notifyDataSetChanged()
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        var tv_holder_date=itemView.tv_date
        var tv_vehicle_type=itemView.tv_vehicle_type
        var tv_source=itemView.tv_source
        var tv_destination=itemView.tv_destination
        var tv_amount=itemView.tv_ammount
        var tv_cancelled=itemView.tv_cancelled

    }
}

