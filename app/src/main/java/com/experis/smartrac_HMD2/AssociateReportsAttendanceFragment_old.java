package com.experis.smartrac_HMD2;

/**
 Class Name: AssociateReportsAttendanceFragment
 Created by Rana Krishna Paul
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssociateReportsAttendanceFragment_old extends Fragment {

    private ListView associatereportsAttendanceDetailsListViewID;
    private TextView associatereportsNoDataTextViewID;

    private View viewTypeViewAssociatereportsID10023;
    private LinearLayout listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID;
    private ListView associatereportsAttendanceDetailsListViewID1;
    private View viewTypeViewAssociatereportsID10024;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private JSONObject JOBJECT_DATA = null;

    private String TAG_JOBJECT_CURRENT_MONTH = "current_month_result";
    private JSONObject JOBJECT_CURRENT_MONTH = null;
    private String TAG_JOBJECT_PREVIOUS_MONTH = "previous_month_result";
    private JSONObject JOBJECT_PREVIOUS_MONTH = null;

    private String TAG_CURRENT_MONTH = "current_month";
    private String TAG_PREVIOUS_MONTH = "previous_month";
    private String TAG_IN = "in";
    private String TAG_LEAVE = "leave";
    private String TAG_MEETING = "meeting";
    private String TAG_OUT = "out";
    private String TAG_WEEKLY_OFF = "weekly_off";

    private String CURRENT_MONTH = "";
    private String PREVIOUS_MONTH = "";

    private String IN = "0";
    private String LEAVE = "0";
    private String MEETING = "0";
    private String OUT = "0";
    private String WEEKLY_OFF = "0";

    private String IN1 = "0";
    private String LEAVE1 = "0";
    private String MEETING1 = "0";
    private String OUT1 = "0";
    private String WEEKLY_OFF1 = "0";

    private String TAG_JARRAY_MONTHLYATTENDANCEDATALIST = "details";

    private String TAG_ATTENDANCE_DATE_SUB = "attendance_date_sub";
    private String TAG_ATTENDANCE_TYPE = "attendance_type";
    private String TAG_ATTENDANCE_APPLY_DATE = "attendance_date";
    private String TAG_REASON = "reason";
    private String TAG_LEAVE_TYPE = "leave_type";
    private String TAG_ATTENDANCE_TIME = "attendance_time";
    private String TAG_ATTENDANCE_STATUS = "status";

    private List<String> attendance_date_subList = null;
    private List<String> attendance_typeList = null;
    private List<String> attendance_dateList = null;
    private List<String> reasonList = null;
    private List<String> leave_typeList = null;
    private List<String> attendance_timeList = null;
    private List<String> attendance_statusList = null;

    public AssociateReportsAttendanceFragment_old() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAllViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity(),"In Time Fragment",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.associatereportsattendancefragment_old, container, false);

        associatereportsAttendanceDetailsListViewID = (ListView)view.findViewById(R.id.associatereportsAttendanceDetailsListViewID);
        associatereportsNoDataTextViewID = (TextView)view.findViewById(R.id.associatereportsNoDataTextViewID);

        viewTypeViewAssociatereportsID10023 = (View)view.findViewById(R.id.viewTypeViewAssociatereportsID10023);
        listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID = (LinearLayout)view.findViewById(R.id.listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID);
        associatereportsAttendanceDetailsListViewID1 = (ListView)view.findViewById(R.id.associatereportsAttendanceDetailsListViewID1);
        viewTypeViewAssociatereportsID10024 = (View)view.findViewById(R.id.viewTypeViewAssociatereportsID10024);

        return view;
    }

    private void initAllViews(){

        //shared preference
        prefs = getActivity().getSharedPreferences(CommonUtils.PREFERENCE_NAME,getActivity().MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //attendance_dateList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(getActivity());

        attendance_date_subList = new ArrayList<String>(0);
        attendance_typeList = new ArrayList<String>(0);
        attendance_dateList = new ArrayList<String>(0);
        reasonList = new ArrayList<String>(0);
        leave_typeList = new ArrayList<String>(0);
        attendance_timeList = new ArrayList<String>(0);
        attendance_statusList = new ArrayList<String>(0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(CommonUtils.isInternelAvailable(getActivity())){
            requestAssociateReportsAttendanceList();
        }
        else{
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        associatereportsAttendanceDetailsListViewID.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    //Toast.makeText(getActivity(), "position:"+ String.valueOf(position), Toast.LENGTH_SHORT).show();
                    loadMonthlyRecords(CURRENT_MONTH);
                }
                if(position==1){
                    //Toast.makeText(getActivity(), "position:"+ String.valueOf(position), Toast.LENGTH_SHORT).show();
                    loadMonthlyRecords(PREVIOUS_MONTH);
                }
            }

        });

    }//onActivityCreated(Bundle savedInstanceState)

    private void loadMonthlyRecords(String MONTH){

        attendance_date_subList.clear();
        attendance_typeList.clear();
        attendance_dateList.clear();
        reasonList.clear();
        leave_typeList.clear();
        attendance_timeList.clear();
        attendance_statusList.clear();

        viewTypeViewAssociatereportsID10023.setVisibility(View.GONE);
        listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID.setVisibility(View.GONE);
        associatereportsAttendanceDetailsListViewID1.setVisibility(View.GONE);
        viewTypeViewAssociatereportsID10024.setVisibility(View.GONE);

        associatereportsNoDataTextViewID.setVisibility(View.GONE);

        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        client = new RestFullClient(Constants.BASE_URL+Constants.ASSOCIATE_REPORTS_ATTENDANCE_MONTHLY_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));
        client.AddParam("req_month", MONTH);

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
                receiveDataForServerResponse1(client.jObj);
                handler1.sendEmptyMessage(0);

            }

        }).start();

    }
    private void receiveDataForServerResponse1(JSONObject jobj){

        try{

            if(client.responseCode==200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

                if(STATUS.equalsIgnoreCase("true")){

                    if (jobj.has(TAG_JOBJECT_DATA)) {

                        System.out.println("jobj.has(TAG_JOBJECT_DATA): in STATUS_CODE = 1: "+ jobj.has(TAG_JOBJECT_DATA));
                        JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                        System.out.println("JOBJECT_DATA: "+JOBJECT_DATA.toString());

                        if(JOBJECT_DATA!=null){

                            JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_MONTHLYATTENDANCEDATALIST);
                            JSONObject c = null;

                            String attendance_date_sub = null;
                            String attendance_type = null;
                            String attendance_date = null;
                            String reason = null;
                            String leave_type = null;
                            String attendance_time = null;
                            String status = null;

                            System.out.println("##########All Monthly Attendance Details###################");

                            attendance_date_subList.clear();
                            attendance_typeList.clear();
                            attendance_dateList.clear();
                            reasonList.clear();
                            leave_typeList.clear();
                            attendance_timeList.clear();
                            attendance_statusList.clear();

                            if(pendingDateArray.length()==0){
                            }
                            if(pendingDateArray.length()!=0){

                                for(int i = 0; i < pendingDateArray.length(); i++) {

                                    try {
                                        c = pendingDateArray.getJSONObject(i);
                                        System.out.println("C is : " + c);
                                        if (c != null) {
                                            attendance_date_sub = c.getString(TAG_ATTENDANCE_DATE_SUB);
                                            System.out.println("attendance_date_sub: " + attendance_date_sub);

                                            attendance_type = c.getString(TAG_ATTENDANCE_TYPE);
                                            System.out.println("attendance_type: " + attendance_type);

                                            attendance_date = c.getString(TAG_ATTENDANCE_APPLY_DATE);
                                            System.out.println("attendance_date: " + attendance_date);

                                            reason = c.getString(TAG_REASON);
                                            System.out.println("reason: " + reason);

                                            leave_type = c.getString(TAG_LEAVE_TYPE);
                                            System.out.println("leave_type: " + leave_type);

                                            attendance_time = c.getString(TAG_ATTENDANCE_TIME);
                                            System.out.println("attendance_time: " + attendance_time);

                                            status = c.getString(TAG_ATTENDANCE_STATUS);
                                            System.out.println("status: " + status);

                                            attendance_date_subList.add(attendance_date_sub);
                                            attendance_typeList.add(attendance_type);
                                            if(attendance_date.equalsIgnoreCase("0000-00-00")){
                                                attendance_dateList.add("-");
                                            }
                                            else{
                                                attendance_dateList.add(attendance_date);
                                            }
                                            reasonList.add(reason);
                                            leave_typeList.add(leave_type);
                                            attendance_timeList.add(attendance_time);
                                            attendance_statusList.add(status);

                                        }
                                    } catch (Exception e) {
                                    }

                                }//for

                            }//if(pendingDateArray.length()!=0)

                            System.out.println("Total attendance_date_subList: " + attendance_date_subList.size());
                            System.out.println("Total attendance_typeList: " + attendance_typeList.size());
                            System.out.println("Total attendance_dateList: " + attendance_dateList.size());
                            System.out.println("Total reasonList: " + reasonList.size());
                            System.out.println("Total leave_typeList: " + leave_typeList.size());
                            System.out.println("Total attendance_timeList: " + attendance_timeList.size());
                            System.out.println("Total attendance_statusList: " + attendance_statusList.size());

                            System.out.println("##########End Of All Monthly Attendance Details###################");


                        }//if(JOBJECT_DATA!=null)

                    }//if(jobj.has(TAG_JOBJECT_DATA))

                }//if(STATUS.equalsIgnoreCase("true"))

            }//if(client.responseCode==200)

            if(client.responseCode!=200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

            }//if(client.responseCode!=200)

        }
        catch(Exception e){}

    }
    Handler handler1 = new Handler(){

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

                //success
                if(STATUS.equalsIgnoreCase("true")){
                    //showSuccessDialog();
                    //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                    showMonthlyAttendanceDetails();
                }

                //failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //failed
            if(client.responseCode!=200){
                //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    private void showMonthlyAttendanceDetails(){
        if(attendance_date_subList.size()!=0){
            viewTypeViewAssociatereportsID10023.setVisibility(View.VISIBLE);
            listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID.setVisibility(View.VISIBLE);
            associatereportsAttendanceDetailsListViewID1.setVisibility(View.VISIBLE);
            viewTypeViewAssociatereportsID10024.setVisibility(View.VISIBLE);
            associatereportsNoDataTextViewID.setVisibility(View.GONE);
            associatereportsAttendanceDetailsListViewID1.setAdapter(new CustomAdapterForAssociatereportsAttendanceMonthlyDetails(getActivity()));
        }
        else{
            viewTypeViewAssociatereportsID10023.setVisibility(View.GONE);
            listview_header_associatereports_MonthlyDetails_for_table_LinearLayoutID.setVisibility(View.GONE);
            associatereportsAttendanceDetailsListViewID1.setVisibility(View.GONE);
            viewTypeViewAssociatereportsID10024.setVisibility(View.GONE);
            associatereportsNoDataTextViewID.setVisibility(View.VISIBLE);
        }
    }
    /*
    * CustomAdapterForAssociatereportsAttendanceMonthlyDetails
    */
    public class CustomAdapterForAssociatereportsAttendanceMonthlyDetails extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForAssociatereportsAttendanceMonthlyDetails(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return attendance_date_subList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;

            if(convertView==null){

                LayoutInflater inflater = (LayoutInflater)cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.customlayout_associatereport_attendance_monthlydetails_old, null);
            }
            else{

                view = convertView;
            }

            final TextView SubmissionDateTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MonthlyDetailsSubmissionDateValueTextViewID);
            final TextView AppliedDateTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MonthlyDetailsApplyDateValueTextViewID);
            final TextView Attendance_TypeTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MonthlyDetailsTypeValueTextViewID);
            final TextView Attendance_TimeTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MonthlyDetailsTimeValueTextViewID);
            final TextView Leave_ReasonTextView = (TextView)view.findViewById(R.id.customlayoutassociatereportsMonthlyDetailsReasonValueTextViewID);
            final TextView statusTextView = (TextView)view.findViewById(R.id.customlayoutassociatereportsMonthlyDetailsStatusValueTextViewID);

            SubmissionDateTextView.setText(attendance_date_subList.get(position));
            AppliedDateTextView.setText(attendance_dateList.get(position));
            Attendance_TimeTextView.setText(attendance_timeList.get(position));
            Leave_ReasonTextView.setText(reasonList.get(position));
            statusTextView.setText(attendance_statusList.get(position));

            if(attendance_typeList.get(position).equalsIgnoreCase("leave")){
                Attendance_TypeTextView.setText(attendance_typeList.get(position)+"\n( "+leave_typeList.get(position)+" )");
            }
            else{
                Attendance_TypeTextView.setText(attendance_typeList.get(position));
            }

            return view;

        }

    }//CustomAdapterForAssociatereportsAttendanceMonthlyDetails

    private void requestAssociateReportsAttendanceList() {

        //progressDialog.setTitle("Attendance Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        client = new RestFullClient(Constants.BASE_URL+Constants.ASSOCIATE_REPORTS_ATTENDANCE_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));

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

    }//requestAssociateReportsAttendanceList()

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

                        if(JOBJECT_DATA.has(TAG_CURRENT_MONTH)){
                            CURRENT_MONTH = JOBJECT_DATA.getString(TAG_CURRENT_MONTH);
                            System.out.println("CURRENT_MONTH: " + CURRENT_MONTH);
                        }
                        if(JOBJECT_DATA.has(TAG_PREVIOUS_MONTH)){
                            PREVIOUS_MONTH = JOBJECT_DATA.getString(TAG_PREVIOUS_MONTH);
                            System.out.println("PREVIOUS_MONTH: " + PREVIOUS_MONTH);
                        }

                        if(JOBJECT_DATA.has(TAG_JOBJECT_CURRENT_MONTH)){
                            JOBJECT_CURRENT_MONTH = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_CURRENT_MONTH);
                            System.out.println("JOBJECT_CURRENT_MONTH : " + JOBJECT_CURRENT_MONTH);

                            if(JOBJECT_CURRENT_MONTH!=null){
                                if(JOBJECT_CURRENT_MONTH.has(TAG_IN)){
                                    IN = JOBJECT_CURRENT_MONTH.getString(TAG_IN);
                                    System.out.println("IN: " + IN);
                                }
                                if(JOBJECT_CURRENT_MONTH.has(TAG_LEAVE)){
                                    LEAVE = JOBJECT_CURRENT_MONTH.getString(TAG_LEAVE);
                                    System.out.println("LEAVE: " + LEAVE);
                                }
                                if(JOBJECT_CURRENT_MONTH.has(TAG_MEETING)){
                                    MEETING = JOBJECT_CURRENT_MONTH.getString(TAG_MEETING);
                                    System.out.println("MEETING: " + MEETING);
                                }
                                if(JOBJECT_CURRENT_MONTH.has(TAG_OUT)){
                                    OUT = JOBJECT_CURRENT_MONTH.getString(TAG_OUT);
                                    System.out.println("OUT: " + OUT);
                                }
                                if(JOBJECT_CURRENT_MONTH.has(TAG_WEEKLY_OFF)){
                                    WEEKLY_OFF = JOBJECT_CURRENT_MONTH.getString(TAG_WEEKLY_OFF);
                                    System.out.println("WEEKLY_OFF: " + WEEKLY_OFF);
                                }

                            }//if(JOBJECT_CURRENT_MONTH!=null)

                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_CURRENT_MONTH))

                        if(JOBJECT_DATA.has(TAG_JOBJECT_PREVIOUS_MONTH)){
                            JOBJECT_PREVIOUS_MONTH = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_PREVIOUS_MONTH);
                            System.out.println("JOBJECT_PREVIOUS_MONTH : " + JOBJECT_PREVIOUS_MONTH);

                            if(JOBJECT_PREVIOUS_MONTH!=null){
                                if(JOBJECT_PREVIOUS_MONTH.has(TAG_IN)){
                                    IN1 = JOBJECT_PREVIOUS_MONTH.getString(TAG_IN);
                                    System.out.println("IN1: " + IN1);
                                }
                                if(JOBJECT_PREVIOUS_MONTH.has(TAG_LEAVE)){
                                    LEAVE1 = JOBJECT_PREVIOUS_MONTH.getString(TAG_LEAVE);
                                    System.out.println("LEAVE1: " + LEAVE1);
                                }
                                if(JOBJECT_PREVIOUS_MONTH.has(TAG_MEETING)){
                                    MEETING1 = JOBJECT_PREVIOUS_MONTH.getString(TAG_MEETING);
                                    System.out.println("MEETING1: " + MEETING1);
                                }
                                if(JOBJECT_PREVIOUS_MONTH.has(TAG_OUT)){
                                    OUT1 = JOBJECT_PREVIOUS_MONTH.getString(TAG_OUT);
                                    System.out.println("OUT1: " + OUT1);
                                }
                                if(JOBJECT_PREVIOUS_MONTH.has(TAG_WEEKLY_OFF)){
                                    WEEKLY_OFF1 = JOBJECT_PREVIOUS_MONTH.getString(TAG_WEEKLY_OFF);
                                    System.out.println("WEEKLY_OFF1: " + WEEKLY_OFF1);
                                }

                            }//if(JOBJECT_PREVIOUS_MONTH!=null)


                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_PREVIOUS_MONTH))

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

    }//receiveDataForServerResponse(JSONObject jobj)

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

                //success
                if(STATUS.equalsIgnoreCase("true")){
                    //showSuccessDialog();
                    //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                    showAttendanceDetailsinAssociateReports();
                }

                //failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //failed
            if(client.responseCode!=200){
                //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show failure Dialog
    private void showFailureDialog(){
        associatereportsNoDataTextViewID.setVisibility(View.VISIBLE);
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();

    }

    private void showAttendanceDetailsinAssociateReports(){

        associatereportsAttendanceDetailsListViewID.setAdapter(new CustomAdapterForAssociatereportsAttendanceDetails(getActivity()));
    }

    /*
    * CustomAdapterForAssociatereportsAttendanceDetails
    */
    public class CustomAdapterForAssociatereportsAttendanceDetails extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForAssociatereportsAttendanceDetails(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;

            if(convertView==null){

                LayoutInflater inflater = (LayoutInflater)cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_associatereports_attendancepage, null);
            }
            else{

                view = convertView;
            }

            final TextView monthTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MonthTagTextViewID);
            final TextView dayspresentTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_DaysPresentTagTextViewID);
            final TextView dayspresentTextView1 = (TextView)view.findViewById(R.id.customlayoutassociatereports_DaysPresentTagTextViewID1);
            final TextView weeklyoffTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_weeklyoffTagTextViewID);
            final TextView leaveTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_LeaveTagTextViewID);
            final TextView meetingTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_MeetingTagTextViewID);

            if(position==0){
                monthTextView.setText(CURRENT_MONTH);
                dayspresentTextView.setText(IN);
                dayspresentTextView1.setText(OUT);
                weeklyoffTextView.setText(WEEKLY_OFF);
                leaveTextView.setText(LEAVE);
                meetingTextView.setText(MEETING);
            }
            if(position==1){
                monthTextView.setText(PREVIOUS_MONTH);
                dayspresentTextView.setText(IN1);
                dayspresentTextView1.setText(OUT1);
                weeklyoffTextView.setText(WEEKLY_OFF1);
                leaveTextView.setText(LEAVE1);
                meetingTextView.setText(MEETING1);
            }

            return view;

        }


    }//CustomAdapterForAssociatereportsAttendanceDetails


    @Override
    public void onDestroy() {
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
