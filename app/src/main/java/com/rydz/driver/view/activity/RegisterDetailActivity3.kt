package com.rydz.driver.view.activity

import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.AppConstants.SENDEDDATA
import com.rydz.driver.CommonUtils.FileUtils
import com.rydz.driver.CommonUtils.PermissionUtils
import com.rydz.driver.CommonUtils.PermissionUtils.Companion.CAMERAPERMISSIONCODE
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.R
import com.rydz.driver.apiConstants.*
import com.rydz.driver.application.App
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.model.requests.RegisterRequest
import com.rydz.driver.viewModel.login.editProfile.RegisterViewModel
import kotlinx.android.synthetic.main.activity_regisetr_detail3.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class RegisterDetailActivity3 : BaseActivity() ,View.OnClickListener{

    var registerRequest = RegisterRequest()
    @set:Inject
    var viewModelFactory: ViewModelFactory? = null
    var viewModel: RegisterViewModel? = null
    var APPTAG:String=RegisterDetailActivity3::class.java.name
    var progressDialog: ProgressDialog? = null
    val DRIVER_LICENCE="driver_license";
    val TZINI="tzini"
    val CAR_LICENCE="car_licence"
    val NUMBER_PLATE="number_plate"
    var typeOfImage:String=CAR_LICENCE
    var driver_licence: File? = null
    var tzini: File? = null
    var numberPlate: File? = null
    var rc: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regisetr_detail3)
        try {
            if (intent!=null)
            {
                registerRequest = intent.getParcelableExtra<RegisterRequest>(SENDEDDATA) as RegisterRequest
            }
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }
        progressDialog = Constant.getProgressDialog(this, "Please wait...")
        (application as App).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java!!)

        viewModel!!.loginResponse().observe(this, Observer<ApiResponse> { this.consumeResponse(it!!) })
        toolBarSetUp()

    }

    private fun toolBarSetUp() {

        iv_back_common.setImageResource(R.drawable.ic_back_white)
        val title: String = getString(R.string.documents)
        tv_title_common.setText(title)
        rl_common.setBackgroundColor(resources.getColor(android.R.color.transparent))


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.btn_reset -> {

                if (isValid())
                {
//                    showAlert("Success")
                    val reqFileDriverLicence = RequestBody.create(MediaType.parse("image/*"), driver_licence)
                    val bodyreqFileDriverLicence = MultipartBody.Part.createFormData("licence", driver_licence!!.getAbsolutePath(), reqFileDriverLicence)
                    val reqFiletzini = RequestBody.create(MediaType.parse("image/*"), tzini)
                    val bodyreqFiletzini = MultipartBody.Part.createFormData("inzi", tzini!!.getAbsolutePath(), reqFiletzini)
                    val reqFilecarLicence = RequestBody.create(MediaType.parse("image/*"), rc)
                    val bodyreqFilecarLicence = MultipartBody.Part.createFormData("rc", rc!!.getAbsolutePath(), reqFilecarLicence)
                    val reqFilecarnumberPlate = RequestBody.create(MediaType.parse("image/*"), numberPlate)
                    val bodyreqFilenumberPlate = MultipartBody.Part.createFormData("plate", numberPlate!!.getAbsolutePath(), reqFilecarnumberPlate)
                    // finally serialize the above MyAttach object into JSONObject
               val gson =  Gson();
                 val json:String = gson.toJson(registerRequest)
                    val requestBody = RequestBody.create(
                        MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                        json
                    )
                    viewModel!!.hitRegisterApi(requestBody,bodyreqFileDriverLicence,bodyreqFiletzini,bodyreqFilenumberPlate,bodyreqFilecarLicence)
                }
//                goToNextScreen()

            }
            R.id.iv_back_common -> {
//                val intent = Intent(this@RegisterDetailActivity3, RegisterDetailActivity2::class.java)
//                intent.putExtra(SENDEDDATA, registerRequest)
//                startActivity(intent)
                finish()
//                navigatewithFinish(RegisterDetailActivity2::class.java)
            }

            R.id.iv_car_licence -> {
                typeOfImage = CAR_LICENCE
                if (checkAndRequestPermissions(PermissionUtils.CAMERAPERMISSIONCODE)) {
                    showPictureDialog()
                    //openCamera()
                }
            }
            R.id.iv_tzini -> {
                typeOfImage = TZINI
                if (checkAndRequestPermissions(PermissionUtils.CAMERAPERMISSIONCODE)) {
                    showPictureDialog()
                   // openCamera()
                }
            }
            R.id.iv_numberPlate -> {
                typeOfImage = NUMBER_PLATE
                if (checkAndRequestPermissions(PermissionUtils.CAMERAPERMISSIONCODE)) {
                    showPictureDialog()
                   // openCamera()
                }
            }
            R.id.iv_driver_licence -> {
                typeOfImage = DRIVER_LICENCE
                if (checkAndRequestPermissions(PermissionUtils.CAMERAPERMISSIONCODE)) {
                    showPictureDialog()
                    //openCamera()
                }
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
                renderSuccessResponse(apiResponse.data!!)
            }

            Status.ERROR -> {
                progressDialog!!.dismiss()
                Toast.makeText(
                    this@RegisterDetailActivity3,
                    resources.getString(R.string.errorString),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
            }
        }
    }

    /*
    * method to handle success response
    * */
    private fun renderSuccessResponse(response: JsonElement) {
        if (!response.isJsonNull) {
            Log.e(APPTAG+"response=", response.toString())
            val data: String = Utils.toJson(response)
            val gson1 = Gson();
            val loginResponse = gson1.fromJson(data, LoginResponse::class.java)
            if (loginResponse.success.toString().equals(AppConstants.TRUE))
            {
                Log.d("data=", loginResponse.driver.id.toString() + "")
                val phonebookIntent = Intent(this@RegisterDetailActivity3, RegisterReviewAcivity::class.java)
                phonebookIntent.putExtra(AppConstants.SENDEDDATA,loginResponse.driver.firstName+" "+loginResponse.driver.lastName)
                phonebookIntent.putExtra(AppConstants.SENDCODE,loginResponse.driver.profilePic)
                startActivity(phonebookIntent)
                finishAffinity()
        } else {
            showAlert(loginResponse.message.toString())
        }
    }
         else {
            Toast.makeText(this@RegisterDetailActivity3, resources.getString(R.string.errorString), Toast.LENGTH_SHORT).show()
        }
    }


    /*
  * method to validate $(mobile number) and $(password)
  * */
    private fun isValid(): Boolean {

        if (driver_licence == null) {
            showAlert(resources.getString(R.string.please_upload_driver_licenve))
            return false
        }
        else if (numberPlate == null) {
            showAlert(resources.getString(R.string.please_upload_numberPlate_picture_with_car))
            return false
        }
        else if (tzini == null) {
            showAlert(resources.getString(R.string.please_upload_tzini_picture))
            return false
        }

        else if (rc == null) {
            showAlert(resources.getString(R.string.please_upload_car_licence_picture))
            return false
        }

        return true
    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {
        super.getRequestCode(requestcode, data)
        Log.e("24000", requestcode.toString())

        when (requestcode) {
                      CAMERAPERMISSIONCODE -> {
                Log.e("CAMERAPERMISSIONCODE", "Here")


                val thumbnail = data!!.extras!!.get("data") as Bitmap

                val file=  File(saveImage(thumbnail))

                if (typeOfImage.equals(DRIVER_LICENCE)) {
                    driver_licence =file
                    // Create glide request manager
                    try{
                     Glide.with(this).load(driver_licence).into(iv_driver_licence)
                    }catch (e:Exception)
                    {

                    }
                } else if (typeOfImage.toString().equals(CAR_LICENCE)) {
                    rc = file
                    try{
                    Glide.with(this).load(rc).into(iv_car_licence)
                    }catch (e:Exception)
                    {

                    }
                } else if (typeOfImage.toString().equals(TZINI)) {
                    try{
                    tzini = file
                    Glide.with(this).load(tzini).into(iv_tzini)
                    }catch (e:Exception)
                    {

                    }
                } else if (typeOfImage.toString().equals(NUMBER_PLATE)) {
                    try{
                    numberPlate = file
                    Glide.with(this).load(numberPlate).into(iv_numberPlate)
                    }catch (e:Exception)
                    {

                    }
                }

            }

            PermissionUtils.GALLERYREQUESTCODE -> {



                if (data != null) {
                    val contentURI = data!!.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        val file = File(FileUtils.getPath(this,data!!.data))


                        if (typeOfImage.toString().equals(DRIVER_LICENCE)) {
                            driver_licence = file

                            Log.e("305",driver_licence?.name.toString())


                            // try{
                            Glide.with(this).load(driver_licence).into(iv_driver_licence)
                            /* }catch (e:Exception)
                             {
                                   Log.e("297", e.printStackTrace().toString())
                             }*/
                        } else if (typeOfImage.toString().equals(CAR_LICENCE)) {
                            rc = file
                            try{
                                Glide.with(this).load(rc).into(iv_car_licence)
                            }catch (e:Exception)
                            {
                                Log.e("305", e.printStackTrace().toString())

                            }
                        } else if (typeOfImage.toString().equals(TZINI)) {
                            try{
                                tzini = file
                                Glide.with(this).load(tzini).into(iv_tzini)
                            }catch (e:Exception)
                            {
                                Log.e("314", e.printStackTrace().toString())

                            }
                        } else if (typeOfImage.toString().equals(NUMBER_PLATE)) {
                            try{
                                numberPlate = file
                                Glide.with(this).load(numberPlate).into(iv_numberPlate)
                            }catch (e:Exception)
                            {
                                Log.e("323", e.printStackTrace().toString())

                            }
                        }





                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }





            }
        }
    }

    override fun onBackPressed() {

        finish()
    }

    private fun checkAndRequestPermissions(REQUEST_ID_MULTIPLE_PERMISSIONS:Int): Boolean {
        val camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded = ArrayList<String>()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(arrayOfNulls<String>(listPermissionsNeeded.size)), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

}
