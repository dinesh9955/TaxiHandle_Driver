package com.rydz.driver.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.R
import kotlinx.android.synthetic.main.activity_register_review_acivity.*


class RegisterReviewAcivity : BaseActivity() ,View.OnClickListener {
    var pic:String=""
    var name:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_review_acivity)
        name=intent?.getStringExtra(AppConstants.SENDEDDATA)!!
        pic=intent?.getStringExtra(AppConstants.SENDCODE)!!
        Log.e("RegisterReviewAcivity",pic+"")
        try{
        getImageRequest(pic).into(iv_userImage)
        }catch (e:Exception)
        {

        }
        tv_name.setText(name)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.btn_done->
            {
                navigatewithFinish(LogInActivity::class.java)
            }
        }
    }
}
