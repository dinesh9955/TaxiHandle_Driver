package com.rydz.driver.model.message

data class CheckUnreadMessgae(
    var message: String = "", // Successful
    var msg: Msg = Msg(),
    var success: Boolean = false // true
)