package com.rydz.driver.model

data class NewMessageResponse(
        var message: String = "", // Successful
        var msg: Msg = Msg(),
        var success: Boolean = false, // true
        var result: Int = 0

)