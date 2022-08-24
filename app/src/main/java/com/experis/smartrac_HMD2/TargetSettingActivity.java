package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetSettingActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private Spinner targetsettingAssociateSpinnerID;
    private Spinner targetsettingDateSpinnerID;
    private Spinner targetsettingMonthSpinnerID;

    private LinearLayout targetsettingDateMonthLayoutID;
    private LinearLayout targetsettingDetailsListLayoutID;

    private ListView targetsettingDetailsListViewID;

    private ImageView targetsettingtopbarbackImageViewID;
    private ImageView targetsettingtopbarusericonImageViewID;
    private ImageView targetsettingPageSubmitImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_ASSOCIATELIST = "associate";
    private String TAG_JARRAY_TARGETPRODUCTLIST = "target_product";

    private JSONObject JOBJECT_DATA = null;

    //ASSOCIATE DETAILS TAGS
    private String TAG_ASSOCIATEID = "id";
    private String TAG_ASSOCIATEISDCODE = "isd_code";
    private String TAG_ASSOCIATEFIRSTNAME = "first_name";
    private String TAG_ASSOCIATELASTNAME = "last_name";
    private List<String> associate_idList = null;
    private List<String> associate_isdList = null;
    private List<String> associate_firstnameList = null;
    private List<String> associate_lastnameList = null;
    private List<String> associate_fullnamewithisdList = null;

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTID = "id";
    private String TAG_PRODUCTNAME = "product_name";
    private String TAG_PRODUCTTARGETQUANTITY = "target_quantity";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;
    private List<String> producttarget_quantityList = null;

    private String associate_IDSelected = "";
    private String associate_NameSelected = "";
    private String yearSelected = "";
    private String monthSelected = "";

    private boolean validYear = false;
    private boolean validMonth = false;

    //private String YEAR[] = {"Select Year","2016","2017","2018","2019","2020","2021","2022"};
    private String MONTH[] = {"Select Month","01","02","03","04","05","06","07","08","09","10","11","12"};
    //private String MONTH[] = {"Select Month","1","2","3","4","5","6","7","8","9","10","11","12"};

    private String YEAR[] = new String[8];
    private Calendar calendar;

    private Set<String> selectionSetProductID = null;
    private List<String> selectionListProductQuantityTarget = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_setting);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.target_setting_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        if(CommonUtils.isInternelAvailable(TargetSettingActivity.this)){
            requestAssociateList();
        }
        else{
            Toast.makeText(TargetSettingActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }

        /*Setting Target Year Dynamically*/
        System.out.println("######Setting Target Year Dynamically######");
        YEAR[0] = "Select Year";
        System.out.println("YEAR[0]: "+ YEAR[0]);
        YEAR[1] = String.valueOf(calendar.get(Calendar.YEAR));
        System.out.println("YEAR[1]: "+ YEAR[1]);
        YEAR[2] = String.valueOf(calendar.get(Calendar.YEAR)+1);
        System.out.println("YEAR[2]: "+ YEAR[2]);
        YEAR[3] = String.valueOf(calendar.get(Calendar.YEAR)+2);
        System.out.println("YEAR[3]: "+ YEAR[3]);
        YEAR[4] = String.valueOf(calendar.get(Calendar.YEAR)+3);
        System.out.println("YEAR[4]: "+ YEAR[4]);
        YEAR[5] = String.valueOf(calendar.get(Calendar.YEAR)+4);
        System.out.println("YEAR[5]: "+ YEAR[5]);
        YEAR[6] = String.valueOf(calendar.get(Calendar.YEAR)+5);
        System.out.println("YEAR[6]: "+ YEAR[6]);
        YEAR[7] = String.valueOf(calendar.get(Calendar.YEAR)+6);
        System.out.println("YEAR[7]: "+ YEAR[7]);
        System.out.println("///////////////////////////////////////////");
        /////////////////////////////////

        //Associate
        targetsettingAssociateSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                associate_NameSelected = parent.getItemAtPosition(position).toString();
                associate_IDSelected = associate_idList.get(position);
                System.out.println("associate_NameSelected: "+ associate_NameSelected);
                System.out.println("associate_IDSelected: "+ associate_IDSelected);
                if(position==0){
                }
                else{
                     if(validYear&&validMonth){
                         requestProductDetailsList();
                     }
                    else{
                         Toast.makeText(TargetSettingActivity.this, "Please Select Valid Year/Month First", Toast.LENGTH_LONG).show();
                         targetsettingAssociateSpinnerID.setSelection(0);
                     }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Year
        targetsettingDateSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                yearSelected = parent.getItemAtPosition(position).toString();
                System.out.println("yearSelected: "+ yearSelected);
                if(position==0){
                    validYear = false;
                }
                else{
                     validYear = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Month
        targetsettingMonthSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monthSelected = parent.getItemAtPosition(position).toString();
                System.out.println("monthSelected: "+ monthSelected);
                if(position==0){
                    validMonth = false;
                }
                else{
                     validMonth = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Back Button
        targetsettingtopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        targetsettingtopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        targetsettingtopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(TargetSettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        //Submit Button
        targetsettingPageSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {

                selectionListProductQuantityTarget.clear();
                selectionSetProductID.clear();

                //ArrayList<String> arraylistEdittextValues = new ArrayList<String>();
                EditText et;
                for (int i = 0; i < targetsettingDetailsListViewID.getCount(); i++) {
                    et = (EditText) targetsettingDetailsListViewID.getChildAt(i).findViewById(R.id.customlayout_targetsetting_ProductQuantityTargetEditTextID);
                    if (et!=null) {
                        if(et.getText().toString().length()!=0){
                            String tmp = String.valueOf(et.getText().toString());
                            System.out.println("EditText Value: "+String.valueOf(et.getText().toString()));
                            //selectionListProductQuantityTarget.add(String.valueOf(et.getText().toString())); //Used Earlier
                            selectionListProductQuantityTarget.add("\""+tmp+"\"");
                            selectionSetProductID.add(product_idList.get(i));
                            System.out.println("selectionSetProductID Value: "+selectionSetProductID.toString());
                            System.out.println("product_idList.get(i) Value: "+product_idList.get(i).toString());
                        }
                        else{
                            selectionListProductQuantityTarget.add("0");
                            System.out.println("EditText Value: "+String.valueOf(et.getText().toString()));
                            selectionSetProductID.add(product_idList.get(i));
                            System.out.println("selectionSetProductID Value: "+selectionSetProductID.toString());
                            System.out.println("product_idList.get(i) Value: "+product_idList.get(i).toString());
                        }
                    }
                }
                System.out.println("selectionListProductQuantityTarget: "+selectionListProductQuantityTarget.toString());
                System.out.println("selectionSetProductID: "+selectionSetProductID.toString());
                System.out.println("product_idList.get(i) Value: "+product_idList.toString());

                if(selectionListProductQuantityTarget.size()!=0&&product_idList.size()!=0){
                    if(CommonUtils.isInternelAvailable(TargetSettingActivity.this)){
                        setProductTarget();
                    }
                    else{
                        Toast.makeText(TargetSettingActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        targetsettingAssociateSpinnerID = (Spinner)findViewById(R.id.targetsettingAssociateSpinnerID);
        targetsettingDateSpinnerID = (Spinner)findViewById(R.id.targetsettingDateSpinnerID);
        targetsettingMonthSpinnerID = (Spinner)findViewById(R.id.targetsettingMonthSpinnerID);

        targetsettingtopbarbackImageViewID = (ImageView)findViewById(R.id.targetsettingtopbarbackImageViewID);
        targetsettingtopbarusericonImageViewID = (ImageView)findViewById(R.id.targetsettingtopbarusericonImageViewID);
        targetsettingPageSubmitImageViewID = (ImageView)findViewById(R.id.targetsettingPageSubmitImageViewID);

        targetsettingDateMonthLayoutID = (LinearLayout)findViewById(R.id.targetsettingDateMonthLayoutID);
        targetsettingDetailsListLayoutID = (LinearLayout)findViewById(R.id.targetsettingDetailsListLayoutID);

        targetsettingDetailsListViewID = (ListView)findViewById(R.id.targetsettingDetailsListViewID);

        associate_idList = new ArrayList<String>(0);
        associate_isdList = new ArrayList<String>(0);
        associate_firstnameList = new ArrayList<String>(0);
        associate_lastnameList = new ArrayList<String>(0);
        associate_fullnamewithisdList = new ArrayList<String>(0);

        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);
        producttarget_quantityList = new ArrayList<String>(0);

        selectionSetProductID = new HashSet<String>(0);
        selectionListProductQuantityTarget =  new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(TargetSettingActivity.this);

        calendar = Calendar.getInstance();
    }


    //Set Product Target
    private void setProductTarget(){
        progressDialog.setMessage("Submitting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SETPRODUCTTARGET_BY_ASSOCIATE_RELATIVE_URI);
        client.AddParam("associate_id", associate_IDSelected);
        client.AddParam("month", monthSelected);
        client.AddParam("year", yearSelected);
        client.AddParam("product_ids", product_idList.toString());
        client.AddParam("target_qty", selectionListProductQuantityTarget.toString());
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

                receiveDataForServerResponse2(client.jObj);
                handler2.sendEmptyMessage(0);

            }

        }).start();
    }
    private void receiveDataForServerResponse2(JSONObject jobj) {

        try {

            if (client.responseCode == 200) {

                STATUS = jobj.getString(TAG_STATUS);
                MESSAGE = jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: " + STATUS);
                System.out.println("MESSAGE: responseCode==200: " + MESSAGE);

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
    Handler handler2 = new Handler(){

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

                //Success
                if(STATUS.equalsIgnoreCase("true")){
                    showSuccessDialog();
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showRetryDialog();
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(TargetSettingActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                targetsettingDetailsListLayoutID.setVisibility(View.GONE);
                targetsettingAssociateSpinnerID.setSelection(0);
            }
        });

        aldb.show();
    }


    //All Product Details
    private void requestProductDetailsList(){

        if(associate_IDSelected != "") {
            product_idList.clear();
            product_nameList.clear();
            producttarget_quantityList.clear();

            //progressDialog.setTitle("Product Target Details");
            progressDialog.setMessage("Loading... Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            client = new RestFullClient(Constants.BASE_URL+Constants.GETPRODUCTTARGET_BY_ASSOCIATE_RELATIVE_URI);
            client.AddParam("associate_id", associate_IDSelected);
            client.AddParam("month", monthSelected);
            client.AddParam("year", yearSelected);
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

        }//if(associate_IDSelected != "")
        else{
              Toast.makeText(TargetSettingActivity.this, "Please Choose an Associate to proceed", Toast.LENGTH_LONG).show();
        }

    }
    private void receiveDataForServerResponse1(JSONObject jobj){

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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_TARGETPRODUCTLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;
                        String quantity = null;

                        System.out.println("##########All PRODUCT List details###################");

                        product_idList.clear();
                        product_nameList.clear();
                        producttarget_quantityList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_PRODUCTID);
                                        name = c.getString(TAG_PRODUCTNAME);
                                        quantity = c.getString(TAG_PRODUCTTARGETQUANTITY);

                                        product_idList.add(id);
                                        product_nameList.add(name);
                                        producttarget_quantityList.add(quantity);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total product_idList: " + product_idList.size());
                        System.out.println("Total product_nameList: " + product_nameList.size());
                        System.out.println("Total producttarget_quantityList: " + producttarget_quantityList.size());
                        System.out.println("##########End Of All PRODUCT List details###################");


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

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showProductTargetDetails();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    private void showProductTargetDetails(){
        if(product_idList.size()!=0){
            targetsettingDetailsListLayoutID.setVisibility(View.VISIBLE);
            setProductTargetListView();
        }
    }
    private void setProductTargetListView(){
        targetsettingDetailsListViewID.setAdapter(new CustomAdapterForProductTargetDetails(TargetSettingActivity.this));
    }
    /*
    * CustomAdapterForProductTargetDetails
    */
    public class CustomAdapterForProductTargetDetails extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForProductTargetDetails(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return product_idList.size();
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
            ViewHolder viewHolder = new ViewHolder();

            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_target_setting, null);
            }
            else{
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = position;

            final TextView nameTextView = (TextView)view.findViewById(R.id.customlayout_targetsetting_ProductNameTextViewID);
            viewHolder.editText = (EditText)view.findViewById(R.id.customlayout_targetsetting_ProductQuantityTargetEditTextID);

            final ViewHolder holder = (ViewHolder)view.getTag();

            nameTextView.setText(product_nameList.get(position));
            holder.editText.setText(producttarget_quantityList.get(position));

            //holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    //holder.editText.setText(s.toString());
                    System.out.println("afterTextChanged(Editable s): " + s.toString());
                    producttarget_quantityList.set(pos1,s.toString()); //Updating with a new value
                }
            });


            return view;

        }

        class ViewHolder{
            EditText editText;
        }

    }//CustomAdapterForAttendanceDetails Class

    //All Associate List
    private void requestAssociateList(){
        associate_idList.clear();
        associate_isdList.clear();
        associate_firstnameList.clear();
        associate_lastnameList.clear();

        //progressDialog.setTitle("Associate Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETASSOCIATE_BY_TEAMLEAD_RELATIVE_URI);
        client.AddParam("tl_id",prefs.getString("USERID",""));
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_ASSOCIATELIST);
                        JSONObject c = null;
                        String id = null;
                        String isd = null;
                        String fname = null;
                        String lname = null;

                        System.out.println("##########All ASSOCIATE List details###################");

                        associate_idList.clear();
                        associate_isdList.clear();
                        associate_firstnameList.clear();
                        associate_lastnameList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_ASSOCIATEID);
                                        isd = c.getString(TAG_ASSOCIATEISDCODE);
                                        fname = c.getString(TAG_ASSOCIATEFIRSTNAME);
                                        lname = c.getString(TAG_ASSOCIATELASTNAME);

                                        associate_idList.add(id);
                                        associate_isdList.add(isd);
                                        associate_firstnameList.add(fname);
                                        associate_lastnameList.add(lname);
                                        associate_fullnamewithisdList.add(fname+" "+lname+" - "+isd);
                                        System.out.println("associate_fullnamewithisdList: " + associate_fullnamewithisdList.toString());
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total associate_idList: " + associate_idList.size());
                        System.out.println("Total associate_isdList: " + associate_isdList.size());
                        System.out.println("Total associate_firstnameList: " + associate_firstnameList.size());
                        System.out.println("Total associate_lastnameList: " + associate_lastnameList.size());
                        System.out.println("Total associate_fullnamewithisdList: " + associate_fullnamewithisdList.size());
                        System.out.println("##########End Of All ASSOCIATE List details###################");


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
                    showAssociateDetails();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    private void showAssociateDetails(){
        SetDropDownItemsForAssociate();
    }

    private void SetDropDownItemsForAssociate(){
        if(associate_idList.size()!=0){
            targetsettingDateMonthLayoutID.setVisibility(View.VISIBLE);
            associate_idList.add(0,"0");
            associate_fullnamewithisdList.add(0,"Select Associate");
            ArrayAdapter dataAdapter = new ArrayAdapter(TargetSettingActivity.this, android.R.layout.simple_spinner_item, associate_fullnamewithisdList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            targetsettingAssociateSpinnerID.setAdapter(dataAdapter);

            associate_IDSelected = associate_fullnamewithisdList.get(0);
            System.out.println("associate_IDSelected Initially: "+ associate_IDSelected);

            setDropDownForDateMonth();

        }
    }

    private void setDropDownForDateMonth(){

        ArrayAdapter dataAdapter = new ArrayAdapter(TargetSettingActivity.this, android.R.layout.simple_spinner_item, YEAR);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetsettingDateSpinnerID.setAdapter(dataAdapter);

        ArrayAdapter dataAdapter1 = new ArrayAdapter(TargetSettingActivity.this, android.R.layout.simple_spinner_item, MONTH);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetsettingMonthSpinnerID.setAdapter(dataAdapter1);
    }

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(TargetSettingActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        TargetSettingActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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

}//TargetSettingActivity
