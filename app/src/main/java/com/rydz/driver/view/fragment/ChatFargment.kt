package com.rydz.driver.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.model.message.MessageList
import com.rydz.driver.view.activity.BaseActivity
import com.rydz.driver.viewModel.login.editProfile.ChathistoryViewModel
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import com.rydz.driver.CommonUtils.AppConstants.*
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.application.App
import com.rydz.driver.fcm.MyFirebaseMessagingService.inChatScreen
import com.rydz.driver.model.chat.ChatHistoryRequest
import com.rydz.driver.model.message.MessageHistory
import com.rydz.driver.socket.AppSocketListener
import com.rydz.driver.socket.SocketListener
import com.rydz.driver.socket.SocketUrls.READMESSAGE
import com.rydz.driver.socket.SocketUrls.SEND_NEW_MESSAGE
import com.rydz.driver.view.activity.MainActivity
import com.rydz.driver.view.adapters.ChatAdapters
import io.socket.emitter.Emitter

class ChatFargment :BaseActivity(),  SocketListener {
    private var messageList: ArrayList<MessageList>? = null
    private var iv_close: ImageView? = null
    private var iv_userImage: ImageView? = null
    private var iv_send: ImageView? = null
    private var tv_name: TextView? = null
    private var tv_rating: TextView? = null
    private var et_message: EditText? = null
    private var rv_chat: RecyclerView?=null
    val CLASSTAG:String=ChatFargment::class.java.simpleName
     private var newMessageListener:String=""
    private var userName:String= ""
    private var userID:String= ""
    private var bookingID:String=""
    private var strRating:String=""
    private var strProfilePic:String=""
    private var chatAdapter: ChatAdapters?=null
    private var from:String="home"
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: ChathistoryViewModel? = null
    var APPTAG:String= ChatFargment::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)
