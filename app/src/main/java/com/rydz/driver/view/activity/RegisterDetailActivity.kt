package com.rydz.driver.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.SENDEDDATA
import com.rydz.driver.CommonUtils.PermissionUtils
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.ApiResponse
import com.rydz.driver.apiConstants.Constant
import com.rydz.driver.apiConstants.Status
import com.rydz.driver.apiConstants.Urls.PRIVACY_POLICY_URL
import com.rydz.driver.apiConstants.Urls.TERM_CONDITION_URL
import com.rydz.driver.apiConstants.ViewModelFactory
import com.rydz.driver.application.App
import com.rydz.driver.model.registerstatusresponse.RegisterNumberStatusResponse
import com.rydz.driver.model.requests.RegisterRequest
import com.rydz.driver.model.requests.RegistrerCheckStatusRequest
import com.rydz.driver.model.uploadImage.UploadImageResponse
import com.rydz.driver.viewModel.login.editProfile.EditProfileViewModel
import com.rydz.driver.viewModel.login.editProfile.UploadImageViewModel
import kotlinx.android.synthetic.main.activity_regisetr_detail.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class RegisterDetailActivity : BaseActivity(), View.OnClickListener {

    var proFilePic: String = ""
    var countryCode: String = ""
    var phoneNumber: String = ""

    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModelUploadImage: UploadImageViewModel? = null
    var progressDialog: ProgressDialog? = null

    var APPTAG: String = RegisterDetailActivity::class.java.name

    var viewModel: EditProfileViewModel? = null

    var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisetr_detail)
        try {
            if (intent != null) {
                if (intent.getStringExtra(AppConstants.SENDEDDATA) != null) {
                    phoneNumber = intent?.getStringExtra(AppConstants.SENDEDDATA)!!
                    countryCode = intent?.getStringExtra(AppConstants.SENDCODE)!!
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        toolBarSetUp()
        progressDialog = Constant.getProgressDialog(this, "Please wait...")

        (application as App).getAppComponent().doInjection(this)


        viewModelUploadImage = ViewModelProviders.of(this, viewModelFactory).get(UploadImageViewModel::class.java)

        viewModelUploadImage!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditProfileViewModel::class.java)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun toolBarSetUp() {

        tv_title_common.text = getString(R.string.signup)
        iv_back_common.setImageResource(R.drawable.ic_back_white)
        rl_common.setBackgroundColor(resources.getColor(android.R.color.transparent))


        val prefixInfoText =
            SpannableString(getString(R.string.by_clicking_i_agree_to_let_rydz_store_the_information_i_provide_in_order_to_provide_and_improve_its_services_you_may_delete_your_account_at_anytime))

        prefixInfoText.setSpan(
            ForegroundColorSpan(getColor(R.color._b78830)),
            150,
            prefixInfoText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        cb_information.text = prefixInfoText

        val prefixText =
            SpannableString(getString(R.string.string_terms))
        val prefixTextLen = prefixText.length


        prefixText.setSpan(ForegroundColorSpan(getColor(R.color._e4c755)), 59, 79, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        prefixText.setSpan(ForegroundColorSpan(getColor(R.color._e4c755)), 84, 99, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        prefixText.setSpan(
            ForegroundColorSpan(getColor(R.color._b78830)),
            99,
            prefixTextLen,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val uris = Uri.parse(TERM_CONDITION_URL)
                val intents = Intent(Intent.ACTION_VIEW, uris)
                startActivity(intents)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val privacyclickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val uris = Uri.parse(PRIVACY_POLICY_URL)
                val intents = Intent(Intent.ACTION_VIEW, uris)
                startActivity(intents)

            }


            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        prefixText.setSpan(clickableSpan, 59, 79, 0)
        prefixText.setSpan(privacyclickableSpan, 84, 99, 0)






        tv_terms.movementMethod = LinkMovementMethod.getInstance()


        tv_terms.text = prefixText


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_reset -> {

                goToNextScreen()

            }
            R.id.iv_userImage -> {
                type = "image"
                showPictureDialog()
            }

            R.id.iv_back_common -> {
                navigatewithFinish(LogInActivity::class.java)
            }


        }
    }

    /*
   * method to validate $(mobile number) and $(password)
   * */
    private fun isValid(): Boolean {

        if (et_first_name.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.nameshouldnotempty))
            et_first_name.requestFocus()
            return false
        }
        if (et_last_name.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.nameshouldnotempty))
            et_email.requestFocus()
            return false
        }
        if (et_email.text.toString().trim().isEmpty()) {
            showAlert(resources.getString(R.string.emailshouldnotempty))
            et_email.requestFocus()
            return false
        }
        if (!Utils.isValidEmail(et_email.text.toString().trim())) {
            showAlert(resources.getString(R.string.emailshouldnotempty))
            et_email.requestFocus()
            return false
        }
        if (et_password.text.toString().length < 8) {
            showAlert(resources.getString(R.string.enter_valid_password))
            et_password.requestFocus()
            return false
        }
        if (!et_confirm_password.text.toString().equals(et_password.text.toString().trim())) {
            showAlert(resources.getString(R.string.confirmpasswordshouldmatch))
            et_confirm_password.requestFocus()
            return false
        }
        if (cb_terms.isChecked && cb_information.isChecked) {
            return true
        } else {
            showAlert(resources.getString(R.string.terms_conditions))

            return false
        }

        return true
    }

    private fun goToNextScreen() {
        if (isValid()) {
            if (et_email.text.toString().trim().isEmpty()) {
                val registerRequest = RegisterRequest()
                registerRequest.profilePic = proFilePic
                registerRequest.firstName = et_first_name.text.toString()
                registerRequest.lastName = et_last_name.text.toString()
                registerRequest.email = et_email.text.toString()
                registerRequest.password = et_password.text.toString()
                registerRequest.contryCode = countryCode
                registerRequest.phone = phoneNumber
                registerRequest.company = et_companyname.text.toString()
                if(cb_updates.isChecked)
                registerRequest.isSubscribed = 1
                else
                    registerRequest.isSubscribed = 0
                val intent = Intent(this@RegisterDetailActivity, RegisterDetailActivity2::class.java)
                intent.putExtra(SENDEDDATA, registerRequest)
                startActivity(intent)
//                finish()
            } else {
                type = "submit"
                val registerRequest = RegistrerCheckStatusRequest()
                registerRequest.email = et_email.text.toString()
                registerRequest.adminId = getAdminId()
                viewModel!!.hitCheckRegisterStatus(registerRequest)
            }

        }
    }


    /*
 * method to handle response
 * */
    private fun consumeResponse(apiResponse: ApiResponse) {

        when (apiResponse.status) {

            Status.LOADING -> progressDialog!!.show()

            Status.SUCCESS -> {
                progressDialog!!.dismiss()
                if (type.toString().equals("image")) {
                    renderUploadImageSuccessResponse(apiResponse.data!!)

                } else {
                    renderSuccessResponse(apiResponse.data!!)
                }
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                showAlert(resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }

    /*
    * method to handle success response
    * */
    private fun renderUploadImageSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG + "response=", "rendeUploadImageSuccessResponse " + response.toString())
            val data: String = Utils.toJson(response)
            val gson1 = Gson()
            val loginResponse = gson1.fromJson(data, UploadImageResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                Log.d("data=", loginResponse.pic.toString() + "")
                proFilePic = loginResponse.pic.profilePic.toString()
//                getImageRequest(proFilePic).into(iv_userImage)

            } else {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }

    /*
  * method to handle success response
  * */
    private fun renderSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG + "responsecheck=", "rendeSuccessresponse   " + response.toString())
            val data: String = Utils.toJson(response)
            val gson1 = Gson()
            val loginResponse = gson1.fromJson(data, RegisterNumberStatusResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE)) {
                val registerRequest = RegisterRequest()
                registerRequest.profilePic = proFilePic
                registerRequest.firstName = et_first_name.text.toString()
                registerRequest.lastName = et_last_name.text.toString()
                registerRequest.email = et_email.text.toString()
                registerRequest.password = et_password.text.toString()
                registerRequest.contryCode = countryCode
                registerRequest.phone = phoneNumber
                registerRequest.company = et_companyname.text.toString()
                if(cb_updates.isChecked)
                    registerRequest.isSubscribed = 1
                else
                    registerRequest.isSubscribed = 0
                val intent = Intent(this@RegisterDetailActivity, RegisterDetailActivity2::class.java)
                intent.putExtra(SENDEDDATA, registerRequest)
                startActivity(intent)
//                finish()
            } else {
                showAlert(loginResponse.message.toString())
            }
        } else {
            showAlert(resources.getString(R.string.errorString))
        }
    }


    override fun getRequestCode(requestcode: Int, data: Intent?) {
        super.getRequestCode(requestcode, data)
        when (requestcode) {

            PermissionUtils.CAMERAPERMISSIONCODE -> {
                Log.e("CAMERAPERMISSIONCODE", "Here")
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                //iv_profile!!.setImageBitmap(thumbnail)
                val file = File(saveImage(thumbnail))
                try {
                    Glide.with(this).load(file).into(iv_userImage)
                } catch (e: Exception) {

                }
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("pic", file.absolutePath, reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)


            }

            PermissionUtils.GALLERYREQUESTCODE -> {
                Log.e("GALLERYREQUESTCODE", "Here")
                val file = File(getImagePathFromUri(data!!.data!!))
                try {
                    Glide.with(this).load(file).into(iv_userImage)
                } catch (e: Exception) {

                }
                val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("pic", file.absolutePath, reqFile)
                viewModelUploadImage!!.hitUploadImageApi(body)


            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        navigatewithFinish(LogInActivity::class.java)
    }

}
