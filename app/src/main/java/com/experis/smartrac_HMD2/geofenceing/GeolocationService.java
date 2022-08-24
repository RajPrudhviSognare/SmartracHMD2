package com.experis.smartrac_HMD2.geofenceing;

/**
 Class Name: GeolocationService
 Created by Rana Krishna Paul
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.experis.smartrac_HMD2.CommonUtils;
import com.experis.smartrac_HMD2.Constants;
import com.experis.smartrac_HMD2.GeoFencingActivity;
import com.experis.smartrac_HMD2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.HashMap;
import java.util.Map;

//import android.support.v7.app.NotificationCompat;

public class GeolocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    public GeolocationService() {
    }

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10*1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected GeofencingRequest geofencingRequest;

    private String TAG = "GeolocationService";

    private PendingIntent mPendingIntent;

    private int locationAccuracyCounter = 0;
    private int NOTIFICATION_ID1 = 999999999;
    private Context context;
    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    public void onStart(Intent intent, int startId) {

        System.out.println("GeolocationService Started");
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "selfStop() is executed: onDestroy()");
        unRegisterGeofences();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void broadcastLocationFound(Location location) {
        Intent intent = new Intent("com.experis.attendancetracking.geolocation.service.casio.demo.common");
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        intent.putExtra("done", 1);

        sendBroadcast(intent);
    }

    protected void startLocationUpdates() {
        try{
            if(Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You have denied these permissions, so this app won't work as expected.",
                        Toast.LENGTH_LONG).show();
                return  ;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch(Exception e ){
            e.printStackTrace();
        }

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "Connected to GoogleApiClient");
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG,
                "New location : " + location.getLatitude() + ", "
                        + location.getLongitude() + ". "
                        + location.getAccuracy());
        Constants.CURRENT_LAT = String.valueOf(location.getLatitude());
        Constants.CURRENT_LONG = String.valueOf(location.getLongitude());
        System.out.println("Constants.CURRENT_LAT: "+Constants.CURRENT_LAT);
        System.out.println("Constants.CURRENT_LONG: "+Constants.CURRENT_LONG);

        ////Added Later after getting Crash report from Play store
        if(Constants.CURRENT_LAT.equalsIgnoreCase("")||Constants.CURRENT_LAT==null||Constants.CURRENT_LAT.equalsIgnoreCase("null")){
            Constants.CURRENT_LAT = "0.0";
            System.out.println("Constants.CURRENT_LAT: 1 "+Constants.CURRENT_LAT);
        }
        if(Constants.CURRENT_LONG.equalsIgnoreCase("")||Constants.CURRENT_LONG==null||Constants.CURRENT_LONG.equalsIgnoreCase("null")){
            Constants.CURRENT_LONG = "0.0";
            System.out.println("Constants.CURRENT_LONG: 1 "+Constants.CURRENT_LONG);
        }
        ////

        /*int distance = CommonUtils.calculateDistanceInKilometer(Double.valueOf(Constants.CURRENT_LAT),Double.valueOf(Constants.CURRENT_LONG),
                Double.valueOf(Constants.UNIV_LAT),Double.valueOf(Constants.UNIV_LONG));
        System.out.println("Distance in KM: "+distance);

        Constants.DISTANCE = String.valueOf(distance);
        System.out.println("Constants.DISTANCE in KM: "+Constants.DISTANCE);*/

        try {
            int distance = CommonUtils.calculateDistanceInMeter(Double.valueOf(Constants.CURRENT_LAT), Double.valueOf(Constants.CURRENT_LONG),
                    Double.valueOf(Constants.UNIV_LAT), Double.valueOf(Constants.UNIV_LONG));
            System.out.println("Distance in M: " + distance);

            Constants.DISTANCE = String.valueOf(distance);
            System.out.println("Constants.DISTANCE in M: " + Constants.DISTANCE);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        broadcastLocationFound(location);

        if (!GeoFencingActivity.geofencesAlreadyRegistered) {
            if (mGoogleApiClient.isConnected()){ //added later on 17.05.2017
                registerGeofences();
            }
        }

        /*if(location.getAccuracy() <= 10) {
            Log.v(TAG, "Stopping geolocation service: because location.getAccuracy() <= 10)");
            stopSelf();
        }*/
        if(location.getAccuracy() <= 10) {
            Log.v(TAG, "Very Low Location Accuracy! inside location.getAccuracy() <= 10)");
            /*Toast.makeText(getApplicationContext(), "Your Location Accuracy is Quite Low, So This App Might Not Work As Axpected",
                    Toast.LENGTH_SHORT).show();*/
            if(locationAccuracyCounter==0||locationAccuracyCounter==10||locationAccuracyCounter==20||locationAccuracyCounter==30){
                //showCustomNotification();
            }
            System.out.println("locationAccuracyCounter Value: "+locationAccuracyCounter);
            locationAccuracyCounter++;
            if(locationAccuracyCounter==40){
                locationAccuracyCounter = 0;
            }
        }
    }

    //showCustomNotification
    private void showCustomNotification(){
       /* //Alert Dialog Builder
        AlertDialog aldb = new AlertDialog.Builder(getApplicationContext()).create();
        aldb.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        aldb.setTitle("Location Accuracy Error!");
        aldb.setMessage("\nYour Mobile's Location Accuracy is Very Low! Please Change Your Mobile's Location Mode into 'High Accuracy'\n");
        //aldb.setPositiveButton("OK, Got It", null);
        aldb.show();*/

        notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                getApplicationContext())
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(getApplicationContext().getString(R.string.app_name)+" - Location Accuracy Low!")
                //.setContentText("Please Change Location Mode into 'High Accuracy'")
                .setContentText("Make Sure Location Accuracy is 'High'")
                .setAutoCancel(true);
        notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.cancel(NOTIFICATION_ID1);//Added Later
        notificationManager.notify(NOTIFICATION_ID1, notification);

    }

    protected void registerGeofences() {
        if (GeoFencingActivity.geofencesAlreadyRegistered) {
            return;
        }

        Log.v(TAG, "Registering Geofences");

        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore
                .getInstance().getSimpleGeofences();

        GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            geofencingRequestBuilder.addGeofence(sg.toGeofence());
        }

        geofencingRequest = geofencingRequestBuilder.build();

        mPendingIntent = requestPendingIntent();

        if( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "You have denied these permissions, so this app won't work as expected.",
                    Toast.LENGTH_LONG).show();
            return  ;
        }

        //if (mGoogleApiClient.isConnected()) {

        GeoFencingActivity.geofencesAlreadyRegistered = true;

        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                    geofencingRequest, mPendingIntent).setResultCallback(this);

        //}

        //GeoFencingActivity.geofencesAlreadyRegistered = true;
    }

    private PendingIntent requestPendingIntent() {

        if (null != mPendingIntent) {

            return mPendingIntent;
        } else {

            Intent intent = new Intent(this, GeofenceReceiverService.class);
            return PendingIntent.getService(this, 119900, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

        }
    }

    protected void unRegisterGeofences(){

        if (mGoogleApiClient.isConnected()) {

            if (null != mPendingIntent) {
                LocationServices.GeofencingApi.removeGeofences(
                        mGoogleApiClient,
                        // This is the same pending intent that was used in addGeofences().
                        mPendingIntent
                ).setResultCallback(this); // Result processed in onResult().

                Log.v(TAG, "Un Registering Geofences inside: null != mPendingIntent");
            }

            //GeoFencingActivity.geofencesAlreadyRegistered = false;
            Log.v(TAG, "Un Registering Geofences inside");
        }
        Log.v(TAG, "Un Registering Geofences outside");

        GeoFencingActivity.geofencesAlreadyRegistered = false;

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.v(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG,
                "Connection failed: ConnectionResult.getErrorCode() = "
                        + result.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        Log.v(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Added Later//////////////////
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        // **************************
        builder.setAlwaysShow(true); // this is the key ingredient
        // **************************
        //End Of Added Later//////////

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            //Toast.makeText(getApplicationContext(), "Geofences_Added", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Registering Geofences: onResult(Status status): status.isSuccess()");
            GeoFencingActivity.geofencesAlreadyRegistered = true;
        } else {
            GeoFencingActivity.geofencesAlreadyRegistered = false;
            String errorMessage = getErrorString(this, status.getStatusCode());
            Toast.makeText(getApplicationContext(), errorMessage,
                    Toast.LENGTH_LONG).show();
            Log.v(TAG, "!status.isSuccess(): onResult(Status status)");
        }

        if (status.isCanceled()) {
            Toast.makeText(getApplicationContext(),
                    "Geofences_Removed", Toast.LENGTH_SHORT)
                    .show();
            Log.v(TAG, "Un Registering Geofences: onResult(Status status): status.isCanceled()");
            GeoFencingActivity.geofencesAlreadyRegistered = false;
        }

    }

    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "geofence_not_available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "geofence_too_many_geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "geofence_too_many_pending_intents";
            default:
                return "unknown_geofence_error";
        }
    }


}
