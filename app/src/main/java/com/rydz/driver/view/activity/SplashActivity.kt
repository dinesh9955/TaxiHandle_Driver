package com.rydz.driver.view.activity

import android.os.Handler
import android.os.Bundle
import com.rydz.driver.CommonUtils.AppConstants.STATICUSERID
import com.rydz.driver.R


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if (isLogin())
            {
                STATICUSERID=getUserId()
                navigatewithFinish(MainActivity::class.java)
            }
            else {
                navigatewithFinish(LogInActivity::class.java)
            }
        }, 2500)
    }
}
