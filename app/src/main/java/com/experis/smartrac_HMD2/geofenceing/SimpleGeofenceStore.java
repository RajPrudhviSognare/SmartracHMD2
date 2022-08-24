package com.experis.smartrac_HMD2.geofenceing;

/**
 Class Name: SimpleGeofenceStore
 Created by Rana Krishna Paul
 */

import android.text.format.DateUtils;

import com.experis.smartrac_HMD2.Constants;
import com.google.android.gms.location.Geofence;

import java.util.HashMap;

public class SimpleGeofenceStore {

    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 1;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;

    protected HashMap<String, SimpleGeofence> geofences = new HashMap<String, SimpleGeofence>();
    private static SimpleGeofenceStore instance = new SimpleGeofenceStore();

    public static SimpleGeofenceStore getInstance() {
        return instance;
    }

    private SimpleGeofenceStore() {

        //Date date = new Date();
        //System.out.println("date.getTime(): "+date.getTime());
        System.out.println("System.nanoTime(): "+System.nanoTime());
        System.out.println("Constants.UNIV_LAT: "+ Constants.UNIV_LAT);
        System.out.println("Constants.UNIV_LONG: "+Constants.UNIV_LONG);

        System.out.println("Constants.UNIV_RADIUS: "+Constants.UNIV_RADIUS);

        /*geofences.put("Experis IT", new SimpleGeofence("Experis IT", Double.parseDouble(Constants.UNIV_LAT), Double.parseDouble(Constants.UNIV_LONG),
                150.0f, GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_DWELL
                        | Geofence.GEOFENCE_TRANSITION_EXIT));*/

        geofences.put("Experis_IT"+System.nanoTime(), new SimpleGeofence("Experis_IT"+System.nanoTime(), Double.parseDouble(Constants.UNIV_LAT), Double.parseDouble(Constants.UNIV_LONG),
                Float.parseFloat(Constants.UNIV_RADIUS), GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_DWELL
                        | Geofence.GEOFENCE_TRANSITION_EXIT));

        System.out.println("Double.parseDouble(Constants.UNIV_LAT): "+Double.parseDouble(Constants.UNIV_LAT));
        System.out.println("Double.parseDouble(Constants.UNIV_LONG): "+Double.parseDouble(Constants.UNIV_LONG));
        System.out.println("Float.parseFloat(Constants.UNIV_RADIUS): "+Float.parseFloat(Constants.UNIV_RADIUS));

        /*geofences.put("Experis IT", new SimpleGeofence("Experis IT", 22.5687649, 88.4343958,
                25.0f, GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_DWELL
                        | Geofence.GEOFENCE_TRANSITION_EXIT));*/

    }

    public HashMap<String, SimpleGeofence> getSimpleGeofences() {
        return this.geofences;
    }

}
