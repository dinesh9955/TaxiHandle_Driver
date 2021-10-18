package com.rydz.driver.model.socketResponse

data class BookRideReponse(
    val booking: Booking,
    val message: String,
    val status: Int,
    val success: Boolean
)