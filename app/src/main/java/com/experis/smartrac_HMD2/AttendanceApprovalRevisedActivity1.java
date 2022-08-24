package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AttendanceApprovalRevisedActivity1 extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView attendanceapprovaltopbarbackImageViewID;
    private ImageView attendanceapprovaltopbarusericonImageViewID;

    private Button attendanceApprovalIntimeBtnID;
    private Button attendanceApprovalOuttimeBtnID;
    private Button attendanceApprovalWeeklyoffBtnID;
    private Button attendanceApprovalLeaveBtnID;
    private Button attendanceApprovalMeetingBtnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_approval_revised1);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.attendance_approval_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        attendanceapprovaltopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attendanceapprovaltopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        attendanceapprovaltopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        /*//Intime
        attendanceApprovalIntimeBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,Attendance_Approval_Revised_Content_Activity1.class);
                i.putExtra("TYPE","INTIME");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        //OutTime
        attendanceApprovalOuttimeBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,Attendance_Approval_Revised_Content_Activity1.class);
                i.putExtra("TYPE","OUTTIME");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });*/

        //In & Out Time Attendance
        attendanceApprovalIntimeBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,InOutTime_ApprovalActivity.class);
                //i.putExtra("TYPE","INTIME");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //Weeklyoff
    /*    attendanceApprovalWeeklyoffBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,Attendance_Approval_Revised_Content_Activity1.class);
                i.putExtra("TYPE","WEEKLYOFF");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        }); */
        //Leave
        attendanceApprovalLeaveBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,Attendance_Approval_Revised_Content_Activity1.class);
                i.putExtra("TYPE","LEAVE");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        //Meeting
        attendanceApprovalMeetingBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AttendanceApprovalRevisedActivity1.this,Attendance_Approval_Revised_Content_Activity1.class);
                i.putExtra("TYPE","MEETING");
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });


    }

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        attendanceapprovaltopbarbackImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarbackImageViewID);
        attendanceapprovaltopbarusericonImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarusericonImageViewID);

        attendanceApprovalIntimeBtnID = (Button)findViewById(R.id.attendanceApprovalIntimeBtnID);
        attendanceApprovalOuttimeBtnID = (Button) findViewById(R.id.attendanceApprovalOuttimeBtnID);
        attendanceApprovalWeeklyoffBtnID = (Button) findViewById(R.id.attendanceApprovalWeeklyoffBtnID);
        attendanceApprovalLeaveBtnID = (Button) findViewById(R.id.attendanceApprovalLeaveBtnID);
        attendanceApprovalMeetingBtnID = (Button) findViewById(R.id.attendanceApprovalMeetingBtnID);

        //Progress Dialog
        progressDialog = new ProgressDialog(AttendanceApprovalRevisedActivity1.this);

    }

    @Override
    public void onBackPressed()
    {
        AttendanceApprovalRevisedActivity1.this.finish();
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
