package com.rydz.driver.CommonUtils.locationMethods;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MyLocation {
    private static final int REQUEST_CODE_CHECK_SETTINGS = 1431;
    private static final long LOCATION_UPDATE_INTERVAL = 6000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = LOCATION_UPDATE_INTERVAL / 2;


    private static final String TAG = MyLocation.class.getSimpleName();
    private static final int MINIMUM_ACCURACY_IN_METERS = 0;
    private static MyLocation myLocation;
    private int mNewLocationReceivedCount = 0;
    private LocationResultInterface locationResultInterface;
    private Context context = null;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;

    public static MyLocation getInstance() {
        return myLocation;
    }


    @SuppressLint("MissingPermission")
    public boolean getLocation(Context context, LocationResultInterface result) {

        locationResultInterface = result;
        this.context = context;
        myLocation = this;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        //This callback gets called every time FusedLocationProvider receives a new location.
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mNewLocationReceivedCount++;
                Location location = locationResult.getLastLocation();

                if (location.hasAccuracy() && location.getAccuracy() <= MINIMUM_ACCURACY_IN_METERS && mNewLocationReceivedCount > 1) {
                    //Since we've received an acceptable location, set the count back to 0 to get ready for next time.
                    Log.e("61","61");
                    mNewLocationReceivedCount = 0;
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    requestLocationUpdates();
                }
                locationResultInterface.handleOneLocation(location);
                locationResultInterface.handleNewLocation(location);
                locationResultInterface.handleCarLocation(location);

            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        requestLocationUpdates();
        return true;
    }

    //Handles the Request Updates button and requests start of location updates.
    private void requestLocationUpdates() {
        //Check if Device has GPS enabled before running LocationSettingsRequest, since the latter forces user to enable High Accuracy mode.
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } catch (SecurityException unlikely) {
                Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
            }
        } else {
            //We've added a lot of code here to verify that the user's location settings are sufficient for the location request we're about to make.
            //The below 4 lines are a dummy locationRequest that must be fed into the Builder below. Otherwise, when GPS is turned back on it won't work correctly.
            LocationRequest locationRequest;
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(LOCATION_UPDATE_INTERVAL).setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(context);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // All location settings requirements are satisfied. We now initialize location requests here.
                    try {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } catch (SecurityException unlikely) {
                        Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
                    }
                }
            });

            task.addOnFailureListener((Activity) context, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult((Activity) context, REQUEST_CODE_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                sendEx.printStackTrace();
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private void showMessage(String enabled) {
        Toast.makeText(context, enabled, Toast.LENGTH_SHORT).show();
    }

}

