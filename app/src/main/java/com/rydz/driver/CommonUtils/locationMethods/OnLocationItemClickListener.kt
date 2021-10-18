package com.rydz.driver.CommonUtils.locationMethods

import com.rydz.driver.model.socketResponse.Source


interface OnLocationItemClickListener {

    fun onItemClick(name: String ,place_obj : Source,des_obj : Source)
}