//        if (!AppSocketListener.getInstance().isSocketConnected) {
            AppSocketListener.getInstance().setActiveSocketListener(this)
            // Restart Socket.io to avoid weird stuff ;-)
            AppSocketListener.getInstance().restartSocket()

        messageList= ArrayList<MessageList>()
        try {
            if (intent!=null)
            {
                userName=intent?.getStringExtra(AppConstants.KEYNAME)!!
                userID=intent.getStringExtra(AppConstants.KEYUSERID)!!
                bookingID=intent.getStringExtra(AppConstants.KEYBOOKINGID)!!
                from=intent.getStringExtra("fromNotification")!!
//                strRating=intent.getStringExtra(AppConstants.KEYRATING)
                strProfilePic=intent?.getStringExtra(AppConstants.KEYPROFILEPIC)!!
                newMessageListener = userID +  getUserId()
            }

        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

        (application as App).getAppComponent().doInjection(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChathistoryViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        rv_chat=findViewById(R.id.rv_chat)
        rv_chat!!.layoutManager= LinearLayoutManager(this@ChatFargment)
        rv_chat!!.setHasFixedSize(true)
        iv_close = findViewById(R.id.iv_close)
        iv_userImage = findViewById(R.id.iv_userImage)
        iv_send = findViewById(R.id.iv_send)
        tv_name = findViewById(R.id.tv_userName)
        tv_rating = findViewById(R.id.tv_rating)
        et_message = findViewById(R.id.et_message)
        iv_close!!.setOnClickListener(this)
        iv_send!!.setOnClickListener(this)
        chatAdapter= ChatAdapters(this@ChatFargment,messageList!!)
        rv_chat!!.adapter=chatAdapter

        try {
            if (!userName.isEmpty()) {
                val chatHistoryRequest = ChatHistoryRequest()
                chatHistoryRequest.bookingId = bookingID
                chatHistoryRequest.driverId = getUserId()
                chatHistoryRequest.userId = userID
                viewModel!!.hitChatHistoryApi(chatHistoryRequest)
                    tv_name!!.setText(userName)
                getImageRequest(strProfilePic).into(iv_userImage!!)

            }
        }catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }

    }

   private fun onGetNewMessage(newMessage :MessageList)
    {
        try {
            if (newMessage != null && newMessage.messageBy.compareTo(0)==0) {
                readMessage()
            }
        }catch (e:Exception)
        {
            e.printStackTrace()
        }

            messageList!!.add(newMessage)
            chatAdapter!!.updateChatList(messageList!!)
            rv_chat!!.scrollToPosition(messageList!!.size-1)

    }

    override fun onBackPressed() {
        gotoBackScreen()
    }

    private fun gotoBackScreen() {
        hideKeyboard()
        MainActivity.mainActivity.forceHideKeyboard(baseContext!!)
        if (from.equals("not"))
        {
            navigatewithFinish(MainActivity::class.java)
        }else {
            finish()
        }
        inChatScreen=false
    }

    private fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    /*
  * method to handle response
  * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> {
//                progressDialog!!.show()
            }

            Status.SUCCESS -> {
//                progressDialog!!.dismiss()
                    renderApiSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
//                progressDialog!!.dismiss()
                showAlert( resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }
    /*
   * method to handle success response
   * */
    private fun renderApiSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.d(APPTAG+"response=", response.toString())
            val data:String=  Utils.toJson(response)
            val gson1 =  Gson()
            val loginResponse = gson1.fromJson(data, MessageHistory::class.java)
            if (loginResponse.success.toString().equals("1"))
            {
                messageList=loginResponse.messageList
                chatAdapter!!.updateChatList(messageList!!)
                rv_chat!!.scrollToPosition(messageList!!.size-1)

            }
            else
            {
//                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.iv_close -> {

              gotoBackScreen()
            }
            R.id.iv_send -> {

                if (et_message!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                    showAlert(getString(R.string.message_should_not_empty))
                } else {
                        val myObject = JSONObject()
                        try {
                            myObject.put(DRIVER_ID, getUserId())
                            myObject.put("userId", userID)
                            myObject.put(BOOKING_ID, bookingID)
                            myObject.put(ADMIN_ID, getAdminId())
                            myObject.put(MESSAGE, et_message!!.text.toString())
                            myObject.put(MESSAGE_BY, "1")
                            myObject.put(MY_CHAT_ID,userID + getUserId())
                            myObject.put(OPPONENT_ID,  getUserId() + userID)
                            sendObjectToSocket(myObject, SEND_NEW_MESSAGE)
                            et_message!!.setText("")

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inChatScreen=true
        commitListeners()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private val onGetNewMessageListener = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.e(CLASSTAG, "call: onDriverChangeRideStatusListener   " + args[0].toString())
            try {
                val jsonObject:JSONObject= args[0] as JSONObject
                if (jsonObject.getString("success").toString().equals(TRUE))
                {
                    val data:String=jsonObject.getJSONObject("msg").toString()
                    val gson1 =  Gson();
                    val loginResponse = gson1.fromJson(data, MessageList::class.java)

                    runOnUiThread(object:Runnable {
                        public override fun run() {
                            // change UI elements here
                            onGetNewMessage(loginResponse)

                        }
                    })
                }
                else
                {
                    showAlert(jsonObject.getString("message").toString())
                }
            }catch (ex:java.lang.Exception)
            {
                ex.printStackTrace()
            }
        }
    }

    fun sendObjectToSocket(jsonObject: JSONObject,type:String)
    {
        try {
            AppSocketListener.getInstance().emit(type,jsonObject)
        }catch (ex: java.lang.Exception)
        {
            ex.printStackTrace()
        }

    }


    override fun onSocketConnected() {
        Log.e(CLASSTAG,"onSocketConnected")
       commitListeners()
    }

    private fun commitListeners() {
        try {
            if (AppSocketListener.getInstance().isSocketConnected)
            {
                AppSocketListener.getInstance().off(newMessageListener, onGetNewMessageListener)
                AppSocketListener.getInstance().addOnHandler(newMessageListener, onGetNewMessageListener)
            }
        }catch (ex:java.lang.Exception)
        {
            ex.printStackTrace()
        }

    }


    override fun onSocketDisconnected() {
        Log.e(CLASSTAG,"onSocketDisconnected")
    }

    override fun onSocketConnectionError() {
        Log.e(CLASSTAG,"onSocketConnectionError")
    }

    override fun onSocketConnectionTimeOut() {
        Log.e(CLASSTAG,"onSocketConnectionTimeOut")
    }


    private fun readMessage()
    {
        val myObject = JSONObject()
        try {

            myObject.put(DRIVER_ID, getUserId())
            myObject.put(BOOKING_ID, bookingID)

            sendObjectToSocket(myObject, READMESSAGE)



        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }






}
