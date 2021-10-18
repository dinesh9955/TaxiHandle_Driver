package com.rydz.driver.model.requests

import com.google.gson.annotations.SerializedName

class LoginRequest {

    @SerializedName("adminId")
    internal var adminId = ""
    @SerializedName("email")
    internal var email = ""
    @SerializedName("password")
    internal var password = ""
    @SerializedName("deviceId")
    internal var deviceId = ""
    @SerializedName("deviceType")
    internal var deviceType = ""
}
