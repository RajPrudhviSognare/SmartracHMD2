package com.experis.smartrac_HMD2.geofenceing;

/**
 Class Name: SimpleGeofence
 Created by Rana Krishna Paul
 */

import com.google.android.gms.location.Geofence;

public class SimpleGeofence {

    private String id = null;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private float radius = 0.0f;;
    private long expirationDuration = 0;
    private int transitionType = 0;
    //private int loiteringDelay = 5000;
    private int loiteringDelay = 60000;
    private int mNotificationResponsiveness = 1*1000;


    public SimpleGeofence(String geofenceId, double latitude, double longitude,
                          float radius, long expiration, int transition) {
        this.id = geofenceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.expirationDuration = expiration;
        this.transitionType = transition;
    }

    public Geofence toGeofence() {
        Geofence g = new Geofence.Builder().setRequestId(getId())
                .setTransitionTypes(transitionType)
                .setCircularRegion(getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(expirationDuration)
                .setNotificationResponsiveness(mNotificationResponsiveness)
                .setLoiteringDelay(loiteringDelay).build();

        return g;
    }

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public float getRadius(){
        return radius;
    }
    public String getId(){
        return id;
    }


}
