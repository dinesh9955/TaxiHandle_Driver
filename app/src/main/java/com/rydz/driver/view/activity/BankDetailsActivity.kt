package com.rydz.driver.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.R
import com.rydz.driver.application.App
import com.rydz.driver.model.requests.RegisterRequest
import kotlinx.android.synthetic.main.activity_bank_details.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*


class BankDetailsActivity : BaseActivity(), View.OnClickListener {

    var registerRequest = RegisterRequest()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        try {
            if (intent != null) {
                registerRequest = intent.getParcelableExtra<RegisterRequest>(AppConstants.SENDEDDATA) as RegisterRequest

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        (application as App).getAppComponent().doInjection(this)




        toolBarSetUp()
        initViewS()
    }

    private fun toolBarSetUp() {
        val title: String = getString(R.string.signup)
        tv_title_common.text = title
    }

    private fun initViewS() {

        iv_back_common.setImageResource(R.drawable.ic_back_white)

        tv_title_common.text = getString(R.string.bank_details)
        rl_common.setBackgroundColor(resources.getColor(android.R.color.transparent))

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_reset -> {

                if (isValid()) {
                    goToNextScreen()
                }

            }

            R.id.iv_back_common -> {

                finish()
            }


        }
    }


    override fun onBackPressed() {

        finish()
    }



    private fun isValid(): Boolean {

        if (et_account_number.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.account_number_shouldnotempty))
            return false
        } else if (et_swift_code.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.swiftcode_shouldnotempty))
            return false
        }
        else if (et_holder_name.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.holder_name_shouldnotempty))
            return false
        }

        return true
    }

    private fun goToNextScreen() {
        registerRequest.accountHolderName = et_holder_name.text.toString()
        registerRequest.accountNumber = et_account_number.text.toString()
        registerRequest.swiftCode = et_swift_code.text.toString()

        val intent = Intent(this, RegisterDetailActivity3::class.java)
        intent.putExtra(AppConstants.SENDEDDATA, registerRequest)

        startActivity(intent)
    }
}
