package com.rydz.driver.view.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.model.socketResponse.Booking
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.fragment.ChatFargment
import com.rydz.driver.view.fragment.PayFragment
import com.rydz.driver.view.fragment.PoolRideFragment
import kotlinx.android.synthetic.main.dialog_note.*
import kotlinx.android.synthetic.main.pooride_rowitem.view.*
import kotlinx.android.synthetic.main.pooride_rowitem.view.iv_chatIndicator


class PoolRideAdapter(
    var context: Context,
    var poolRideFragment: PoolRideFragment,
    var bookingList: List<Booking>
) : RecyclerView.Adapter<PoolRideAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.pooride_rowitem, p0, false)
        return ViewHolder(myView)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {

        try {
            if (bookingList.get(p1).bookingType.equals("1") && bookingList.get(p1).storeId != null && bookingList.get(p1).storeId._id != null) {//merchant ride  - hide chat option and show store name

                viewHolder.tv_chat.visibility = View.GONE
                viewHolder.tv_username.text = "" + bookingList.get(p1).storeId.storeName
                try{
                viewHolder.tv_bookingno.text = "Order No : " + bookingList.get(p1).orderNo
                }
                catch (e : Exception)
                {

                }


            } else {  //pooling ride  - user name
                viewHolder.tv_username.text = "" + bookingList.get(p1).userId.firstName + " " + bookingList.get(p1).userId.lastName
                try{
                MainActivity.mainActivity.getImageRequest(bookingList.get(p1).userId.profilePic).into(viewHolder.iv_user_image)
                }catch (e:Exception)
                {

                }
            }
        } catch (exception: Exception) {

        }


        var driverStatus = 9

        if (bookingList.size > 0) {
            driverStatus = bookingList.get(p1).status.toInt()
        }

        if (bookingList.get(p1).note != null && bookingList.get(p1).note.toString().length > 0) {
            viewHolder.iv_note.visibility = View.VISIBLE
        } else
            viewHolder.iv_note.visibility = View.GONE


        viewHolder.iv_note.setOnClickListener {
            showNotesDialog(bookingList.get(p1).note.toString())
        }

        viewHolder.btn_starttrip.setOnClickListener {
            Log.e("57", bookingList.get(p1).status.toString())
            poolRideFragment.OnstartTrip(
                bookingList.get(p1).id,
                bookingList.get(p1).status.toInt(),
                p1
            )
        }


        if(bookingList.get(p1).msgRead)
            viewHolder.iv_chatIndicator.visibility = View.VISIBLE
            else
            viewHolder.iv_chatIndicator.visibility = View.GONE


        viewHolder.tv_chat.setOnClickListener {
            if (!Constant.checkInternetConnection(context)) {
            } else {
                if (bookingList.get(p1) != null) {
                    val intent = Intent(context, ChatFargment::class.java)
                    intent.putExtra("fromNotification", "home")
                    intent.putExtra(AppConstants.KEYBOOKINGID, bookingList.get(p1).id)
                    intent.putExtra(
                        AppConstants.KEYPROFILEPIC,
                        bookingList.get(p1).userId.profilePic
                    )
                    intent.putExtra(AppConstants.KEYUSERID, bookingList.get(p1).userId.id)
                    intent.putExtra(
                        AppConstants.KEYNAME,
                        bookingList.get(p1).userId.firstName + " " + bookingList.get(
                            p1
                        ).userId.lastName
                    )
                    viewHolder.iv_chatIndicator.visibility=View.GONE
                    context.startActivity(intent)
                }
            }

        }
        when (driverStatus) {
            0 -> {
                viewHolder.ll_poolRide.visibility = View.VISIBLE
                viewHolder.tv_src.visibility = View.GONE
                viewHolder.tv_des.visibility = View.GONE
                viewHolder.btn_starttrip.text = context.getString(R.string.arrived)
                poolRideFragment.tv_user_address.text = bookingList.get(p1).source.name
                poolRideFragment.notifyItemOfRecycle(p1)

            }

            1 -> {
                viewHolder.ll_poolRide.visibility = View.VISIBLE
                if (bookingList.get(p1).bookingType.equals("1"))
                    viewHolder.btn_starttrip.text = context.getString(R.string.pickup)
                else
                    viewHolder.btn_starttrip.text = context.getString(R.string.starttrip)
                viewHolder.tv_src.visibility = View.GONE
                viewHolder.tv_des.visibility = View.GONE
                poolRideFragment.tv_user_address.text = bookingList.get(p1).destination.name
                poolRideFragment.notifyItemOfRecycle(p1)

            }

            2 -> {
                viewHolder.ll_poolRide.visibility = View.VISIBLE
                viewHolder.tv_chat.visibility = View.GONE
                viewHolder.iv_note.visibility = View.GONE
                viewHolder.tv_pickup.setText(R.string.dropoff)
                viewHolder.tv_src.visibility = View.VISIBLE
                viewHolder.tv_src.text = bookingList.get(p1).source.name
                viewHolder.tv_des.visibility = View.VISIBLE
                viewHolder.tv_des.text = bookingList.get(p1).destination.name
                poolRideFragment.tv_user_address.text = bookingList.get(p1).destination.name
                if (bookingList.get(p1).bookingType.equals("1"))
                    viewHolder.btn_starttrip.text = context.getString(R.string.delieverd)
                else
                    viewHolder.btn_starttrip.text = context.getString(R.string.complete_trip)

                poolRideFragment.notifyItemOfRecycle(p1)


            }

            3 -> {


                viewHolder.ll_poolRide.visibility = View.VISIBLE

                if (MainActivity.mainActivity.FragmentTag.toString().equals(AppConstants.POOLSCREEN)) {

                    if (bookingList.get(p1).bookingType.equals("1")) {   //its for merchat ride


                        if (bookingList.get(p1).isPaid != 1) {


                            try {

                                val fragment = PayFragment()
                                val b = Bundle()
                                b.putSerializable(AppConstants.SENDEDDATA, bookingList.get(p1))
                                fragment.arguments = b
                                MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)


                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }


                        } else {
                            viewHolder.ll_poolRide.visibility = View.GONE

                            if (bookingList.size == 1) {
                                Log.e("200", "200")
                                poolRideFragment.showPaymentDoneDialog(
                                    bookingList.get(p1).fare,
                                    bookingList.get(p1).id,bookingList.get(p1).bookingType
                                )

                            } else {
                                poolRideFragment.showPaymentDoneDialog(
                                    bookingList.get(p1).fare,
                                    bookingList.get(p1).id,bookingList.get(p1).bookingType
                                )
                            }


                        }
                    } else {  // normal pool ride
                        try {

                            val fragment = PayFragment()
                            val b = Bundle()
                            b.putSerializable(AppConstants.SENDEDDATA, bookingList.get(p1))
                            fragment.arguments = b
                            MainActivity.mainActivity.replaceFragment(fragment, AppConstants.ACCEPTSCREEN)


                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                    }

                }


            }


        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ll_poolRide = itemView.ll_poolRide
        var btn_starttrip = itemView.btn_starttrip
        var tv_username = itemView.tv_user_name
        var tv_bookingno = itemView.tv_bookingno
        var iv_user_image = itemView.iv_user_image
        var tv_chat = itemView.tv_chat
        var iv_note = itemView.iv_note
        var tv_pickup = itemView.tv_pickup
        var tv_src = itemView.tv_src
        var tv_des = itemView.tv_des
        var iv_chatIndicator=itemView.iv_chatIndicator


    }


    fun showNotesDialog(notes: String) {

        var noteDialog: Dialog? = null



        noteDialog = Dialog(context)
        noteDialog.setCancelable(false)
        noteDialog.setContentView(R.layout.dialog_note)
        if (noteDialog != null) {
            noteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        try {
            noteDialog.tv_note.text = notes

            noteDialog.tv_okay.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (noteDialog.isShowing)
                        noteDialog.dismiss()
                }
            })
            if (!noteDialog.isShowing)
                noteDialog.show()


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }


    }


    fun showNotificationIndicatore(pos: Int) {
        Log.e("284",pos.toString())
      bookingList.get(pos).msgRead=true
        notifyItemChanged(pos)

    }

}

