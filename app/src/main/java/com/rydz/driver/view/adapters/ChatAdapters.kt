package com.rydz.driver.view.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.model.message.MessageList
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapters(var context:Context,var messageList:ArrayList<MessageList>) : RecyclerView.Adapter<ChatAdapters.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val context = p0.getContext()
        val inflater = LayoutInflater.from(context!!)

        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_chat, p0, false)

        // Return a new holder instance
        val viewHolder = ViewHolder(contactView)
        return viewHolder
//       val myView=LayoutInflater.from(context).inflate(R.layout.item_chat,p0,false)
//        return ViewHolder(myView)
    }
    override fun getItemCount(): Int {

        Log.e("SIZE : ",messageList.size.toString())
       return messageList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {

        if (messageList.get(p1).messageBy.toString().equals("1"))
        {
            viewHolder.ll_sender.visibility=View.VISIBLE
            viewHolder.ll_reciever.visibility=View.GONE
            viewHolder.tv_sender_message.setText(messageList.get(p1).message)
            viewHolder.tv_sender_time.setText(Utils.getTime(messageList.get(p1).date))
        }
        else
        {
            viewHolder.ll_sender.visibility=View.GONE
            viewHolder.ll_reciever.visibility=View.VISIBLE
            viewHolder.tv_reciever_message.setText(messageList.get(p1).message)
            viewHolder.tv_reciever_time.setText(Utils.getTime(messageList.get(p1).date))
        }

    }

    fun updateChatList( newmessageList:ArrayList<MessageList>)
    {
        messageList=newmessageList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var ll_sender=itemView.ll_sender
        var ll_reciever=itemView.ll_reciever
        var tv_sender_message=itemView.tv_sender_message
        var tv_reciever_message=itemView.tv_reciever_message
        var tv_sender_time=itemView.tv_sender_time
        var tv_reciever_time=itemView.tv_reciever_time

    }
}

