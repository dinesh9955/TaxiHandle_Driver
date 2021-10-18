/*
 * Sudesh Bishnoi
 */

package com.rydz.driver.view.activity

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.media.MediaScannerConnection.scanFile
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.CommonUtils.CompressFile
import com.rydz.driver.CommonUtils.PermissionUtils
import com.rydz.driver.CommonUtils.Utils
import com.rydz.driver.CommonUtils.sharedpreferences.MySharedPreferences
import com.rydz.driver.R
import com.rydz.driver.apiConstants.Urls
import com.rydz.driver.interfaces.RequestCode
import com.rydz.driver.model.loginResponse.LoginResponse
import com.rydz.driver.networkReciever.ConnectivityChangeReceiver
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, RequestCode,
    ConnectivityChangeReceiver.ConnectivityReceiverListener {
    var imageFilePath: String = ""
    private var isAppPermission: Boolean = true
    private val IMAGE_DIRECTORY = "/capy"
    private var isLocationPermission: Boolean = true
    lateinit var receiver: ConnectivityChangeReceiver
    private var mSnackBar: Snackbar? = null
    val TAG = "BaseActivity" // Activity Tag

    /*

    OnCreate method when a activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        // Utils(this@BaseActivity).changeLanguage(getSelectedLanguage())
    }


    //Navigate screen without finishing activity
    public fun navigate(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
    }

    //Navigate screen with finish a activity
    public fun navigatewithFinish(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
        finish()
    }


    //Method is used for show a keyboard
    fun showKeyboard(editText: EditText) {
        if (editText.text.length > 0) {
            editText.setSelection(editText.text.length)
        }
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val editText = getCurrentFocus()

        if (editText != null)
            imm.showSoftInput(editText, 0)
    }

    //method is used for hide  a keyboard
    fun hideKeyboard(editText: EditText) {
        try {
            editText.isFocusable = false
            editText.isFocusableInTouchMode = false
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val editText = getCurrentFocus()
            if (editText != null)
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    //method is used for hide  a keyboard
    fun justHideKeyboard(editText: EditText) {
        try {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val editText = getCurrentFocus()
            if (editText != null)
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    //Activity onResume functionaliy
    override fun onResume() {

        ConnectivityChangeReceiver.connectivityReceiverListener = this

        try {
            super.onResume()
            //Change a language
            Utils(this@BaseActivity).changeLanguage(getSelectedLanguage())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    //My Shared Prefrences Reference
    fun getMyPreferences(): MySharedPreferences {
        return MySharedPreferences(this@BaseActivity)
    }

    //check is user login or not
    fun isLogin(): Boolean {
        if (getMyPreferences().getStringValue(AppConstants.USER_ID).isNullOrEmpty()) {
            return false
        } else {
            return true
        }
    }

    //Get Country code using Telephone manager
    fun getCountryCodeINCode(): String {

        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.getNetworkCountryIso()
        Log.e("countryCodeValue", countryCodeValue + "")
        return countryCodeValue
    }

    //OnStart method with register a network reciever
    override fun onStart() {
        super.onStart()

        try {
            registerNetworkReceiver()
        } catch (e: Exception) {
        }
    }

    //Ondestroy with deregister a reciever
    override fun onDestroy() {
        /**
         * Unregister Reciever
         */
        try {
            unregisterReceiver(receiver)
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    /**
     * Method to register network change receiver
     */
    fun registerNetworkReceiver() {

        try {
            receiver = ConnectivityChangeReceiver(this, Handler())
            registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        } catch (e: Exception) {
        }

    }

    //Show progress untiol a image not load in Glide
    fun getImageLoader(): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return circularProgressDrawable
    }


    //Request glide to load a image
    fun getImageRequest(imageUrl: String): RequestBuilder<Any> {
        // Create glide request manager
        val requestManager = Glide.with(this)

        val requestOptions = RequestOptions()
        requestOptions.placeholder(getImageLoader())
        requestOptions.error(R.drawable.user_placeholder)


// Create request builder and load image.
        val requestBuilder =
            requestManager.asBitmap().load(Urls.BASEIMAGEURL + imageUrl).thumbnail(0.01f).apply(requestOptions) as RequestBuilder<Any>
        return requestBuilder
    }

    //GetUser saved id
    fun getUserId(): String {
        if (isLogin()) {
            return getMyPreferences().getStringValue(AppConstants.USER_ID)
        } else {
            return ""
        }

    }


    // get User saved Auth token from preferences
    fun getAuthToken(): String {
        if (!getMyPreferences().getStringValue(AppConstants.AUTH_TOKEN).toString().isNullOrEmpty()) {
            return getMyPreferences().getStringValue(AppConstants.AUTH_TOKEN).toString()
        } else {
            return ""
        }
    }

    // Get seleted language from preferences
    fun getSelectedLanguage(): String {
        if (getMyPreferences().getStringValue(AppConstants.MYLANGUAGE).isNullOrEmpty()) {
            return "en"
        } else {
            return getMyPreferences().getStringValue(AppConstants.MYLANGUAGE)
        }
    }

    //Get user's all detail from preferences
    fun getUserDetail(): LoginResponse {
        if (!getMyPreferences().getStringValue(AppConstants.USERDETAIL).isNullOrEmpty()) {
            val gson = Gson()
            val json = getMyPreferences().getStringValue(AppConstants.USERDETAIL)
            val obj = gson.fromJson(json, LoginResponse::class.java)
            return obj
        } else {
            return LoginResponse()
        }
    }

    //Admin id to request a server for hitting a api
    fun getAdminId(): String {
      //return "5f69f4fac158265871588117"
       return "5f7aed891b111e4b680552a4"
    }

    //get Firebase token for notifications
    fun getDeviceId(): String {
       /* val refreshedToken = FirebaseInstanceId.getInstance().token.toString() + ""
        return refreshedToken*/

        if (!getMyPreferences().getStringValue(AppConstants.DEVICETOKEN).toString().isNullOrEmpty()) {
            return getMyPreferences().getStringValue(AppConstants.DEVICETOKEN).toString()
        } else {
            return ""
        }
    }

    //get device type
    fun getDeviceType(): String {
        return "Android"
    }

    //OnClick methid
    override fun onClick(v: View?) {

    }

    //show a toast message
    fun showMessage(message: String) {
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
    }


    //Open camera with check permission
    fun openCamera() {
        if (checkAndRequestPermissions2(PermissionUtils.CAMERAPERMISSIONCODE)) {
            camera()
        }
    }

    //open camera if permision granted
    private fun camera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            takePicture, PermissionUtils.CAMERAPERMISSIONCODE
        )
    }

    //open gallery with chechk permission
    @RequiresApi(Build.VERSION_CODES.M)
    fun openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            if (isGalleryPermissions()) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(
                    pickPhoto,
                    PermissionUtils.GALLERYREQUESTCODE
                )//one can be replaced with any action code
            }

        } else {
            // do something for phones running an SDK before lollipop
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                pickPhoto,
                PermissionUtils.GALLERYREQUESTCODE
            )//one can be replaced with any action code
        }

    }

    //on Get A result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getRequestCode(requestCode, data)
        }

    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {

    }

    override fun onGetPermissionCode(requestCode: Int) {

    }


    //run tim epermissions
    @RequiresApi(Build.VERSION_CODES.M)
    public fun isCameraPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val storagepermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission == PackageManager.PERMISSION_GRANTED && storagepermission == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to capture image denied")

            return true
        } else {
            makeCameraRequest()
            return false
        }


    }

    public fun makeCameraRequest() {
        ActivityCompat.requestPermissions(
            this@BaseActivity,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PermissionUtils.CAMERAPERMISSIONCODE
        )
    }


    //run tim epermissions
    @RequiresApi(Build.VERSION_CODES.M)
    public fun isGalleryPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )


        if (permission == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to capture image denied")

            return true
        } else {
            makeGalleryRequest()
            return false
        }


    }
    public fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    public fun makeGalleryRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PermissionUtils.READSTORAGEPERMISSIONCODE
        )
    }

    public fun isLocationPermissions(): Boolean {
        var isGranted: Boolean = false
        val permission = ContextCompat.checkSelfPermission(
            this@BaseActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionCoarce = ContextCompat.checkSelfPermission(
            this@BaseActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permission != PackageManager.PERMISSION_GRANTED && permissionCoarce != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to get image from gallery denied")
            makeLocationRequest()
            isGranted = false

        } else {
            isGranted = true
        }
        return isGranted
    }

    public fun isGpsEnable(): Boolean {
        val service = getSystemService(LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        Log.e("ENABLED : ","is  "+enabled)
        return enabled
    }

    public fun checkGpsService(): Boolean {
        if (!isLocationPermissions()) {
            return false
        } else if (!isGpsEnable()) {
            showSettingsAlert()
            return false
        } else {
            return true
        }
    }

    //Got to app permission setting using intent
    fun goToApppermission() {
        try {
            if (isAppPermission) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("SETTINGS")
                alertDialog.setMessage("Enable Location Provider! Go to settings menu?")
                alertDialog.setPositiveButton("Settings",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", getPackageName(), null)
                            intent.setData(uri)
                            startActivity(intent)
                            isAppPermission = true
                            dialog.dismiss()
                        }
                    })
                alertDialog.setNegativeButton("Cancel",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            isAppPermission = true
                            dialog.dismiss()
                        }
                    })
                alertDialog.show()
                isAppPermission = false
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    // Show a Location Setting
    fun showSettingsAlert() {
        try {
            if (isLocationPermission) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setCancelable(false)
                alertDialog.setTitle("SETTINGS")
                alertDialog.setMessage("Enable Location Provider! Go to settings menu?")
                alertDialog.setPositiveButton("Settings",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                            isLocationPermission = true
                            dialog.dismiss()
                        }
                    })
                alertDialog.setNegativeButton("Cancel",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            isLocationPermission = true
                            dialog.dismiss()
                        }
                    })
                isLocationPermission = false
                alertDialog.show()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    public fun makeLocationRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PermissionUtils.LOCATIONPERMISSIONCODE
        )
    }

    //On  get permission result
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionUtils.CAMERAPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && !shouldShowRequestPermissionRationale(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {

                        val i = Intent().apply {
                            setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            addCategory(Intent.CATEGORY_DEFAULT);
                            setData(Uri.parse("package:com.rydz.driver"));
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        }

                        startActivity(i);

                    } else {
                        makeCameraRequest()

                    }

                } else {
                    if (checkAndRequestPermissions2(PermissionUtils.CAMERAPERMISSIONCODE)) {
                        //openCamera()
                        showPictureDialog()
                    }
                }
            }

            PermissionUtils.READSTORAGEPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        val i = Intent().apply {
                            setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            addCategory(Intent.CATEGORY_DEFAULT);
                            setData(Uri.parse("package:com.rydz.driver"));
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        }

                        startActivity(i);

                    }
                } else {
                    openGallery()
                    Log.i(TAG, "Permission has been granted by user")
                }
            }

            PermissionUtils.LOCATIONPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }


        }
    }


    //Check multiple permissions
    private fun checkAndRequestPermissions2(REQUEST_ID_MULTIPLE_PERMISSIONS: Int): Boolean {
        val camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded = ArrayList<String>()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA)
        }

        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toArray(arrayOfNulls<String>(listPermissionsNeeded.size)),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    // save captured image from camera
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".png")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            scanFile(
                this,
                arrayOf(f.getPath()),
                arrayOf("image/png"), null
            )
            fo.close()
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    @Nullable
    public fun getImagePathFromUri(@Nullable aUri: Uri): String {
        var imagePath: String? = null
        if (aUri == null) {
            return imagePath!!
        }
        if (DocumentsContract.isDocumentUri(this, aUri)) {
            val documentId = DocumentsContract.getDocumentId(aUri)
            if ("com.android.providers.media.documents" == aUri.getAuthority()) {
                val id = documentId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == aUri.getAuthority()) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                imagePath = getImagePath(contentUri, null!!)
            }
        } else if ("content".equals(aUri.getScheme(), ignoreCase = true)) {
            imagePath = getImagePath(aUri, null)
        } else if ("file".equals(aUri.getScheme(), ignoreCase = true)) {
            imagePath = aUri.getPath()
        }

        Log.e("GalleryPath : ", imagePath.toString())
        val file = CompressFile.getCompressed(this@BaseActivity, imagePath)
        Log.e("Galleryfile : ", file.absolutePath)
        return file.absolutePath!!
    }

    private fun getImagePath(aUri: Uri, aSelection: String?): String {
        var path: String? = null
        val cursor = getContentResolver()
            .query(aUri, null, aSelection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
//        val file:File=CompressFile.getCompressedImageFile(File(path),this@BaseActivity)
//        path=file.absolutePath
        return path!!
    }




    //Sjoe capture or pick a image dialouge
    public fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.selectaction))
        val pictureDialogItems = arrayOf<String>(
            getString(R.string.selectfromgallery),
            getString(R.string.capturefromcamera),
            getString(R.string.cancel)
        )
        pictureDialog.setItems(pictureDialogItems,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    when (which) {
                        0 -> openGallery()
                        1 -> openCamera()
                        2 -> dialog.dismiss()
                    }
                }
            })
        pictureDialog.show()
    }

    //Show alert message
    fun showAlert(message: String) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            public override fun run() {
                if (message != null && !message.isEmpty()) {
                    try {
                      var  dialog =   AlertDialog.Builder(this@BaseActivity)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, object : DialogInterface.OnClickListener {
                                override fun onClick(arg0: DialogInterface, arg1: Int) {
                                    arg0.dismiss()
                                }
                            }).create()
dialog.show()

                       // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#b78830"))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

            }
        })

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {


        showSnackBar(isConnected)
    }


    private fun showSnackBar(isConnected: Boolean) {

        try {

            if (!isConnected) {

                val messageToUser = getString(R.string.check_internet) //TODO

                mSnackBar = Snackbar.make(
                    findViewById(R.id.topLayout),
                    messageToUser,
                    Snackbar.LENGTH_LONG
                ) //Assume "rootLayout" as the root layout of every activity.
                mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
                mSnackBar?.show()
            } else {
                mSnackBar?.dismiss()
            }

        } catch (e: java.lang.Exception) {

        }
    }


}
