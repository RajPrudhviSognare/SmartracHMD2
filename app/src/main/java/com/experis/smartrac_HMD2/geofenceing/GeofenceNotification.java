package com.experis.smartrac_HMD2.geofenceing;

/**
   Class Name: GeofenceNotification
   Created by Rana Krishna Paul
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
//import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.experis.smartrac_HMD2.Constants;
import com.experis.smartrac_HMD2.R;
import com.google.android.gms.location.Geofence;

public class GeofenceNotification {

    public static final int NOTIFICATION_ID = 229999999;

    protected Context context;

    protected NotificationManager notificationManager;
    protected Notification notification;

    public GeofenceNotification(Context context) {
        this.context = context;

        this.notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    protected void buildNotificaction(String geofenceID,
                                      int transitionType) {

        String notificationText = "";
        //Object[] notificationTextParams = new Object[] { simpleGeofence.getId() };

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                notificationText = "You are now 'Dwelling' inside the premises";
                Constants.GEOFENCE_ENABLED = true;
                Log.v("GeofenceNotification", "Dwelling inside the premises : setting Constants.GEOFENCE_ENABLED = true; ");
                break;

            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationText = "You have 'Entered' the premises";
                Constants.GEOFENCE_ENABLED = true;
                Log.v("GeofenceNotification", "Entered inside the premises : setting Constants.GEOFENCE_ENABLED = true; ");
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationText = "You have 'Exited' the premises";
                Constants.GEOFENCE_ENABLED = false;
                Log.v("GeofenceNotification", "Exited from the premises : setting Constants.GEOFENCE_ENABLED = false; ");
                break;
        }

        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationText)
                /*.setStyle(
                        new NotificationCompat.BigTextStyle()
                                .bigText(notificationText))*/
                .setAutoCancel(true);

        notification = notificationBuilder.build();
        //notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
    }

    public void displayNotification(String geofenceID,
                                    int transitionType) {
        buildNotificaction(geofenceID, transitionType);

        notificationManager.cancel(NOTIFICATION_ID);//Added Later
        ////notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
