package com.experis.smartrac_HMD2.geofenceing;

/**
 Class Name: GeofenceReceiverService
 Created by Rana Krishna Paul
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceReceiverService extends IntentService {

    private Context context;

    private static String TAG = "GeofenceReceiverService";
    public GeofenceReceiverService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            System.out.println("geofenceTransition inside 'GeofenceReceiverService' : "+geofenceTransition);
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            if(triggeringGeofences!=null){

                System.out.println("Total Geofencing Triggered inside 'GeofenceReceiverService' : "+triggeringGeofences.size());

                // Get the Ids of each geofence that was triggered.
                ArrayList<String> triggeringGeofencesIdsList = new ArrayList<String>();

                if(triggeringGeofences.size()!=0){
                    for (Geofence geofence : triggeringGeofences) {
                        triggeringGeofencesIdsList.add(geofence.getRequestId());
                    }
                }

                if(triggeringGeofencesIdsList.size()!=0){
                    String geofenceID = triggeringGeofencesIdsList.get(0);
                    System.out.println("geofenceID inside 'GeofenceReceiverService' : "+geofenceID);
                    new GeofenceNotification(getApplicationContext()).displayNotification(geofenceID,geofenceTransition);
                }

            }//if

        }
    }


}
