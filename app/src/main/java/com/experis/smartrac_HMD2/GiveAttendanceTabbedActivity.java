package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.experis.smartrac_HMD2.geofenceing.GeolocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

public class GiveAttendanceTabbedActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private ImageView attendancetopbarbackImageViewID;
    private ImageView attendancetopbarusericonImageViewID;

    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_attendance_tabbed);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.attendance_topbar_title);

        /*PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        checkUpdatedGooglePlayVersion();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(0);

        attendancetopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attendancetopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        attendancetopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GiveAttendanceTabbedActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        startGeolocationService(getApplicationContext());

        //Added Later//////////////////
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            System.out.println("Inside GiveAttendanceTabbedActivity Page checkAllPermissions() is called Above Lallipop: ");
            try{
                checkAllPermissions();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //End Of Added Later//////////

    }//onCreate()

    private void checkAllPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(GiveAttendanceTabbedActivity.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GiveAttendanceTabbedActivity.this,
                    android.Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(GiveAttendanceTabbedActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1991);

                // The callback method gets the result of the request.
            }

        }//if

    }//checkAllPermissions()

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1991: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // tasks you need to do.
                    System.out.println("CAMERA, WRITE_EXTERNAL_STORAGE & READ_EXTERNAL_STORAGE ARE GRANTED!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("CAMERA, WRITE_EXTERNAL_STORAGE & READ_EXTERNAL_STORAGE ARE REJECTED!");
                }
            }
        }
    }

    static public void startGeolocationService(Context context) {

        Intent geolocationService = new Intent(context,
                GeolocationService.class);
        context.startService(geolocationService);

        /*Intent geolocationService = new Intent(context,
                GeolocationService.class);
        PendingIntent piGeolocationService = PendingIntent.getService(context,
                0, geolocationService, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piGeolocationService);
        alarmManager
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), 1 * 60 * 1000,
                        piGeolocationService);*/
    }

    static public void stopGeolocationService(Context context) {

        Intent geolocationService = new Intent(context,
                GeolocationService.class);
        context.stopService(geolocationService);

        /*Intent geolocationService = new Intent(context,
                GeolocationService.class);
        PendingIntent piGeolocationService = PendingIntent.getService(context,
                0, geolocationService, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piGeolocationService);
        alarmManager
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), 1 * 60 * 1000,
                        piGeolocationService);*/
    }

    private void initAllViews(){
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        Constants.UNIV_LAT = prefs.getString("USEROUTLETLATITUDE","0.0");
        Constants.UNIV_LONG = prefs.getString("USEROUTLETLONGITUDE","0.0");
        Constants.UNIV_RADIUS = prefs.getString("USEROUTLETGPSRANGE","500.0");

        System.out.println("Constants.UNIV_LAT: "+Constants.UNIV_LAT);
        System.out.println("Constants.UNIV_LONG: "+Constants.UNIV_LONG);
        System.out.println("Constants.UNIV_RADIUS: "+Constants.UNIV_RADIUS);

        attendancetopbarbackImageViewID = (ImageView) findViewById(R.id.attendancetopbarbackImageViewID);
        attendancetopbarusericonImageViewID = (ImageView) findViewById(R.id.attendancetopbarusericonImageViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(GiveAttendanceTabbedActivity.this);
    }

    private void setupViewPager(ViewPager viewPager) {
        //ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AttendanceIntimeFragmet(), "In TIme");
        adapter.addFragment(new AttendanceOuttimeFragment(), "Out TIme");
       // adapter.addFragment(new AttendanceWeeklyoffFragment(), "Weekly Off");
        adapter.addFragment(new AttendanceLeaveFragment(), "Leave");
        adapter.addFragment(new AttendanceMeetingFragment(), "On Duty");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        stopGeolocationService(getApplicationContext());
        clearAllNotification();
        //Added Later
        Constants.UNIV_LAT = "0.0";
        Constants.UNIV_LONG = "0.0";
        Constants.UNIV_RADIUS = "500.0";
        Constants.DISTANCE = "0";
        ////////////
        GiveAttendanceTabbedActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void clearAllNotification(){
        notificationManager = (NotificationManager)GiveAttendanceTabbedActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onResume(){
        super.onResume();
        //startGeolocationService(getApplicationContext());

     /*   System.out.println("onResume() is called inside GiveAttendanceTabbedActivity: ");
        if(ImageCapture.ATTENDANCE_TYPE.equalsIgnoreCase("out")){
            //newCameraInterface.onClickPictureBack();
            int position = tabLayout.getSelectedTabPosition();
            System.out.println("onResume(): Tab position: "+position);
            Fragment fragment = adapter.getItem(tabLayout.getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        ((AttendanceOuttimeFragment)fragment).onClickPictureBack1();
                        break;
                }
            }
        }//if*/


        if(ImageCapture.ATTENDANCE_TYPE.equalsIgnoreCase("in")){
            //newCameraInterface.onClickPictureBack();
            int position = tabLayout.getSelectedTabPosition();
            System.out.println("onResume(): Tab position: "+position);
            Fragment fragment = adapter.getItem(tabLayout.getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        try {
                            ((AttendanceIntimeFragmet) fragment).onClickPictureBack2();
                        }
                        catch(NullPointerException e){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setMessage(e.getMessage());
                            alertDialogBuilder.setPositiveButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        } catch (Exception e){

                        }
                        break;
                    case 1:

                        break;

                }
            }
        }//if

        System.out.println("onResume() is called inside GiveAttendanceTabbedActivity: ");
        if(ImageCapture.ATTENDANCE_TYPE.equalsIgnoreCase("out")){
            //newCameraInterface.onClickPictureBack();
            int position = tabLayout.getSelectedTabPosition();
            System.out.println("onResume(): Tab position: "+position);
            Fragment fragment = adapter.getItem(tabLayout.getSelectedTabPosition());
            if (fragment != null) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        ((AttendanceOuttimeFragment)fragment).onClickPictureBack1();
                        break;
                }
            }
        }//if
    }

    private void checkUpdatedGooglePlayVersion(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( getApplicationContext() );
        System.out.println("checkUpdatedGooglePlayVersion() Status: "+status);
        if(status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
            final AlertDialog.Builder aldb = new AlertDialog.Builder(GiveAttendanceTabbedActivity.this);
            aldb.setCancelable(false);
            aldb.setTitle("Error!");
            aldb.setMessage("Google Play services out of date. Please update it before using Attendance features.");
            aldb.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                       onBackPressed();
                }
            });
            aldb.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id=com.google.android.gms"));
                    stopGeolocationService(getApplicationContext());
                    clearAllNotification();
                    GiveAttendanceTabbedActivity.this.finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    startActivity(i);
                }
            });

            aldb.show();
        }
    }

}//Main Class
