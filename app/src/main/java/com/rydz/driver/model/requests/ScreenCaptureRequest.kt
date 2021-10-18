package com.rydz.driver.model.requests

import com.google.gson.annotations.SerializedName

class ScreenCaptureRequest {

    @SerializedName("bookingId")
    internal var bookingId = ""
    @SerializedName("image")
    internal var image = ""

}
