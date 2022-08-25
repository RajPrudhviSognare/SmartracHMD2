package com.experis.smartrac_HMD2;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView dashboardTopbarCompanyLogoImageViewID, dashboardtopbarsosImageViewID;
    private TextView dashboardTopCompanyAddressTextViewID;
    private TextView dashboardTopUserNameTextViewID;

    private GridView dashBoardForAssociatesGridViewID;
    private GridView dashBoardForTLGridViewID;
    private GridView dashBoardForSMGridViewID;
    private GridView dashBoardForSOOGridViewID;
    private GridView dashBoardForAssetManagerGridViewID;

    private ImageView dashboardtopbarusericonImageViewID;
    private ImageView dashboardtopbarLocationImageViewID;
    private ImageView dashboardtopbarLogouticonImageViewID;

    private RestFullClient client;
    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";
    private String TAG_LEAVESTATUS = "leave_status";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";
    private String LEAVESTATUS = "false";

    ////////////////////////////////////////////////
    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JOBJECT_USER = "user";
    private String TAG_JOBJECT_TEAM_LEAD = "team_lead";

    private JSONObject JOBJECT_DATA = null;
    private JSONObject JOBJECT_USER = null;
    private JSONObject JOBJECT_TEAM_LEAD = null;

    //USER DETAILS TAGS
    private String TAG_USERID = "id";
    private String TAG_USERISDCODE = "isd_code";
    private String TAG_USERREGIONID = "region_id";
    private String TAG_USERSTATEID = "state_id";
    private String TAG_USERCITYID = "city_id";
    private String TAG_USERBRANCHID = "branch_id";
    private String TAG_USEROUTLETID = "outlet_id";
    private String TAG_USERTLID = "tl_id";
    private String TAG_USERFIRSTNAME = "first_name";
    private String TAG_USERLASTNAME = "last_name";
    private String TAG_USERMOBILENUMBER = "mobile_number";
    private String TAG_USEREMAILID = "email";
    private String TAG_USERGENDER = "gender";
    private String TAG_USERISDLEVEL = "isd_level";
    private String TAG_USERROLEID = "role_id";
    private String TAG_USERSTATUS = "status";
    private String TAG_USERREGIONNAME = "region_name";
    private String TAG_USERSTATENAME = "state_name";
    private String TAG_USERCITYNAME = "city_name";
    private String TAG_USERBRANCHNAME = "branch_name";
    private String TAG_USERROLE = "role";
    private String TAG_USERISDLEVELNAME = "isd_level_name";
    private String TAG_USEROUTLETCODE = "outlet_code";
    private String TAG_USEROUTLETNAME = "outlet_name";
    private String TAG_USEROUTLETADDRESS = "address";
    private String TAG_USEROUTLETLATITUDE = "latitude";
    private String TAG_USEROUTLETLONGITUDE = "longitude";
    private String TAG_USEROUTLETGPSRANGE = "gps_range";
    private String TAG_USERCLIENTID = "client_id";
    private String TAG_USERCLIENTCODE = "client_code";
    private String TAG_USERCLIENTNAME = "client_name";
    private String TAG_USERCLIENTLOGO = "logo";
    private String TAG_KYC_STATUS = "kyc_status";


    //TL DETAILS TAGS
    private String TAG_TLID = "id";
    private String TAG_TLISDCODE = "isd_code";
    private String TAG_TLOUTLETID = "outlet_id";
    private String TAG_TLFIRSTNAME = "first_name";
    private String TAG_TLLASTNAME = "last_name";
    private String TAG_TLMOBILENUMBER = "mobile_number";
    private String TAG_TLEMAILID = "email";

    //USER DETAILS
    private String USERID = "";
    private String USERISDCODE = "";
    private String USERREGIONID = "";
    private String USERSTATEID = "";
    private String USERCITYID = "";
    private String USERBRANCHID = "";
    private String USEROUTLETID = "";
    private String USERTLID = "";
    private String USERFIRSTNAME = "";
    private String USERLASTNAME = "";
    private String USERMOBILENUMBER = "";
    private String USEREMAILID = "";
    private String USERGENDER = "";
    private String USERISDLEVEL = "";
    private String USERROLEID = "";
    private String USERSTATUS = "";
    private String USERREGIONNAME = "";
    private String USERSTATENAME = "";
    private String USERCITYNAME = "";
    private String USERBRANCHNAME = "";
    private String USERROLE = "";
    private String USERISDLEVELNAME = "";
    private String USEROUTLETCODE = "";
    private String USEROUTLETNAME = "";
    private String USEROUTLETADDRESS = "";
    private String USEROUTLETLATITUDE = "";
    private String USEROUTLETLONGITUDE = "";
    private String USEROUTLETGPSRANGE = "";
    private String USERCLIENTID = "";
    private String USERCLIENTCODE = "";
    private String USERCLIENTNAME = "";
    private String USERCLIENTLOGO = "";
    private String KYC_STATUS = "no";

    //TL DETAILS
    private String TLID = "";
    private String TLISDCODE = "";
    private String TLOUTLETID = "";
    private String TLFIRSTNAME = "";
    private String TLLASTNAME = "";
    private String TLMOBILENUMBER = "";
    private String TLEMAILID = "";
    /////////////////////////////////////////

    private ImageLoader imageLoader = null;

    private String URL1 = "https://manpoweronline.in";

    private String ROLE_TYPE = "Associate"; //Associates, TeamLead, StoreManager
    //private String ROLE_TYPE = "TeamLead"; //Associates, TeamLead, StoreManager
    //private String ROLE_TYPE = "StoreManager"; //Associates, TeamLead, StoreManager

    private int ROLE_ID; //1=>Associate, 2=>Teamlead, 3=>Admin, 4==>SOO, 6==>Asset Manager
    private String COMPANY_ADDRESS = "";
    private String COMPANY_OUTLETNAME = "";
    private String COMPANY_LOGO_PATH = "";
    private String COMPANY_LOGO_NAME = "";

    private int dashIconsForTL[] = {
//            R.drawable.dashboardicon,
//            R.drawable.attendanceapprovalicon,
//            R.drawable.associatereporticon,
//            R.drawable.sendmessagesicon,
//            R.drawable.sentmessagesicon,
    };

    private int dashIconsForSM[] = {
//            R.drawable.attendanceapprovalicon,
//            R.drawable.targeticon,
//            R.drawable.managestockicon,
//            R.drawable.associatereporticon,
//            R.drawable.sendmessagesicon,
//            R.drawable.sentmessagesicon
    };

    private int dashIconsForAssociates[] = {
            R.drawable.sales,
            R.drawable.dashboardicon,
//            R.drawable.attendanceicon,
//            R.drawable.hrmsicon,
//            R.drawable.messagesicon,
//            R.drawable.associatereporticon,
    };

    private int dashIconsForSOO[] = {
            R.drawable.dashboardicon,
            R.drawable.attendanceapprovalicon,
            R.drawable.associatereporticon
    };

    private int dashIconsForAssetManager[] = {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.dashboard_topbar_title);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dash Board");
        setSupportActionBar(toolbar);
        */

        UpdateApp();
        //Initialize all views ID & variables
        initAllViews();

        /***Set Username & Outlet Details***/
        getAnsSetValuesFromPreference();

        /***Creating an Image Directory for storing Selfie Pictures Temporarily***/
        createImageDirectory();
        startGeolocationService(getApplicationContext());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            System.out.println("Inside GetCurrentLocationActivity Page checkAllPermissions() is called Above Lallipop: ");
            try {
                checkAllPermissions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (CommonUtils.locationServicesEnabled(DashBoardActivity.this)) {

        } else {
            //Toast.makeText(GiveAttendanceTabbedActivity.this, "Please TURN ON Your Mobile's 'GPS' (Location Settings)", Toast.LENGTH_LONG).show();
            final AlertDialog.Builder aldb = new AlertDialog.Builder(DashBoardActivity.this);
            aldb.setTitle("Location Error!");
            aldb.setMessage("Please TURN ON Your Mobile's 'GPS' (Location Settings)");
            aldb.setCancelable(false);
            aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressed();
                }
            });
        }

        ////Added later for TL PUSH
        if (Constants.FROM_PUSH && Constants.PUSH_FOR.equalsIgnoreCase("")) {
            Intent i = new Intent(DashBoardActivity.this, AttendanceApprovalRevisedActivity1.class);
            Constants.FROM_PUSH = false;
            Constants.PUSH_FOR = "";
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        ////
        ////Added later for Associate PUSH
        if (Constants.FROM_PUSH && Constants.PUSH_FOR.equalsIgnoreCase("message")) {
            Intent i = new Intent(DashBoardActivity.this, MessagesForAssociateActivity.class);
            Constants.FROM_PUSH = false;
            Constants.PUSH_FOR = "";
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        ////

        /***Dash Board for Associates***/
        //if (ROLE_TYPE.equalsIgnoreCase("Associates")) {
        if (ROLE_ID == 1) {
            dashBoardForAssociatesGridViewID.setVisibility(View.VISIBLE);
            dashBoardForTLGridViewID.setVisibility(View.GONE);
            dashBoardForSMGridViewID.setVisibility(View.GONE);
            dashBoardForSOOGridViewID.setVisibility(View.GONE);
            dashBoardForAssetManagerGridViewID.setVisibility(View.GONE);
            dashboardtopbarLocationImageViewID.setVisibility(View.VISIBLE);
            dashBoardForAssociatesGridViewID.setAdapter(new CustomAdapterForAssociates(DashBoardActivity.this));
        }

        /***Dash Board for TeamLead***/
        //if (ROLE_TYPE.equalsIgnoreCase("TeamLead")) {
        if (ROLE_ID == 2) {
            dashBoardForAssociatesGridViewID.setVisibility(View.GONE);
            dashBoardForTLGridViewID.setVisibility(View.VISIBLE);
            dashBoardForSMGridViewID.setVisibility(View.GONE);
            dashBoardForSOOGridViewID.setVisibility(View.GONE);
            dashBoardForAssetManagerGridViewID.setVisibility(View.GONE);
            dashboardtopbarLocationImageViewID.setVisibility(View.VISIBLE);
            dashBoardForTLGridViewID.setAdapter(new CustomAdapterForTL(DashBoardActivity.this));
        }

        /***Dash Board for StoreManager***/
        //if (ROLE_TYPE.equalsIgnoreCase("StoreManager")) {
        if (ROLE_ID == 3) {
            dashBoardForAssociatesGridViewID.setVisibility(View.GONE);
            dashBoardForTLGridViewID.setVisibility(View.GONE);
            dashBoardForSMGridViewID.setVisibility(View.VISIBLE);
            dashBoardForSOOGridViewID.setVisibility(View.GONE);
            dashBoardForAssetManagerGridViewID.setVisibility(View.GONE);
            dashboardtopbarLocationImageViewID.setVisibility(View.VISIBLE);
            dashBoardForSMGridViewID.setAdapter(new CustomAdapterForSM(DashBoardActivity.this));
        }

        /***Dash Board for SOO***/
        //if (ROLE_TYPE.equalsIgnoreCase("SOO")) {
        if (ROLE_ID == 4) {
            dashBoardForAssociatesGridViewID.setVisibility(View.GONE);
            dashBoardForTLGridViewID.setVisibility(View.GONE);
            dashBoardForSMGridViewID.setVisibility(View.GONE);
            dashBoardForSOOGridViewID.setVisibility(View.VISIBLE);
            dashBoardForAssetManagerGridViewID.setVisibility(View.GONE);
            dashboardtopbarLocationImageViewID.setVisibility(View.VISIBLE);
            dashBoardForSOOGridViewID.setAdapter(new CustomAdapterForSOO(DashBoardActivity.this));
        }

        /***Dash Board for Asset Manager***/
        //if (ROLE_TYPE.equalsIgnoreCase("AM")) {
        if (ROLE_ID == 6) {
            dashBoardForAssociatesGridViewID.setVisibility(View.GONE);
            dashBoardForTLGridViewID.setVisibility(View.GONE);
            dashBoardForSMGridViewID.setVisibility(View.GONE);
            dashBoardForSOOGridViewID.setVisibility(View.GONE);
            dashBoardForAssetManagerGridViewID.setVisibility(View.VISIBLE);
            dashboardtopbarLocationImageViewID.setVisibility(View.VISIBLE);
            dashBoardForAssetManagerGridViewID.setAdapter(new CustomAdapterForAssetManager(DashBoardActivity.this));
        }

        /***GridView Options Click Listener For Associates***/
        dashBoardForAssociatesGridViewID.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    Intent i = new Intent(DashBoardActivity.this, SalesReport.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                if (pos == 1) {
                    Intent i = new Intent(DashBoardActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
//                //Attendance
//                if (pos == 0) {
//                    if (LEAVESTATUS.equalsIgnoreCase("true")) {
//                        showOptionDisableDialog();
//                    }
//                    if (LEAVESTATUS.equalsIgnoreCase("false")) {
//                        Intent i = new Intent(DashBoardActivity.this, GiveAttendanceTabbedActivity.class);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                    }
//                }
//                //HRMS
//                if (pos == 1) {
//                    Intent i = new Intent(DashBoardActivity.this, HRMSTabbedActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                }
//                //Send Messages
//                if (pos == 2) {
//                    Intent i = new Intent(DashBoardActivity.this, MessagesForAssociateActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                }
//                //Associates Reports
//                if (pos == 3) {
//                    Intent i = new Intent(DashBoardActivity.this, AssociateReportsTabbedActivity.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
//                }
//                //Receive Asset
//                if (pos == 4) {
//
//                    sendmessage();
//                }
//
            }

        });
        /***End Of GridView Options Click Listener For Associates***/

        /***GridView Options Click Listener For TeamLead***/
        dashBoardForTLGridViewID.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                //Dash Board
                if (pos == 0) {
                    /*if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,DashboardAttendanceCountActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        //Toast.makeText(DashBoardActivity.this,"Coming Soon!",Toast.LENGTH_SHORT).show();
                    }*/

                    Intent i = new Intent(DashBoardActivity.this, DashboardAttendanceCountActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                }
                //Attendance
          /*      if(pos==1){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,GiveAttendanceTabbedActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                } */
                //Attendance Approval
                if (pos == 1) {
                    Intent i = new Intent(DashBoardActivity.this, AttendanceApprovalRevisedActivity1.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Receive Asset
               /* if(pos==3){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,ReceiveAssetByTLSOOActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
                //Distribute Asset
                if(pos==4){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,DistributeAssetActivity1.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
                //Target setting
                if(pos==3){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,TargetSettingActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
                //Manage Daily Stock
                if(pos==4){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,ManageStocksActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }*/
                //Associates Reports
                if (pos == 2) {
                    Intent i = new Intent(DashBoardActivity.this, AssociateReportsTabbedTLActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //HRMS
          /*      if(pos==3){
                    Intent i = new Intent(DashBoardActivity.this,HRMSTabbedActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }

           */
                //Sending Message
                if (pos == 3) {
                    Intent i = new Intent(DashBoardActivity.this, MessagesForTLActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Sent Messages
                if (pos == 4) {
                    Intent i = new Intent(DashBoardActivity.this, MessagesSentForTLActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
             /*   if(pos==6){

                    sendmessage();
                }
*/


            }

        });
        /***End Of GridView Options Click Listener For TeamLead***/

        /***GridView Options Click Listener For StoreManager***/
        dashBoardForSMGridViewID.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                //Attendance Approval
                if (pos == 0) {
                    Intent i = new Intent(DashBoardActivity.this, ApproveAttendanceTabbedActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Target setting
                if (pos == 1) {
                    Toast.makeText(DashBoardActivity.this, "This Feature is Under Development!", Toast.LENGTH_SHORT).show();
                }
                //Manage Daily Stock
                if (pos == 2) {
                    Toast.makeText(DashBoardActivity.this, "This Feature is Under Development!", Toast.LENGTH_SHORT).show();
                }
                //Sales/Stock Tracking
                if (pos == 3) {
                    Toast.makeText(DashBoardActivity.this, "This Feature is Under Development!", Toast.LENGTH_SHORT).show();
                }
                //Competition Sales Tracking
                if (pos == 4) {
                    Toast.makeText(DashBoardActivity.this, "This Feature is Under Development!", Toast.LENGTH_SHORT).show();
                }
                //Associates Reports
                if (pos == 5) {
                    Toast.makeText(DashBoardActivity.this, "This Feature is Under Development!", Toast.LENGTH_SHORT).show();
                }
                if (pos == 6) {
                    Intent i = new Intent(DashBoardActivity.this, MessagesForTLActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                if (pos == 7) {
                    Intent i = new Intent(DashBoardActivity.this, MessagesSentForTLActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }

            }

        });
        /***End Of GridView Options Click Listener For StoreManager***/

        /***GridView Options Click Listener For SOO***/
        dashBoardForSOOGridViewID.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                //Dash Board
                if (pos == 0) {
                    /*if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,DashboardAttendanceCountActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                        //Toast.makeText(DashBoardActivity.this,"Coming Soon!",Toast.LENGTH_SHORT).show();
                    }*/

                    Intent i = new Intent(DashBoardActivity.this, DashboardAttendanceCountActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                }
                //Attendance Approval
                if (pos == 1) {
                    Intent i = new Intent(DashBoardActivity.this, AttendanceApprovalRevisedActivity1.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Associates Reports
                if (pos == 2) {
                    Intent i = new Intent(DashBoardActivity.this, AssociateReportsTabbedActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Receive Asset
               /* if(pos==3){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,ReceiveAssetByTLSOOActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
                //Distribute Asset
                if(pos==4){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,DistributeAssetActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }*/
                //Target setting
                /*if(pos==5){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,TargetSettingActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }*/
                //Manage Daily Stock
                /*if(pos==6){
                    if(LEAVESTATUS.equalsIgnoreCase("true")){
                        showOptionDisableDialog();
                    }
                    if(LEAVESTATUS.equalsIgnoreCase("false")){
                        Intent i = new Intent(DashBoardActivity.this,ManageStocksActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }*/

            }

        });
        /***End Of GridView Options Click Listener For SOO***/

        /***GridView Options Click Listener For Asset Manager***/
        dashBoardForAssetManagerGridViewID.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                //Receive Asset
                if (pos == 0) {
                    if (LEAVESTATUS.equalsIgnoreCase("true")) {
                        showOptionDisableDialog();
                    }
                    if (LEAVESTATUS.equalsIgnoreCase("false")) {
                        Intent i = new Intent(DashBoardActivity.this, ReceiveAssetByManagerActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
                //Distribute Asset
                if (pos == 1) {
                    if (LEAVESTATUS.equalsIgnoreCase("true")) {
                        showOptionDisableDialog();
                    }
                    if (LEAVESTATUS.equalsIgnoreCase("false")) {
                        Intent i = new Intent(DashBoardActivity.this, DistributeAssetActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                }
            }

        });
        /***End Of GridView Options Click Listener For Asset Manager***/

        dashboardtopbarsosImageViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, SOSActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //User Icon Click Event
        dashboardtopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* LayoutInflater inflater = (LayoutInflater) DashBoardActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(
                        R.layout.overflow_layout, null, false), 300, 400, true);
                pw.showAtLocation(findViewById(R.id.container), Gravity.CLIP_HORIZONTAL, 0,
                        0);

                 TextView loc=(TextView)findViewById(R.id.menuItem1);
                TextView changepwd=(TextView)findViewById(R.id.menuItem2);
                TextView sos=(TextView)findViewById(R.id.menuItem3);


               loc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DashBoardActivity.this,GetCurrentLocationActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }
                });
                changepwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DashBoardActivity.this,ChangePasswordActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }
                });
                sos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(DashBoardActivity.this,SOSActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }
                });*/

                Intent i = new Intent(DashBoardActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //Topbar Map/Location Icon
        dashboardtopbarLocationImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashBoardActivity.this, GetCurrentLocationActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //Logout Icon Click Logic
        dashboardtopbarLogouticonImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsEditor.putString("LOGGEDIN", "no");
                prefsEditor.commit();

                deleteAllSelfies();

                Toast.makeText(DashBoardActivity.this, "You have been Logged out successfully!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
                DashBoardActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        //Added Later//////////////////
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            System.out.println("Inside DashBoardActivity Page checkAllPermissions() is called Above Lallipop: ");
            try {
                checkAllPermissions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //End Of Added Later//////////

    }//onCreate()

    private void sendmessage() {
        String lat = Constants.UNIV_LAT1;
        String lon = Constants.UNIV_LONG1;
        ArrayList<String> numbers = new ArrayList<String>(0);
        numbers.add(prefs.getString("DEFAULTSOS", ""));
        numbers.add(prefs.getString("FIRSTSOS", ""));
        numbers.add(prefs.getString("SECONDSOS", ""));
        numbers.add(prefs.getString("THIRDSOS", ""));

        Toast.makeText(getApplicationContext(), "Emergency message sent \nLat: " + lat + "\n Long: " + lon,
                Toast.LENGTH_LONG).show();
        SmsManager smsManager = SmsManager.getDefault();

        for (int i = 0; i < numbers.size(); i++) {

            if (!numbers.get(i).equalsIgnoreCase("")) {

                smsManager.sendTextMessage(numbers.get(i), null,
                        "Please Help I am here.. http://www.google.com/maps/place/" + lat + "," + lon, null, null);
            }
        }


    }

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        ////Added Later
        Constants.UNIV_LAT = prefs.getString("USEROUTLETLATITUDE", "0.0");
        Constants.UNIV_LONG = prefs.getString("USEROUTLETLONGITUDE", "0.0");
        Constants.UNIV_RADIUS = prefs.getString("USEROUTLETGPSRANGE", "500.0");

        if (Constants.UNIV_RADIUS.equalsIgnoreCase("0.0")) {
            Constants.UNIV_RADIUS = "500.0";
        }

        System.out.println("Constants.UNIV_LAT: " + Constants.UNIV_LAT);
        System.out.println("Constants.UNIV_LONG: " + Constants.UNIV_LONG);
        System.out.println("Constants.UNIV_RADIUS: " + Constants.UNIV_RADIUS);
        ////Added Later

        dashboardTopbarCompanyLogoImageViewID = (ImageView) findViewById(R.id.dashboardTopbarCompanyLogoImageViewID);
        dashboardTopCompanyAddressTextViewID = (TextView) findViewById(R.id.dashboardTopCompanyAddressTextViewID);
        dashboardTopUserNameTextViewID = (TextView) findViewById(R.id.dashboardTopUserNameTextViewID);

        dashBoardForAssociatesGridViewID = (GridView) findViewById(R.id.dashBoardForAssociatesGridViewID);
        dashBoardForTLGridViewID = (GridView) findViewById(R.id.dashBoardForTLGridViewID);
        dashBoardForSMGridViewID = (GridView) findViewById(R.id.dashBoardForSMGridViewID);
        dashBoardForSOOGridViewID = (GridView) findViewById(R.id.dashBoardForSOOGridViewID);
        dashBoardForAssetManagerGridViewID = (GridView) findViewById(R.id.dashBoardForAssetManagerGridViewID);

        dashboardtopbarusericonImageViewID = (ImageView) findViewById(R.id.dashboardtopbarusericonImageViewID);
        dashboardtopbarLocationImageViewID = (ImageView) findViewById(R.id.dashboardtopbarLocationImageViewID);

        dashboardtopbarLogouticonImageViewID = (ImageView) findViewById(R.id.dashboardtopbarLogouticonImageViewID);
        dashboardtopbarsosImageViewID = (ImageView) findViewById(R.id.dashboardtopbarsosImageViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(DashBoardActivity.this);

        imageLoader = new ImageLoader(DashBoardActivity.this);
    }

    private void getAnsSetValuesFromPreference() {
        //ROLE_TYPE = prefs.getString("")
        ROLE_TYPE = prefs.getString("USERROLEID", "");
        if (ROLE_TYPE != null || !ROLE_TYPE.equals("")) {
            try {
                ROLE_ID = Integer.parseInt(ROLE_TYPE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ROLE_ID IN DASHBOARD: " + ROLE_ID);
        }

        COMPANY_ADDRESS = prefs.getString("USEROUTLETADDRESS", "");
        COMPANY_OUTLETNAME = prefs.getString("USEROUTLETNAME", "");

        System.out.println("USER NAME IN DASHBOARD: " + prefs.getString("USERFIRSTNAME", "") + " " + prefs.getString("USERLASTNAME", "") + " (" + prefs.getString("USERISDCODE", "") + ")");
        dashboardTopUserNameTextViewID.setText("");
        dashboardTopUserNameTextViewID.setText("Hello, " + prefs.getString("USERFIRSTNAME", "") + " " + prefs.getString("USERLASTNAME", "") + " (" + prefs.getString("USERISDCODE", "") + ")");

        System.out.println("COMPANY_ADDRESS IN DASHBOARD: " + COMPANY_ADDRESS);

        if (COMPANY_OUTLETNAME.equalsIgnoreCase("null")) {
            COMPANY_OUTLETNAME = "";
        }
        if (COMPANY_ADDRESS.equalsIgnoreCase("null")) {
            COMPANY_ADDRESS = "";
        }

        if (!COMPANY_OUTLETNAME.equalsIgnoreCase("") && !COMPANY_ADDRESS.equalsIgnoreCase("")) {
            dashboardTopCompanyAddressTextViewID.setText("");
            dashboardTopCompanyAddressTextViewID.setText("Outlet: " + COMPANY_OUTLETNAME + ", " + COMPANY_ADDRESS);
        } else {
            dashboardTopCompanyAddressTextViewID.setText("");
            dashboardTopCompanyAddressTextViewID.setText("Outlet: " + COMPANY_OUTLETNAME + "  " + COMPANY_ADDRESS);
        }

        COMPANY_LOGO_NAME = prefs.getString("USERCLIENTLOGO", "");
        System.out.println("COMPANY_LOGO_NAME IN DASHBOARD: " + COMPANY_LOGO_NAME);

        COMPANY_LOGO_PATH = Constants.BASE_URL_CLIENT_LOGO + COMPANY_LOGO_NAME;
        System.out.println("COMPANY_LOGO_PATH IN DASHBOARD: " + COMPANY_LOGO_PATH);

        imageLoader.DisplayImage(COMPANY_LOGO_PATH, dashboardTopbarCompanyLogoImageViewID);

    }

    //Creating an Image Directory for storing Self Pictures
    private void createImageDirectory() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Self_Pictures");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void checkLeaveStatus() {

        progressDialog.setMessage("Updating Credentials... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL + Constants.LEAVESTATUS_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID", ""));

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    client.Execute(1); //POST Request
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                receiveDataForServerResponse(client.jObj);
                handler.sendEmptyMessage(0);

            }

        }).start();

    }

    private void receiveDataForServerResponse(JSONObject jobj) {

        try {

            if (client.responseCode == 200) {

                STATUS = jobj.getString(TAG_STATUS);
                MESSAGE = jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: " + STATUS);
                System.out.println("MESSAGE: responseCode==200: " + MESSAGE);

                if (STATUS.equalsIgnoreCase("true")) {
                    LEAVESTATUS = jobj.getString(TAG_LEAVESTATUS);
                    System.out.println("LEAVESTATUS: responseCode==200: & STATUS==true: " + LEAVESTATUS);


                    JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                    System.out.println("JOBJECT_DATA: " + JOBJECT_DATA.toString());

                    if (JOBJECT_DATA != null) {

                        if (JOBJECT_DATA.has(TAG_JOBJECT_USER)) {
                            JOBJECT_USER = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_USER);
                            System.out.println("JOBJECT_USER: " + JOBJECT_USER.toString());

                            //User Details
                            USERID = JOBJECT_USER.getString(TAG_USERID);
                            System.out.println("USERID: " + USERID);
                            USERISDCODE = JOBJECT_USER.getString(TAG_USERISDCODE);
                            System.out.println("USERISDCODE " + USERISDCODE);
                            USERREGIONID = JOBJECT_USER.getString(TAG_USERREGIONID);
                            System.out.println("USERREGIONID: " + USERREGIONID);
                            USERSTATEID = JOBJECT_USER.getString(TAG_USERSTATEID);
                            System.out.println("USERSTATEID: " + USERSTATEID);
                            USERCITYID = JOBJECT_USER.getString(TAG_USERCITYID);
                            System.out.println("USERCITYID: " + USERCITYID);
                            USERBRANCHID = JOBJECT_USER.getString(TAG_USERBRANCHID);
                            System.out.println("USERBRANCHID: " + USERBRANCHID);
                            USEROUTLETID = JOBJECT_USER.getString(TAG_USEROUTLETID);
                            System.out.println("USEROUTLETID: " + USEROUTLETID);
                            USERTLID = JOBJECT_USER.getString(TAG_USERTLID);
                            System.out.println("USERTLID: " + USERTLID);
                            USERFIRSTNAME = JOBJECT_USER.getString(TAG_USERFIRSTNAME);
                            System.out.println("USERFIRSTNAME: " + USERFIRSTNAME);
                            USERLASTNAME = JOBJECT_USER.getString(TAG_USERLASTNAME);
                            System.out.println("USERLASTNAME: " + USERLASTNAME);
                            USERMOBILENUMBER = JOBJECT_USER.getString(TAG_USERMOBILENUMBER);
                            System.out.println("USERMOBILENUMBER: " + USERMOBILENUMBER);
                            USEREMAILID = JOBJECT_USER.getString(TAG_USEREMAILID);
                            System.out.println("USEREMAILID: " + USEREMAILID);
                            USERGENDER = JOBJECT_USER.getString(TAG_USERGENDER);
                            System.out.println("USERGENDER: " + USERGENDER);
                            USERISDLEVEL = JOBJECT_USER.getString(TAG_USERISDLEVEL);
                            System.out.println("USERISDLEVEL: " + USERISDLEVEL);
                            USERROLEID = JOBJECT_USER.getString(TAG_USERROLEID);
                            System.out.println("USERROLEID: " + USERROLEID);
                            USERSTATUS = JOBJECT_USER.getString(TAG_USERSTATUS);
                            System.out.println("USERSTATUS: " + USERSTATUS);
                            USERREGIONNAME = JOBJECT_USER.getString(TAG_USERREGIONNAME);
                            System.out.println("USERREGIONNAME: " + USERREGIONNAME);
                            USERSTATENAME = JOBJECT_USER.getString(TAG_USERSTATENAME);
                            System.out.println("USERSTATENAME: " + USERSTATENAME);
                            USERCITYNAME = JOBJECT_USER.getString(TAG_USERCITYNAME);
                            System.out.println("USERCITYNAME: " + USERCITYNAME);
                            USERBRANCHNAME = JOBJECT_USER.getString(TAG_USERBRANCHNAME);
                            System.out.println("USERBRANCHNAME: " + USERBRANCHNAME);
                            USERROLE = JOBJECT_USER.getString(TAG_USERROLE);
                            System.out.println("USERROLE: " + USERROLE);
                            USERISDLEVELNAME = JOBJECT_USER.getString(TAG_USERISDLEVELNAME);
                            System.out.println("USERISDLEVELNAME: " + USERISDLEVELNAME);
                            USEROUTLETCODE = JOBJECT_USER.getString(TAG_USEROUTLETCODE);
                            System.out.println("USEROUTLETCODE: " + USEROUTLETCODE);
                            USEROUTLETNAME = JOBJECT_USER.getString(TAG_USEROUTLETNAME);
                            System.out.println("USEROUTLETNAME: " + USEROUTLETNAME);
                            USEROUTLETADDRESS = JOBJECT_USER.getString(TAG_USEROUTLETADDRESS);
                            System.out.println("USEROUTLETADDRESS: " + USEROUTLETADDRESS);
                            USEROUTLETLATITUDE = JOBJECT_USER.getString(TAG_USEROUTLETLATITUDE);
                            System.out.println("USEROUTLETLATITUDE: " + USEROUTLETLATITUDE);
                            USEROUTLETLONGITUDE = JOBJECT_USER.getString(TAG_USEROUTLETLONGITUDE);
                            System.out.println("USEROUTLETLONGITUDE: " + USEROUTLETLONGITUDE);
                            USEROUTLETGPSRANGE = JOBJECT_USER.getString(TAG_USEROUTLETGPSRANGE);
                            System.out.println("USEROUTLETGPSRANGE: " + USEROUTLETGPSRANGE);
                            USERCLIENTID = JOBJECT_USER.getString(TAG_USERCLIENTID);
                            System.out.println("USERCLIENTID: " + USERCLIENTID);
                            USERCLIENTCODE = JOBJECT_USER.getString(TAG_USERCLIENTCODE);
                            System.out.println("USERCLIENTCODE: " + USERCLIENTCODE);
                            USERCLIENTNAME = JOBJECT_USER.getString(TAG_USERCLIENTNAME);
                            System.out.println("USERCLIENTNAME: " + USERCLIENTNAME);
                            USERCLIENTLOGO = JOBJECT_USER.getString(TAG_USERCLIENTLOGO);
                            System.out.println("USERCLIENTLOGO: " + USERCLIENTLOGO);
                            KYC_STATUS = JOBJECT_USER.getString(TAG_KYC_STATUS);
                            System.out.println("KYC_STATUS: " + KYC_STATUS);

                            if (USEROUTLETLATITUDE.equalsIgnoreCase("null") || USEROUTLETLATITUDE == null) {
                                USEROUTLETLATITUDE = "0.0";
                            }
                            if (USEROUTLETLONGITUDE.equalsIgnoreCase("null") || USEROUTLETLONGITUDE == null) {
                                USEROUTLETLONGITUDE = "0.0";
                            }
                            if (USEROUTLETGPSRANGE.equalsIgnoreCase("null") || USEROUTLETGPSRANGE == null) {
                                USEROUTLETGPSRANGE = "500.0";
                            }

                            Constants.UNIV_LAT = USEROUTLETLATITUDE;
                            Constants.UNIV_LONG = USEROUTLETLONGITUDE;
                            Constants.UNIV_RADIUS = USEROUTLETGPSRANGE;

                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_USER))

                        if (JOBJECT_DATA.has(TAG_JOBJECT_TEAM_LEAD)) {
                            JOBJECT_TEAM_LEAD = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_TEAM_LEAD);
                            System.out.println("JOBJECT_TEAM_LEAD: " + JOBJECT_TEAM_LEAD.toString());

                            //Team Lead Details
                            TLID = JOBJECT_TEAM_LEAD.getString(TAG_TLID);
                            System.out.println("TLID: " + TLID);
                            TLISDCODE = JOBJECT_TEAM_LEAD.getString(TAG_TLISDCODE);
                            System.out.println("TLISDCODE: " + TLISDCODE);
                            TLOUTLETID = JOBJECT_TEAM_LEAD.getString(TAG_TLOUTLETID);
                            System.out.println("TLOUTLETID: " + TLOUTLETID);
                            TLFIRSTNAME = JOBJECT_TEAM_LEAD.getString(TAG_TLFIRSTNAME);
                            System.out.println("TLFIRSTNAME: " + TLFIRSTNAME);
                            TLLASTNAME = JOBJECT_TEAM_LEAD.getString(TAG_TLLASTNAME);
                            System.out.println("TLLASTNAME: " + TLLASTNAME);
                            TLMOBILENUMBER = JOBJECT_TEAM_LEAD.getString(TAG_TLMOBILENUMBER);
                            System.out.println("TLMOBILENUMBER: " + TLMOBILENUMBER);
                            TLEMAILID = JOBJECT_TEAM_LEAD.getString(TAG_TLEMAILID);
                            System.out.println("TLEMAILID: " + TLEMAILID);

                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_TEAM_LEAD))

                    }//if(JOBJECT_DATA!=null){
                    else {
                        System.out.println("JOBJECT_DATA is Null");
                    }


                }//if(STATUS.equalsIgnoreCase("true"))


            }//if(client.responseCode==200)

            if (client.responseCode != 200) {

                STATUS = jobj.getString(TAG_STATUS);
                MESSAGE = jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode!=200: " + STATUS);
                System.out.println("MESSAGE: responseCode!=200: " + MESSAGE);

            }//if(client.responseCode!=200)

        } catch (Exception e) {
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }

            //Success
            if (client.responseCode == 200) {
                //Success
                if (STATUS.equalsIgnoreCase("true")) {
                    //showSuccessDialog();
                    //Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    saveUserDetails();
                }
            }
        }//handleMessage(Message msg)

    };

    private void saveUserDetails() {

        //USER DETAILS
        prefsEditor.putString("USERID", USERID);
        prefsEditor.putString("USERISDCODE", USERISDCODE);
        prefsEditor.putString("USERREGIONID", USERREGIONID);
        prefsEditor.putString("USERSTATEID", USERSTATEID);
        prefsEditor.putString("USERCITYID", USERCITYID);
        prefsEditor.putString("USERBRANCHID", USERBRANCHID);
        prefsEditor.putString("USEROUTLETID", USEROUTLETID);
        prefsEditor.putString("USERTLID", USERTLID);
        prefsEditor.putString("USERFIRSTNAME", USERFIRSTNAME);
        prefsEditor.putString("USERLASTNAME", USERLASTNAME);
        prefsEditor.putString("USERMOBILENUMBER", USERMOBILENUMBER);
        prefsEditor.putString("USEREMAILID", USEREMAILID);
        prefsEditor.putString("USERGENDER", USERGENDER);
        prefsEditor.putString("USERISDLEVEL", USERISDLEVEL);
        prefsEditor.putString("USERROLEID", USERROLEID);
        prefsEditor.putString("USERSTATUS", USERSTATUS);
        prefsEditor.putString("USERREGIONNAME", USERREGIONNAME);
        prefsEditor.putString("USERSTATENAME", USERSTATENAME);
        prefsEditor.putString("USERCITYNAME", USERCITYNAME);
        prefsEditor.putString("USERBRANCHNAME", USERBRANCHNAME);
        prefsEditor.putString("USERROLE", USERROLE);
        prefsEditor.putString("USERISDLEVELNAME", USERISDLEVELNAME);
        prefsEditor.putString("USEROUTLETCODE", USEROUTLETCODE);
        prefsEditor.putString("USEROUTLETNAME", USEROUTLETNAME);
        prefsEditor.putString("USEROUTLETADDRESS", USEROUTLETADDRESS);
        prefsEditor.putString("USEROUTLETLATITUDE", USEROUTLETLATITUDE);
        prefsEditor.putString("USEROUTLETLONGITUDE", USEROUTLETLONGITUDE);
        prefsEditor.putString("USEROUTLETGPSRANGE", USEROUTLETGPSRANGE);
        prefsEditor.putString("USERCLIENTID", USERCLIENTID);
        prefsEditor.putString("USERCLIENTCODE", USERCLIENTCODE);
        prefsEditor.putString("USERCLIENTNAME", USERCLIENTNAME);
        prefsEditor.putString("USERCLIENTLOGO", USERCLIENTLOGO);
        prefsEditor.putString("KYC_STATUS", KYC_STATUS);

        //TL DETAILS
        prefsEditor.putString("TLID", TLID);
        prefsEditor.putString("TLISDCODE", TLISDCODE);
        prefsEditor.putString("TLOUTLETID", TLOUTLETID);
        prefsEditor.putString("TLFIRSTNAME", TLFIRSTNAME);
        prefsEditor.putString("TLLASTNAME", TLLASTNAME);
        prefsEditor.putString("TLMOBILENUMBER", TLMOBILENUMBER);
        prefsEditor.putString("TLEMAILID", TLEMAILID);

        prefsEditor.commit();

        if (USERSTATUS.equalsIgnoreCase("1")) {
            //Initialize all views ID & variables
            initAllViews();

            //Set Username & Outlet Details
            getAnsSetValuesFromPreference();

            //onCreate(null);
        } else {
            showInactiveStatus();
        }

    }

    //Inactive Status Dialog
    public void showInactiveStatus() {

        final AlertDialog.Builder aldb = new AlertDialog.Builder(DashBoardActivity.this);
        aldb.setTitle("Smartrac");
        aldb.setMessage("You Profile is not Active anymore. Kindly consult with your TL. \nThank you!");
        aldb.setCancelable(false);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                prefsEditor.putString("LOGGEDIN", "no");
                prefsEditor.commit();

                deleteAllSelfies();

                Toast.makeText(DashBoardActivity.this, "You have been Logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
                DashBoardActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        aldb.show();

    }

    // Google play Store update 25-07-2022

    public void UpdateApp() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                requestUpdate(result);
                android.view.ContextThemeWrapper ctw = new android.view.ContextThemeWrapper(this, R.style.AppTheme);
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctw);
                alertDialogBuilder.setTitle("Update Your Smartrac");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setIcon(R.drawable.applogo1);
                alertDialogBuilder.setMessage("Smartrac recommends that you update to the latest version for a seamless & enhanced performance of the app.");
                alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialogBuilder.show();

            } else {

            }
        });
    }


    //--------------------------- end ----------------------------

    //CustomAdapterForAssociates
    public class CustomAdapterForAssociates extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForAssociates(Context context) {
            cntx = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dashIconsForAssociates.length;
            //return 8;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            //return 1;
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            ViewHolder viewHolder = new ViewHolder();

            GridView grid = (GridView) parent;
            //int sizeH = grid.getMinimumHeight();
            //int sizeW = grid.getMinimumWidth();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_dashboard, null);
                //view.setLayoutParams(new GridView.LayoutParams(sizeW, sizeH));
            } else {
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = pos;

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.customlayout_dashboard_LinearLayoutID);
            if (pos1 == 0) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 1) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 2) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 3) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 4) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 5) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 6) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 7) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 8) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }

            viewHolder.imageView = (ImageView) view.findViewById(R.id.customlayout_dashboard_ImageViewID);
            viewHolder.imageView.setImageResource(dashIconsForAssociates[pos]);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

    }//CustomAdapterForAssociates

    //CustomAdapterForTL
    public class CustomAdapterForTL extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForTL(Context context) {
            cntx = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dashIconsForTL.length;
            //return 8;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            //return 1;
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            ViewHolder viewHolder = new ViewHolder();

            GridView grid = (GridView) parent;
            //int sizeH = grid.getMinimumHeight();
            //int sizeW = grid.getMinimumWidth();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_dashboard, null);
                //view.setLayoutParams(new GridView.LayoutParams(sizeW, sizeH));
            } else {
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = pos;

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.customlayout_dashboard_LinearLayoutID);
            if (pos1 == 0) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 1) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 2) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 3) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 4) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 5) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 6) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 7) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 8) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 9) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 10) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }

            viewHolder.imageView = (ImageView) view.findViewById(R.id.customlayout_dashboard_ImageViewID);
            viewHolder.imageView.setImageResource(dashIconsForTL[pos]);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

    }//CustomAdapterForTL

    //CustomAdapterForSM
    public class CustomAdapterForSM extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForSM(Context context) {
            cntx = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dashIconsForSM.length;
            //return 8;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            //return 1;
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            ViewHolder viewHolder = new ViewHolder();

            GridView grid = (GridView) parent;
            //int sizeH = grid.getMinimumHeight();
            //int sizeW = grid.getMinimumWidth();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_dashboard, null);
                //view.setLayoutParams(new GridView.LayoutParams(sizeW, sizeH));
            } else {
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = pos;

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.customlayout_dashboard_LinearLayoutID);
            if (pos1 == 0) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 1) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 2) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 3) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 4) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 5) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 6) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 7) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 8) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 9) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }

            viewHolder.imageView = (ImageView) view.findViewById(R.id.customlayout_dashboard_ImageViewID);
            viewHolder.imageView.setImageResource(dashIconsForSM[pos]);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

    }//CustomAdapterForSM

    //CustomAdapterForSOO
    public class CustomAdapterForSOO extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForSOO(Context context) {
            cntx = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dashIconsForSOO.length;
            //return 8;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            //return 1;
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            ViewHolder viewHolder = new ViewHolder();

            GridView grid = (GridView) parent;
            //int sizeH = grid.getMinimumHeight();
            //int sizeW = grid.getMinimumWidth();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_dashboard, null);
                //view.setLayoutParams(new GridView.LayoutParams(sizeW, sizeH));
            } else {
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = pos;

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.customlayout_dashboard_LinearLayoutID);
            if (pos1 == 0) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 1) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 2) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            if (pos1 == 3) {
                layout.setBackgroundResource(R.drawable.bg_4);
            }
            if (pos1 == 4) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 5) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            if (pos1 == 6) {
                layout.setBackgroundResource(R.drawable.bg_3);
            }
            viewHolder.imageView = (ImageView) view.findViewById(R.id.customlayout_dashboard_ImageViewID);
            viewHolder.imageView.setImageResource(dashIconsForSOO[pos]);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

    }//CustomAdapterForSOO

    //CustomAdapterForAssetManager
    public class CustomAdapterForAssetManager extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForAssetManager(Context context) {
            cntx = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dashIconsForAssetManager.length;
            //return 8;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() != 0)
                return getCount();

            //return 1;
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            ViewHolder viewHolder = new ViewHolder();

            GridView grid = (GridView) parent;
            //int sizeH = grid.getMinimumHeight();
            //int sizeW = grid.getMinimumWidth();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_dashboard, null);
                //view.setLayoutParams(new GridView.LayoutParams(sizeW, sizeH));
            } else {
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = pos;

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.customlayout_dashboard_LinearLayoutID);
            if (pos1 == 0) {
                layout.setBackgroundResource(R.drawable.bg_1);
            }
            if (pos1 == 1) {
                layout.setBackgroundResource(R.drawable.bg_2);
            }
            viewHolder.imageView = (ImageView) view.findViewById(R.id.customlayout_dashboard_ImageViewID);
            viewHolder.imageView.setImageResource(dashIconsForAssetManager[pos]);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }

    }//CustomAdapterForAssetManager

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (ROLE_ID==1){
            //Checking For Leave Status
            checkLeaveStatus();
        }*/

        //Initialize all views ID & variables
        initAllViews();

        //Checking For Leave Status
        checkLeaveStatus();
    }

    @Override
    public void onBackPressed() {
        deleteAllSelfies();
        showLogoutDialog();
    }

    //Delete All Selfies
    private void deleteAllSelfies() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Self_Pictures");
        if (file.exists() && file.isDirectory()) {
            System.out.println("'Self_Pictures' Directory is found");
            File[] files = file.listFiles();
            if (files != null) {
                if (files.length != 0) {
                    System.out.println("Total Files Found: " + files.length);
                    //System.out.println("Inside if(files != null)");
                    System.out.println("Inside if(files.length!= 0)");
                    for (File f : files) {
                        try {
                            System.out.println("Deleted File is: " + f.getPath().toString());
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } else {
            System.out.println("'Self_Pictures' Directory is not found");
        }
    }

    //Display option disabled dialog for 'Leave'
    private void showOptionDisableDialog() {
        final AlertDialog.Builder aldb = new AlertDialog.Builder(DashBoardActivity.this);
        aldb.setMessage("This option is temporarily blocked because you are on 'Leave' today!");
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        aldb.show();
    }


    //Logout Dialog
    private void showLogoutDialog() {

        final AlertDialog.Builder aldb = new AlertDialog.Builder(DashBoardActivity.this);
        //aldb.setTitle("Exiting the app!");
        aldb.setMessage("Would you like to Logout?");
        aldb.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                prefsEditor.putString("LOGGEDIN", "no");
                prefsEditor.commit();

                Toast.makeText(DashBoardActivity.this, "You have been Logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashBoardActivity.this, LoginActivity.class);
                DashBoardActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.setPositiveButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        aldb.show();

    }

    private void checkAllPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                //     || ContextCompat.checkSelfPermission(DashBoardActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(DashBoardActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {

            /*// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashBoardActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(DashBoardActivity.this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.CAMERA}, 1991);
            }*/

            ActivityCompat.requestPermissions(DashBoardActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            //          android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.CAMERA}, 1991);

        }//if

    }//checkAllPermissions()

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1991: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // tasks you need to do.
                    System.out.println("ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATION ARE GRANTED!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATION ARE REJECTED!");
                }
            }

        }

    }

    @Override
    protected void onDestroy() {
        if (isServiceRunning("GeolocationService")) {
            System.out.println("GeolocationService is running!");
            stopGeolocationService(getApplicationContext());
        } else {
            System.out.println("GeolocationService is already destroyed!");
        }

        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }

        super.onDestroy();
    }


    public boolean isServiceRunning(String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    static public void startGeolocationService(Context context) {
        Intent geolocationService = new Intent(context,
                GeolocationService.class);
        context.startService(geolocationService);
    }

    static public void stopGeolocationService(Context context) {
        Intent geolocationService = new Intent(context,
                GeolocationService.class);
        context.stopService(geolocationService);
    }


}//Main Class
