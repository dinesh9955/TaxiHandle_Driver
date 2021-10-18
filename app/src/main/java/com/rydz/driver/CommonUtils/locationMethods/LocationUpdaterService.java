package com.rydz.driver.CommonUtils.locationMethods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;

/**
 * Created by benjakuben on 12/17/14.
 */
public class LocationUpdaterService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Timer timer1;

    public abstract interface LocationCallback {
        public void handleNewLocation(Location location);
        public void handleOneLocation(Location location);
        public void handleCarLocation(Location location);
    }

    private static final String TAG = LocationUpdaterService.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private LocationCallback mLocationCallback;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @SuppressLint("MissingPermission")
    public LocationUpdaterService(Context context, LocationCallback callback) {
        try
        {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            mLocationCallback = callback;
            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_NO_POWER)
                    .setInterval(0)        // 4
                    // seconds, in milliseconds
                    .setFastestInterval(0); // 1 second, in milliseconds
            mContext = context;
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void connect() {
        try
        {
            mGoogleApiClient.connect();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try
        {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        try
        {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        mLocationCallback.handleOneLocation(location);
                        mLocationCallback.handleNewLocation(location);
                    }
                    // your UI code here
                });

            }
            upDateNewLocation();
            upDateNewLocationforCar();
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    private void upDateNewLocation() {
        try
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    upDateNewLocation();
                }
            },10000);

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        mLocationCallback.handleNewLocation(location);
                    }
                    // your UI code here
                });

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void upDateNewLocationforCar() {
        try
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    upDateNewLocationforCar();
                }
            },100000);

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this );
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        mLocationCallback.handleCarLocation(location);
                    }
                    // your UI code here
                });

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */

        try
        {
            if (connectionResult.hasResolution() && mContext instanceof Activity) {
                try {
                    Activity activity = (Activity) mContext;
                    // Start an Activity that tries to resolve the error
                    connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
                } catch (IntentSender.SendIntentException e) {
                    // Log the error
                    e.printStackTrace();
                }
            } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
                Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try
        {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable(){
                @Override
                public void run() {
                    mLocationCallback.handleNewLocation(location);
                    mLocationCallback.handleOneLocation(location);
                }
                // your UI code here
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }





}
