package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class AttendanceApprovalRevisedActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView attendanceapprovaltopbarbackImageViewID;
    private ImageView attendanceapprovaltopbarusericonImageViewID;
    //private Spinner attendanceApprovalAttendanceTypeSpinnerID;
    private AppCompatSpinner attendanceApprovalAttendanceTypeSpinnerID;
    private LinearLayout containerViewID;
    private int pos = 0;

    private String ATTENDANCE_TYPE[] = {"Select Attendance Type","In time","Out time","Weekly off","Leave","Meeting"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_approval_revised);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.attendance_approval_topbar_title);


        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        SetDropDownItemsForAttendanceType();

        attendanceapprovaltopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //User Icon Click Event
        attendanceapprovaltopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //Attendance Types
        attendanceApprovalAttendanceTypeSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = parent.getItemAtPosition(position).toString();
                pos = position;
                int pos1 = attendanceApprovalAttendanceTypeSpinnerID.getSelectedItemPosition();
                System.out.println("Selected position : "+ pos);
                System.out.println("Selected pos1 : "+ pos1);
                System.out.println("Selected name : "+ name);
                /*if(position==0){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                }
                else if(position==1){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                    loadIntimeFragment();
                }
                else if(position==2){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                    loadOuttimeFragment();
                }
                else if(position==3){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                    loadWeeklyoffFragment();
                }
                else if(position==4){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                    loadLeaveFragment();
                }
                else if(position==5){
                    //attendanceApprovalAttendanceTypeSpinnerID.setSelection(position);
                    loadMeetingFragment();
                }*/

                if(position==0){

                }else{
                    switch(position)
                    {
                        case 0:
                            //SetDropDownItemsForAttendanceType();
                            break;
                        case  1:
                            loadIntimeFragment();
                            //SetDropDownItemsForAttendanceType();
                            break;
                        case  2:
                            loadOuttimeFragment();
                            //SetDropDownItemsForAttendanceType();
                            break;
                        case  3:
                            loadWeeklyoffFragment();
                            //SetDropDownItemsForAttendanceType();
                            break;
                        case  4:
                            loadLeaveFragment();
                            //SetDropDownItemsForAttendanceType();
                            break;
                        case  5:
                            loadMeetingFragment();
                            //SetDropDownItemsForAttendanceType();
                            break;
                    }


                }//else


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        attendanceapprovaltopbarbackImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarbackImageViewID);
        attendanceapprovaltopbarusericonImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarusericonImageViewID);
        //attendanceApprovalAttendanceTypeSpinnerID = (Spinner)findViewById(R.id.attendanceApprovalAttendanceTypeSpinnerID);
        attendanceApprovalAttendanceTypeSpinnerID = (AppCompatSpinner)findViewById(R.id.attendanceApprovalAttendanceTypeSpinnerID);
        containerViewID = (LinearLayout)findViewById(R.id.containerViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(AttendanceApprovalRevisedActivity.this);

    }

    private void SetDropDownItemsForAttendanceType(){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AttendanceApprovalRevisedActivity.this, android.R.layout.simple_spinner_dropdown_item, ATTENDANCE_TYPE);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attendanceApprovalAttendanceTypeSpinnerID.setAdapter(null);
        attendanceApprovalAttendanceTypeSpinnerID.setAdapter(dataAdapter);
        attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
    }

    //In time Attendance
    private void loadIntimeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        AttendanceApprovalIntimeFragment inTime = new AttendanceApprovalIntimeFragment();
        fmt.replace(R.id.containerViewID,inTime,"IntimeFragment");
        fmt.commit();
        /*attendanceApprovalAttendanceTypeSpinnerID.post(new Runnable() {
            public void run() {
                attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
            }
        });*/
    }
    //Out time Attendance
    private void loadOuttimeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        AttendanceApprovalOuttimeFragment inTime1 = new AttendanceApprovalOuttimeFragment();
        fmt.replace(R.id.containerViewID,inTime1,"OuttimeFragment");
        fmt.commit();
        /*attendanceApprovalAttendanceTypeSpinnerID.post(new Runnable() {
            public void run() {
                attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
            }
        });*/
    }
    //Weekly off Attendance
    private void loadWeeklyoffFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        AttendanceApprovalWeeklyOffFragment inTime2 = new AttendanceApprovalWeeklyOffFragment();
        fmt.replace(R.id.containerViewID,inTime2,"WeeklyoffFragment");
        fmt.commit();
        /*attendanceApprovalAttendanceTypeSpinnerID.post(new Runnable() {
            public void run() {
                attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
            }
        });*/
    }
    //Leave Attendance
    private void loadLeaveFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        AttendanceApprovalLeaveFragment inTime3 = new AttendanceApprovalLeaveFragment();
        fmt.replace(R.id.containerViewID,inTime3,"LeaveFragment");
        fmt.commit();
        /*attendanceApprovalAttendanceTypeSpinnerID.post(new Runnable() {
            public void run() {
                attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
            }
        });*/
    }
    //Meeting Attendance
    private void loadMeetingFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        AttendanceApprovalMeetingFragment inTime4 = new AttendanceApprovalMeetingFragment();
        fmt.replace(R.id.containerViewID,inTime4,"MeetingFragment");
        fmt.commit();
        /*attendanceApprovalAttendanceTypeSpinnerID.post(new Runnable() {
            public void run() {
                attendanceApprovalAttendanceTypeSpinnerID.setSelection(pos);
            }
        });*/
    }

    @Override
    public void onBackPressed()
    {
        AttendanceApprovalRevisedActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}//Main Class
