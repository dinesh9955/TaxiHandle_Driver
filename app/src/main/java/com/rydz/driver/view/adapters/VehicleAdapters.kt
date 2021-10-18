package com.rydz.driver.view.adapters

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.CustomTextUtils.CustomEditText
import com.rydz.driver.R
import com.rydz.driver.model.vehicle_type.Vehicletype
import com.rydz.driver.view.activity.RegisterDetailActivity2
import kotlinx.android.synthetic.main.item_vehicle_type_text.view.*

class VehicleAdapters(
    var context: Context,
    var vehicletype: ArrayList<Vehicletype>,
    var et_vehicle_type: CustomEditText,
    var vehicle_id: RegisterDetailActivity2?,
    var dialog: Dialog
) : RecyclerView.Adapter<VehicleAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
       val myView=LayoutInflater.from(context).inflate(R.layout.item_vehicle_type_text,p0,false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
       return vehicletype.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.tv_vehicle.setText(vehicletype.get(p1).name)
        viewHolder.itemView.id=p1
        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            val id:Int=it.id
            vehicle_id!!.changeValue(vehicletype.get(id).id.toString())
            et_vehicle_type.setText(vehicletype.get(id).name)
            dialog.dismiss()
        })

    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var tv_vehicle=itemView.tv_vehicleType

    }
}

