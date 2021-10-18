package com.rydz.driver.model.requests

data class VehicleRateDetails(
    val message: String,
    val success: Boolean,
    val vehicletypedetail: Vehicletypedetail
)