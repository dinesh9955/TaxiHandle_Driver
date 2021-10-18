package com.rydz.driver.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.rydz.driver.CommonUtils.PermissionUtils
import com.rydz.driver.CommonUtils.Utils

import com.rydz.driver.interfaces.RequestCode
import com.rydz.driver.model.userRating.RatingList
import com.rydz.driver.view.activity.BaseActivity
import com.rydz.driver.view.activity.MainActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.openDialog

import java.lang.Exception
import java.util.ArrayList
import android.os.Build
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.rydz.driver.R


open class BaseFragment : Fragment(), View.OnClickListener, RequestCode {
    var m_currentToast:Toast?=null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        try {
            MainActivity.mainActivity.checkGpsService()

        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onClick(v: View?) {

    }


    override fun getRequestCode(requestcode: Int, data: Intent?) {

    }




    override fun onResume() {
        super.onResume()
        Utils(context).changeLanguage(MainActivity.mainActivity.getSelectedLanguage())
    }

    override fun onGetPermissionCode(requestCode: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getRequestCode(requestCode, data)
        }
    }

    fun getNoteValue():String{
        if (MainActivity.mainActivity.getMyPreferences().getStringValue(AppConstants.NOTE).isNullOrEmpty()) {
            return ""
        } else {
            return MainActivity.mainActivity.getMyPreferences().getStringValue(AppConstants.NOTE)
        }
    }

    fun setNoteValue(note : String){
        MainActivity.mainActivity.getMyPreferences().setStringValue(AppConstants.NOTE,note)
    }

    fun navigate(destination: Class<*>) {
        val intent = Intent(context, destination)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera() {
        if ((context as BaseActivity).isCameraPermissions()) {
            camera()
        }
    }



    open fun setFeedBack(ratingListArrayList: ArrayList<RatingList>?) {

    }

    private fun camera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            takePicture,
            PermissionUtils.CAMERAPERMISSIONCODE
        )//zero can be replaced with any action code
    }







    companion object {
        val main: MainActivity
            get() = MainActivity.mainActivity
    }

    fun showAlert(message:String)
    {
        if (!openDialog)
        {
            openDialog=true

        Handler(Looper.getMainLooper()).post(object:Runnable {
            public override fun run() {
                if (message!=null && !message.isEmpty())
                {
                    AlertDialog.Builder(context!!)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, object: DialogInterface.OnClickListener{
                            override fun onClick(arg0:DialogInterface, arg1:Int) {
                                openDialog=false
                                arg0.dismiss()
                            }
                        }).create().show()
                }

            }
        })

    }
    }

    /**
     * To showzoomableimage
     */
    fun showZoomableImage(img : String) {
        val zoomableImgDialog = Dialog(activity!!)
        zoomableImgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        zoomableImgDialog.setContentView(R.layout.dialog_image)
        zoomableImgDialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);
        val myZoomageView = zoomableImgDialog.findViewById<ImageView>(R.id.myZoomageView)
        val ivClose = zoomableImgDialog.findViewById<ImageView>(R.id.iv_close)
        try{
            MainActivity.mainActivity.getImageRequest(img).into(myZoomageView)
        }catch (e:Exception)
        {

        }
        ivClose.setOnClickListener {
            zoomableImgDialog.dismiss()
        }
        zoomableImgDialog.show()
    }


}
