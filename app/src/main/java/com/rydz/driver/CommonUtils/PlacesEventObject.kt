package com.rydz.driver.CommonUtils;

import com.rydz.driver.model.socketResponse.Source


public class PlacesEventObject(src: Source, destination:Source, isPin:Boolean, tripDis:Double,isFocusOnSource:Boolean) {

    val src: Source = src
    val destination: Source = destination
    val isPin:Boolean=isPin
    val tripDistance=tripDis
    val isFocusOnSource:Boolean=isFocusOnSource


}
