package com.rydz.driver.view.adapters


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.rydz.driver.CommonUtils.PlacesEventObject
import com.rydz.driver.CommonUtils.locationMethods.OnLocationItemClickListener
import com.rydz.driver.R
import com.rydz.driver.model.socketResponse.Source
import com.rydz.driver.view.activity.LocationSetActivity
import kotlinx.android.synthetic.main.location_rowitem.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*


class LocationBottomsheetAdapter(val placesClient: PlacesClient, val context: Context, val placesList: List<AutocompletePrediction>, val onItemClickListener: OnLocationItemClickListener, val placeType: String) : RecyclerView.Adapter<LocationBottomsheetAdapter.ViewHolder>() {


    lateinit var mOnItemClickListener: OnLocationItemClickListener
    var srcObject: Source? = null
    var desObject: Source? = null

    init {
        mOnItemClickListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: LocationBottomsheetAdapter.ViewHolder, position: Int) {

        holder?.tv_locname?.text = placesList.get(position).getPrimaryText(null).toString()
        holder?.tv_locAddress?.text = placesList.get(position).getSecondaryText(null).toString()
        var place: com.google.android.libraries.places.api.model.Place? = null

        holder?.rl_location.setOnClickListener {

            (context as LocationSetActivity).progress.visibility = View.VISIBLE

            val placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS)
            val request = FetchPlaceRequest.builder(placesList.get(position).placeId, placeFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener({ response ->
                place = response.getPlace()


                if (placeType.equals("source")) {

                    srcObject = Source(place!!.latLng!!.latitude, place!!.latLng!!.longitude, place!!.name + ", " + place!!.address)
                    // desObject = SendBookResponse.Booking.Destination()
                }
                else {

                    // srcObject = SendBookResponse.Booking.Source()
                    desObject = Source(place!!.latLng!!.latitude, place!!.latLng!!.longitude, place!!.name + ", " + place!!.address)
                }

                if (srcObject != null && desObject != null) {
                    onItemClickListener.onItemClick("location name", srcObject!!, desObject!!)
                } else if (srcObject != null && desObject == null) {
                    if(EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java) != null) {
                        desObject = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).destination
                    }else {
                        desObject = Source()
                    }
                    onItemClickListener.onItemClick("location name", srcObject!!, desObject!!)
                }
                else if (srcObject == null && desObject != null) {

                    if(EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java) != null) {
                        srcObject = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).src
                    }
                    else {
                        srcObject =Source()
                    }
                    //srcObject = SendBookResponse.Booking.Source()
                    onItemClickListener.onItemClick("location name", srcObject!!, desObject!!)
                }

            }).addOnFailureListener({

            })
            //  mOnItemClickListener?.onItemClick("location name", place!!)
        }

/*
        holder?.rl_location.setOnClickListener {
            (context as LocationSetActivity).progress.visibility = View.VISIBLE
            val placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.ADDRESS)
            val request = FetchPlaceRequest.builder(placesList.get(position).placeId, placeFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener({ response ->
                place = response.getPlace()


                    // srcObject = Source()
                    desObject = Source(place!!.latLng!!.latitude.toString(), place!!.latLng!!.longitude.toString(), place!!.name + ", " + place!!.address)


                onItemClickListener.onItemClick("location name",  desObject!!)

            }).addOnFailureListener({

            })
            //  mOnItemClickListener?.onItemClick("location name", place!!)
        }
*/
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.location_rowitem, viewGroup, false))
    }


    override fun getItemCount(): Int {
        return placesList.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_locname: TextView = view.tv_locname
        var tv_locAddress: TextView = view.tv_locAddress
        var rl_location: RelativeLayout = view.rl_location


    }

}