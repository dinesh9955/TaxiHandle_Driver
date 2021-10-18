package com.rydz.driver.CommonUtils.locationMethods;

import android.location.Location;

public  interface LocationResultInterface {
    public void handleNewLocation(Location location);
    public void handleOneLocation(Location location);
    public void handleCarLocation(Location location);
}
