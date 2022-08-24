package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

public class DashboardAttendanceCountActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView dashboardattendancecount_topbarbackImageViewID;

    private TextView DashboardAttendanceCountTotalUsersValueTextViewID;

    private TextView DashboardAttendanceCountFullDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountFullDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountFullDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountHalfDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountHalfDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountHalfDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountLeaveDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountLeaveDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountLeaveDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountMeetingDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountMeetingDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountMeetingDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountTrainingDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountTrainingDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountTrainingDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountWeeklyOffDayApprovedValueTextViewID;
    private TextView DashboardAttendanceCountWeeklyOffDayRejectedValueTextViewID;
    private TextView DashboardAttendanceCountWeeklyOffDayPendingValueTextViewID;

    private TextView DashboardAttendanceCountTOTALABSENTDayCountValueTextViewID;
    private TextView DashboardAttendanceCountTOTALAbscondingDayCountValueTextViewID;
    private TextView DashboardAttendanceCountTOTALNotAvailableDayCountValueTextViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JOBJECT_DATA1 = "dashboard_count";

    private JSONObject JOBJECT_DATA = null;
    private JSONObject JOBJECT_DATA1 = null;

    private String TAG_TOTAL_ASSOCIATES = "dashboard_associate_count";
    private String TAG_TOTAL_ABSCONDING = "consicutive_3days";
    private String TAG_TOTAL_NOTAVAILABLE = "not_available";

    private String TAG_APPROVE_FULLDAY = "approve_in";
    private String TAG_REJECT_FULLDAY = "rejected_in";
    private String TAG_PENDING_FULLDAY = "pending_in";

    private String TAG_APPROVE_HALFDAY = "approve_out";
    private String TAG_REJECT_HALFDAY = "rejected_out";
    private String TAG_PENDING_HALFDAY = "pending_out";

    private String TAG_APPROVE_LEAVE = "approve_leave";
    private String TAG_REJECT_LEAVE = "rejected_leave";
    private String TAG_PENDING_LEAVE = "pending_leave";

    private String TAG_APPROVE_MEETING = "approve_meeting";
    private String TAG_REJECT_MEETING = "rejected_meeting";
    private String TAG_PENDING_MEETING = "pending_meeting";

    private String TAG_APPROVE_WEEKLYOFF = "approve_weekly_off";
    private String TAG_REJECT_WEEKLYOFF = "rejected_weekly_off";
    private String TAG_PENDING_WEEKLYOFF = "pending_weekly_off";

    private String TAG_APPROVE_TRAINING = "approve_training";
    private String TAG_REJECT_TRAINING = "rejected_training";
    private String TAG_PENDING_TRAINING = "pending_training";

    private String TAG_ABSENT = "absent";

    private String TOTAL_ASSOCIATES = "0";
    private String ABSCONDING = "0";
    private String NOTAVAILABLE = "0";

    private String APPROVE_FULLDAY = "0";
    private String REJECT_FULLDAY = "0";
    private String PENDING_FULLDAY = "0";

    private String APPROVE_HALFDAY = "0";
    private String REJECT_HALFDAY = "0";
    private String PENDING_HALFDAY = "0";

    private String APPROVE_LEAVE = "0";
    private String REJECT_LEAVE = "0";
    private String PENDING_LEAVE = "0";

    private String APPROVE_MEETING = "0";
    private String REJECT_MEETING = "0";
    private String PENDING_MEETING = "0";

    private String APPROVE_WEEKLYOFF = "0";
    private String REJECT_WEEKLYOFF = "0";
    private String PENDING_WEEKLYOFF = "0";

    private String APPROVE_TRAINING = "0";
    private String REJECT_TRAINING = "0";
    private String PENDING_TRAINING = "0";

    private String ABSENT = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_attendance_count);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.dashboardattendancecount_topbar_title);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.DashboardAttendanceCountDateTAGTextViewID);
        textViewDate.setText(currentDate);

        initAllViews();

        if(CommonUtils.isInternelAvailable(DashboardAttendanceCountActivity.this)){
            requestDashBoardAttendanceCount();
        }
        else{
            Toast.makeText(DashboardAttendanceCountActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        //Back Button
        dashboardattendancecount_topbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        dashboardattendancecount_topbarbackImageViewID = (ImageView) findViewById(R.id.dashboardattendancecount_topbarbackImageViewID);

        DashboardAttendanceCountTotalUsersValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTotalUsersValueTextViewID);

        DashboardAttendanceCountFullDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountFullDayApprovedValueTextViewID);
        DashboardAttendanceCountFullDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountFullDayRejectedValueTextViewID);
        DashboardAttendanceCountFullDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountFullDayPendingValueTextViewID);

        DashboardAttendanceCountHalfDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountHalfDayApprovedValueTextViewID);
        DashboardAttendanceCountHalfDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountHalfDayRejectedValueTextViewID);
        DashboardAttendanceCountHalfDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountHalfDayPendingValueTextViewID);

        DashboardAttendanceCountLeaveDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountLeaveDayApprovedValueTextViewID);
        DashboardAttendanceCountLeaveDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountLeaveDayRejectedValueTextViewID);
        DashboardAttendanceCountLeaveDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountLeaveDayPendingValueTextViewID);

        DashboardAttendanceCountMeetingDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountMeetingDayApprovedValueTextViewID);
        DashboardAttendanceCountMeetingDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountMeetingDayRejectedValueTextViewID);
        DashboardAttendanceCountMeetingDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountMeetingDayPendingValueTextViewID);

        DashboardAttendanceCountWeeklyOffDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountWeeklyOffDayApprovedValueTextViewID);
        DashboardAttendanceCountWeeklyOffDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountWeeklyOffDayRejectedValueTextViewID);
        DashboardAttendanceCountWeeklyOffDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountWeeklyOffDayPendingValueTextViewID);

        DashboardAttendanceCountTrainingDayApprovedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTrainingDayApprovedValueTextViewID);
        DashboardAttendanceCountTrainingDayRejectedValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTrainingDayRejectedValueTextViewID);
        DashboardAttendanceCountTrainingDayPendingValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTrainingDayPendingValueTextViewID);

        DashboardAttendanceCountTOTALABSENTDayCountValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTOTALABSENTDayCountValueTextViewID);
        DashboardAttendanceCountTOTALAbscondingDayCountValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTOTALAbscondingDayCountValueTextViewID);
        DashboardAttendanceCountTOTALNotAvailableDayCountValueTextViewID = (TextView)findViewById(R.id.DashboardAttendanceCountTOTALNotAvailableDayCountValueTextViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(DashboardAttendanceCountActivity.this);

    }

    private void requestDashBoardAttendanceCount(){
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GET_ATTENDANCE_COUNT_DASHBOARD_RELATIVE_URI);
        client.AddParam("associate_id",prefs.getString("USERID",""));
        System.out.println("current user  > " +prefs.getString("USERID",""));

        new Thread(new Runnable(){
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
    private void receiveDataForServerResponse(JSONObject jobj){

        try{

            if(client.responseCode==200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

                if(STATUS.equalsIgnoreCase("true")) {

                    JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                    System.out.println("JOBJECT_DATA: " + JOBJECT_DATA.toString());

                    if (JOBJECT_DATA != null) {

                        TOTAL_ASSOCIATES = JOBJECT_DATA.getString(TAG_TOTAL_ASSOCIATES);
                        System.out.println("TOTAL_ASSOCIATES: responseCode==200: "+ TOTAL_ASSOCIATES);
                        /*ABSCONDING = JOBJECT_DATA.getString(TAG_TOTAL_ABSCONDING);
                        System.out.println("ABSCONDING: responseCode==200: "+ ABSCONDING);*/
                        NOTAVAILABLE = JOBJECT_DATA.getString(TAG_TOTAL_NOTAVAILABLE);
                        System.out.println("NOTAVAILABLE: responseCode==200: "+ NOTAVAILABLE);

                        JOBJECT_DATA1 = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_DATA1);
                        System.out.println("JOBJECT_DATA1: " + JOBJECT_DATA1.toString());

                        if (JOBJECT_DATA1 != null){

                            APPROVE_FULLDAY = JOBJECT_DATA1.getString(TAG_APPROVE_FULLDAY);
                            REJECT_FULLDAY = JOBJECT_DATA1.getString(TAG_REJECT_FULLDAY);
                            PENDING_FULLDAY = JOBJECT_DATA1.getString(TAG_PENDING_FULLDAY);

                            APPROVE_HALFDAY = JOBJECT_DATA1.getString(TAG_APPROVE_HALFDAY);
                            REJECT_HALFDAY = JOBJECT_DATA1.getString(TAG_REJECT_HALFDAY);
                            PENDING_HALFDAY = JOBJECT_DATA1.getString(TAG_PENDING_HALFDAY);

                            APPROVE_LEAVE = JOBJECT_DATA1.getString(TAG_APPROVE_LEAVE);
                            REJECT_LEAVE = JOBJECT_DATA1.getString(TAG_REJECT_LEAVE);
                            PENDING_LEAVE = JOBJECT_DATA1.getString(TAG_PENDING_LEAVE);

                            APPROVE_MEETING = JOBJECT_DATA1.getString(TAG_APPROVE_MEETING);
                            REJECT_MEETING = JOBJECT_DATA1.getString(TAG_REJECT_MEETING);
                            PENDING_MEETING = JOBJECT_DATA1.getString(TAG_PENDING_MEETING);

                            APPROVE_WEEKLYOFF = JOBJECT_DATA1.getString(TAG_APPROVE_WEEKLYOFF);
                            REJECT_WEEKLYOFF = JOBJECT_DATA1.getString(TAG_REJECT_WEEKLYOFF);
                            PENDING_WEEKLYOFF = JOBJECT_DATA1.getString(TAG_PENDING_WEEKLYOFF);

                            /*APPROVE_TRAINING = JOBJECT_DATA1.getString(TAG_APPROVE_TRAINING);
                            REJECT_TRAINING = JOBJECT_DATA1.getString(TAG_REJECT_TRAINING);
                            PENDING_TRAINING = JOBJECT_DATA1.getString(TAG_PENDING_TRAINING);*/

                            //ABSENT = JOBJECT_DATA1.getString(TAG_ABSENT);

                            System.out.println("##########All ASSOCIATE Attendance Count details###################");

                            System.out.println("Total APPROVE_FULLDAY: " + APPROVE_FULLDAY);
                            System.out.println("Total REJECT_FULLDAY: " + REJECT_FULLDAY);
                            System.out.println("Total PENDING_FULLDAY: " + PENDING_FULLDAY);

                            System.out.println("Total APPROVE_HALFDAY: " + APPROVE_HALFDAY);
                            System.out.println("Total REJECT_HALFDAY: " + REJECT_HALFDAY);
                            System.out.println("Total PENDING_HALFDAY: " + PENDING_HALFDAY);

                            System.out.println("Total APPROVE_LEAVE: " + APPROVE_LEAVE);
                            System.out.println("Total REJECT_LEAVE: " + REJECT_LEAVE);
                            System.out.println("Total PENDING_LEAVE: " + PENDING_LEAVE);

                            System.out.println("Total APPROVE_MEETING: " + APPROVE_MEETING);
                            System.out.println("Total REJECT_MEETING: " + REJECT_MEETING);
                            System.out.println("Total PENDING_MEETING: " + PENDING_MEETING);

                            System.out.println("Total APPROVE_WEEKLYOFF: " + APPROVE_WEEKLYOFF);
                            System.out.println("Total REJECT_WEEKLYOFF: " + REJECT_WEEKLYOFF);
                            System.out.println("Total PENDING_WEEKLYOFF: " + PENDING_WEEKLYOFF);

                            /*System.out.println("Total APPROVE_TRAINING: " + APPROVE_TRAINING);
                            System.out.println("Total REJECT_TRAINING: " + REJECT_TRAINING);
                            System.out.println("Total PENDING_TRAINING: " + PENDING_TRAINING);*/

                            //System.out.println("Total ABSENT: " + ABSENT);

                            System.out.println("##########End Of All ASSOCIATE Attendance Count details###################");

                        }//if (JOBJECT_DATA1 != null)
                        else{
                            System.out.println("JOBJECT_DATA1 is Null");
                        }

                    }//if (JOBJECT_DATA != null)
                    else{
                        System.out.println("JOBJECT_DATA is Null");
                    }

                }//if(STATUS.equalsIgnoreCase("true")

            }//if(client.responseCode==200)
            if(client.responseCode!=200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode!=200: "+ STATUS);
                System.out.println("MESSAGE: responseCode!=200: "+ MESSAGE);

            }//if(client.responseCode!=200)

        }
        catch(Exception e){}

    }
    Handler handler = new Handler(){

        public void handleMessage(Message msg){

            try {
                if((progressDialog != null) && progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
            }catch (final Exception e) {
                e.printStackTrace();
            }

            //Success
            if(client.responseCode==200){

                //Toast.makeText(SignupActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showAssociateAttendanceCountDetails();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    private void showAssociateAttendanceCountDetails(){

        DashboardAttendanceCountTotalUsersValueTextViewID.setText("");
        DashboardAttendanceCountTotalUsersValueTextViewID.setText(TOTAL_ASSOCIATES);

        DashboardAttendanceCountFullDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountFullDayApprovedValueTextViewID.setText(APPROVE_FULLDAY);
        DashboardAttendanceCountFullDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountFullDayRejectedValueTextViewID.setText(REJECT_FULLDAY);
        DashboardAttendanceCountFullDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountFullDayPendingValueTextViewID.setText(PENDING_FULLDAY);

        DashboardAttendanceCountHalfDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountHalfDayApprovedValueTextViewID.setText(APPROVE_HALFDAY);
        DashboardAttendanceCountHalfDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountHalfDayRejectedValueTextViewID.setText(REJECT_HALFDAY);
        DashboardAttendanceCountHalfDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountHalfDayPendingValueTextViewID.setText(PENDING_HALFDAY);

        DashboardAttendanceCountLeaveDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountLeaveDayApprovedValueTextViewID.setText(APPROVE_LEAVE);
        DashboardAttendanceCountLeaveDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountLeaveDayRejectedValueTextViewID.setText(REJECT_LEAVE);
        DashboardAttendanceCountLeaveDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountLeaveDayPendingValueTextViewID.setText(PENDING_LEAVE);

        DashboardAttendanceCountMeetingDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountMeetingDayApprovedValueTextViewID.setText(APPROVE_MEETING);
        DashboardAttendanceCountMeetingDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountMeetingDayRejectedValueTextViewID.setText(REJECT_MEETING);
        DashboardAttendanceCountMeetingDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountMeetingDayPendingValueTextViewID.setText(PENDING_MEETING);

        DashboardAttendanceCountWeeklyOffDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountWeeklyOffDayApprovedValueTextViewID.setText(APPROVE_WEEKLYOFF);
        DashboardAttendanceCountWeeklyOffDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountWeeklyOffDayRejectedValueTextViewID.setText(REJECT_WEEKLYOFF);
        DashboardAttendanceCountWeeklyOffDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountWeeklyOffDayPendingValueTextViewID.setText(PENDING_WEEKLYOFF);

        DashboardAttendanceCountTrainingDayApprovedValueTextViewID.setText("");
        DashboardAttendanceCountTrainingDayApprovedValueTextViewID.setText(APPROVE_TRAINING);
        DashboardAttendanceCountTrainingDayRejectedValueTextViewID.setText("");
        DashboardAttendanceCountTrainingDayRejectedValueTextViewID.setText(REJECT_TRAINING);
        DashboardAttendanceCountTrainingDayPendingValueTextViewID.setText("");
        DashboardAttendanceCountTrainingDayPendingValueTextViewID.setText(PENDING_TRAINING);

        DashboardAttendanceCountTOTALABSENTDayCountValueTextViewID.setText("");
        DashboardAttendanceCountTOTALABSENTDayCountValueTextViewID.setText(ABSENT);

        DashboardAttendanceCountTOTALAbscondingDayCountValueTextViewID.setText("");
        DashboardAttendanceCountTOTALAbscondingDayCountValueTextViewID.setText(ABSCONDING);

        DashboardAttendanceCountTOTALNotAvailableDayCountValueTextViewID.setText("");
        DashboardAttendanceCountTOTALNotAvailableDayCountValueTextViewID.setText(NOTAVAILABLE);
    }

    @Override
    public void onBackPressed()
    {
        DashboardAttendanceCountActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            if((progressDialog != null) && progressDialog.isShowing() ){
                progressDialog.dismiss();
            }
        }catch (final Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }

        super.onDestroy();

    }

}//Main Class
