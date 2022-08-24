package com.experis.smartrac_HMD2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class GeoFencingActivity extends AppCompatActivity {

    static public boolean geofencesAlreadyRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        Fragment f = new MapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.map_container, f, "Map")
                .commit();

        //fragmentManager.executePendingTransactions();

        //startGeolocationService(getApplicationContext()); //Used in GiveAttendanceTabbedActivity
    }

    /*static public void startGeolocationService(Context context) {

        Intent geolocationService = new Intent(context,
                GeolocationService.class);
        context.startService(geolocationService);

        *//*PendingIntent piGeolocationService = PendingIntent.getService(context,
                0, geolocationService, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piGeolocationService);
        alarmManager
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), 2 * 60 * 1000,
                        piGeolocationService);*//*
    }*/


}


