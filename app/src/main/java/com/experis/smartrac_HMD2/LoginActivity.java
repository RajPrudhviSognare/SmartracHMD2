package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText loginpageUsernameEditTextID;
    private EditText loginpagePasswordEditTextID;
    private Button loginpageLoginBtnID;
    private TextView forgotpasswdTextViewID;

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private String GCMRegdID = "";
    private String IMEIID = "";
    private RestFullClient client;

    private String username = "";
    private String passwd = "";

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

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
    private  String DEFAULTSOS="7890446704";
    private  String FIRSTSOS="";
    private  String SECONDSOS="";
    private  String THIRDSOS="";


    //TL DETAILS
    private String TLID = "";
    private String TLISDCODE = "";
    private String TLOUTLETID = "";
    private String TLFIRSTNAME = "";
    private String TLLASTNAME = "";
    private String TLMOBILENUMBER = "";
    private String TLEMAILID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login2);

        initAllViews();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            System.out.println("Inside Login Page checkAllPermissions() is called Above Lallipop: ");
            try{
                checkAllPermissions();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            try{
                IMEIID = CommonUtils.getIMEI(LoginActivity.this);
                System.out.println("Inside Login Page 'IMEIID' below Lallipop: "+IMEIID);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        GCMRegdID = prefs.getString("GCMREGISTRATIONID","No GCM Regd. ID Found");
        System.out.println("Inside Login Page 'GCM/FCM Token': "+GCMRegdID);

        //Internet connection checker
        if(CommonUtils.isInternelAvailable(this)){
            //registerForGCMID();
        }
        else{
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
            showAlertDialog(LoginActivity.this, "Internet Connection Error!",
                    "Please check your internet settings.\n", false);
        }
        //Internet connection checker

        //Login button click
        loginpageLoginBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(CommonUtils.isInternelAvailable(LoginActivity.this)){

                        validateData();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*Intent i = new Intent(LoginActivity.this,MapsActivity.class);
                startActivity(i);*/

            }
        });
        //Forgot Password
        forgotpasswdTextViewID.setOnClickListener(new TextView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                //LoginActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

    }//onCreate()

    private void initAllViews(){
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        loginpageUsernameEditTextID = (EditText)findViewById(R.id.loginpageUsernameEditTextID);
        loginpagePasswordEditTextID = (EditText)findViewById(R.id.loginpagePasswordEditTextID);
        loginpageLoginBtnID = (Button)findViewById(R.id.loginpageLoginBtnID);

        forgotpasswdTextViewID = (TextView)findViewById(R.id.forgotpasswdTextViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    private void checkAllPermissions(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(LoginActivity.this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.CAMERA}, 1991);

        }//if
        else{
            IMEIID = CommonUtils.getIMEI(LoginActivity.this);
            System.out.println("Inside Login Page 'Device_IMEI_ID': "+IMEIID);
            System.out.println("READ_PHONE_STATE IS ALREADY GRANTED! ==> getIMEI() is called");
        }

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
                        && grantResults[5] == PackageManager.PERMISSION_GRANTED) {

                    IMEIID = CommonUtils.getIMEI(LoginActivity.this);
                    System.out.println("Inside Login Page 'Device_IMEI_ID': "+IMEIID);
                    System.out.println("READ_PHONE_STATE IS GRANTED! ==> getIMEI() is called");

                } else {
                    // permission denied! Disable the
                    // functionality that depends on these permissions.
                    IMEIID = "READ_PHONE_STATE IS REJECTED!";
                    System.out.println("Inside Login Page 'Device_IMEI_ID': "+IMEIID);
                    System.out.println("READ_PHONE_STATE IS REJECTED!");
                }

            }

        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //LoginActivity.this.finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(loginpageUsernameEditTextID.getText().toString()))
        {

            loginpageUsernameEditTextID.setError("Required field!");
            focusView = loginpageUsernameEditTextID;
            cancel = true;

        }
        else if(TextUtils.isEmpty(loginpagePasswordEditTextID.getText().toString()))
        {

            loginpagePasswordEditTextID.setError("Required field!");
            focusView = loginpagePasswordEditTextID;
            cancel = true;

        }

        if(cancel){

            focusView.requestFocus();
        }
        else
        {
            getTextValues();

        }

    }//validateData

    //Get the values from EditText
    private void getTextValues() {

        username = loginpageUsernameEditTextID.getText().toString();
        passwd = loginpagePasswordEditTextID.getText().toString();

        if(!username.equalsIgnoreCase("")&&!passwd.equalsIgnoreCase("")){

            sendDataForLogin();
        }
        else{

            Toast.makeText(this, "Please Enter valid Credential!", Toast.LENGTH_SHORT).show();
        }
    }

    //For Login
    private void sendDataForLogin(){

        //progressDialog.setTitle("Login");
        progressDialog.setMessage("Checking login credentials... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.LOGIN_RELATIVE_URI);
        client.AddParam("login_id", username);
        System.out.println("login_id:"+username);
        client.AddParam("password", CommonUtils.md5(passwd));
        System.out.println("password:"+ CommonUtils.md5(passwd));
        client.AddParam("device_id", IMEIID);
        System.out.println("device_id:"+IMEIID);
        client.AddParam("registration_id", GCMRegdID);
        System.out.println("GCM registration_id:"+GCMRegdID);
        client.AddParam("device_type", "android");
        System.out.println("device_type:"+"android");

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

                if(STATUS.equalsIgnoreCase("true")){

                    JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                    System.out.println("JOBJECT_DATA: "+JOBJECT_DATA.toString());

                    if(JOBJECT_DATA!=null){

                        if(JOBJECT_DATA.has(TAG_JOBJECT_USER)){
                            JOBJECT_USER = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_USER);
                            System.out.println("JOBJECT_USER: "+JOBJECT_USER.toString());

                            //User Details
                            USERID = JOBJECT_USER.getString(TAG_USERID);
                            System.out.println("USERID: "+USERID);
                            USERISDCODE = JOBJECT_USER.getString(TAG_USERISDCODE);
                            System.out.println("USERISDCODE "+USERISDCODE);
                            USERREGIONID = JOBJECT_USER.getString(TAG_USERREGIONID);
                            System.out.println("USERREGIONID: "+USERREGIONID);
                            USERSTATEID = JOBJECT_USER.getString(TAG_USERSTATEID);
                            System.out.println("USERSTATEID: "+USERSTATEID);
                            USERCITYID = JOBJECT_USER.getString(TAG_USERCITYID);
                            System.out.println("USERCITYID: "+USERCITYID);
                            USERBRANCHID = JOBJECT_USER.getString(TAG_USERBRANCHID);
                            System.out.println("USERBRANCHID: "+USERBRANCHID);
                            USEROUTLETID = JOBJECT_USER.getString(TAG_USEROUTLETID);
                            System.out.println("USEROUTLETID: "+USEROUTLETID);
                            USERTLID = JOBJECT_USER.getString(TAG_USERTLID);
                            System.out.println("USERTLID: "+USERTLID);
                            USERFIRSTNAME = JOBJECT_USER.getString(TAG_USERFIRSTNAME);
                            System.out.println("USERFIRSTNAME: "+USERFIRSTNAME);
                            USERLASTNAME = JOBJECT_USER.getString(TAG_USERLASTNAME);
                            System.out.println("USERLASTNAME: "+USERLASTNAME);
                            USERMOBILENUMBER = JOBJECT_USER.getString(TAG_USERMOBILENUMBER);
                            System.out.println("USERMOBILENUMBER: "+USERMOBILENUMBER);
                            USEREMAILID = JOBJECT_USER.getString(TAG_USEREMAILID);
                            System.out.println("USEREMAILID: "+USEREMAILID);
                            USERGENDER = JOBJECT_USER.getString(TAG_USERGENDER);
                            System.out.println("USERGENDER: "+USERGENDER);
                            USERISDLEVEL = JOBJECT_USER.getString(TAG_USERISDLEVEL);
                            System.out.println("USERISDLEVEL: "+USERISDLEVEL);
                            USERROLEID = JOBJECT_USER.getString(TAG_USERROLEID);
                            System.out.println("USERROLEID: "+USERROLEID);
                            USERSTATUS = JOBJECT_USER.getString(TAG_USERSTATUS);
                            System.out.println("USERSTATUS: "+USERSTATUS);
                            USERREGIONNAME = JOBJECT_USER.getString(TAG_USERREGIONNAME);
                            System.out.println("USERREGIONNAME: "+USERREGIONNAME);
                            USERSTATENAME = JOBJECT_USER.getString(TAG_USERSTATENAME);
                            System.out.println("USERSTATENAME: "+USERSTATENAME);
                            USERCITYNAME = JOBJECT_USER.getString(TAG_USERCITYNAME);
                            System.out.println("USERCITYNAME: "+USERCITYNAME);
                            USERBRANCHNAME = JOBJECT_USER.getString(TAG_USERBRANCHNAME);
                            System.out.println("USERBRANCHNAME: "+USERBRANCHNAME);
                            USERROLE = JOBJECT_USER.getString(TAG_USERROLE);
                            System.out.println("USERROLE: "+USERROLE);
                            USERISDLEVELNAME = JOBJECT_USER.getString(TAG_USERISDLEVELNAME);
                            System.out.println("USERISDLEVELNAME: "+USERISDLEVELNAME);
                            USEROUTLETCODE = JOBJECT_USER.getString(TAG_USEROUTLETCODE);
                            System.out.println("USEROUTLETCODE: "+USEROUTLETCODE);
                            USEROUTLETNAME = JOBJECT_USER.getString(TAG_USEROUTLETNAME);
                            System.out.println("USEROUTLETNAME: "+USEROUTLETNAME);
                            USEROUTLETADDRESS = JOBJECT_USER.getString(TAG_USEROUTLETADDRESS);
                            System.out.println("USEROUTLETADDRESS: "+USEROUTLETADDRESS);
                            USEROUTLETLATITUDE = JOBJECT_USER.getString(TAG_USEROUTLETLATITUDE);
                            System.out.println("USEROUTLETLATITUDE: "+USEROUTLETLATITUDE);
                            USEROUTLETLONGITUDE = JOBJECT_USER.getString(TAG_USEROUTLETLONGITUDE);
                            System.out.println("USEROUTLETLONGITUDE: "+USEROUTLETLONGITUDE);
                            USEROUTLETGPSRANGE = JOBJECT_USER.getString(TAG_USEROUTLETGPSRANGE);
                            System.out.println("USEROUTLETGPSRANGE: "+USEROUTLETGPSRANGE);
                            USERCLIENTID = JOBJECT_USER.getString(TAG_USERCLIENTID);
                            System.out.println("USERCLIENTID: "+USERCLIENTID);
                            USERCLIENTCODE = JOBJECT_USER.getString(TAG_USERCLIENTCODE);
                            System.out.println("USERCLIENTCODE: "+USERCLIENTCODE);
                            USERCLIENTNAME = JOBJECT_USER.getString(TAG_USERCLIENTNAME);
                            System.out.println("USERCLIENTNAME: "+USERCLIENTNAME);
                            USERCLIENTLOGO = JOBJECT_USER.getString(TAG_USERCLIENTLOGO);
                            System.out.println("USERCLIENTLOGO: "+USERCLIENTLOGO);
                            KYC_STATUS = JOBJECT_USER.getString(TAG_KYC_STATUS);
                            System.out.println("KYC_STATUS: "+KYC_STATUS);

                            if(USEROUTLETLATITUDE.equalsIgnoreCase("null")||USEROUTLETLATITUDE==null){
                                USEROUTLETLATITUDE = "0.0";
                            }
                            if(USEROUTLETLONGITUDE.equalsIgnoreCase("null")||USEROUTLETLONGITUDE==null){
                                USEROUTLETLONGITUDE = "0.0";
                            }
                            if(USEROUTLETGPSRANGE.equalsIgnoreCase("null")||USEROUTLETGPSRANGE==null){
                                USEROUTLETGPSRANGE = "500.0";
                            }

                            Constants.UNIV_LAT = USEROUTLETLATITUDE;
                            Constants.UNIV_LONG = USEROUTLETLONGITUDE;
                            Constants.UNIV_RADIUS = USEROUTLETGPSRANGE;

                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_USER))

                        if(JOBJECT_DATA.has(TAG_JOBJECT_TEAM_LEAD)){
                            JOBJECT_TEAM_LEAD = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_TEAM_LEAD);
                            System.out.println("JOBJECT_TEAM_LEAD: "+JOBJECT_TEAM_LEAD.toString());

                            //Team Lead Details
                            TLID = JOBJECT_TEAM_LEAD.getString(TAG_TLID);
                            System.out.println("TLID: "+TLID);
                            TLISDCODE = JOBJECT_TEAM_LEAD.getString(TAG_TLISDCODE);
                            System.out.println("TLISDCODE: "+TLISDCODE);
                            TLOUTLETID = JOBJECT_TEAM_LEAD.getString(TAG_TLOUTLETID);
                            System.out.println("TLOUTLETID: "+TLOUTLETID);
                            TLFIRSTNAME = JOBJECT_TEAM_LEAD.getString(TAG_TLFIRSTNAME);
                            System.out.println("TLFIRSTNAME: "+TLFIRSTNAME);
                            TLLASTNAME = JOBJECT_TEAM_LEAD.getString(TAG_TLLASTNAME);
                            System.out.println("TLLASTNAME: "+TLLASTNAME);
                            TLMOBILENUMBER = JOBJECT_TEAM_LEAD.getString(TAG_TLMOBILENUMBER);
                            System.out.println("TLMOBILENUMBER: "+TLMOBILENUMBER);
                            TLEMAILID = JOBJECT_TEAM_LEAD.getString(TAG_TLEMAILID);
                            System.out.println("TLEMAILID: "+TLEMAILID);

                        }//if(JOBJECT_DATA.has(TAG_JOBJECT_TEAM_LEAD))

                    }//if(JOBJECT_DATA!=null){
                    else{
                        System.out.println("JOBJECT_DATA is Null");
                    }

                }//if(STATUS.equalsIgnoreCase("true"))


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
                    //showSuccessDialog();
                    Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    saveUserDetails();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){

                Toast.makeText(LoginActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();

            }

        }//handleMessage(Message msg)

    };

    private void saveUserDetails(){

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

        prefsEditor.putString("IMEIID", IMEIID);
        prefsEditor.putString("DEFAULTSOS", DEFAULTSOS);
        prefsEditor.putString("FIRSTSOS", FIRSTSOS);
        prefsEditor.putString("SECONDSOS", SECONDSOS);
        prefsEditor.putString("THIRDSOS", THIRDSOS);
        prefsEditor.putString("LOGGEDIN", "yes");
        prefsEditor.commit();

        if(KYC_STATUS.equalsIgnoreCase("yes")){
            Intent i = new Intent(LoginActivity.this,DashBoardActivity.class);
            LoginActivity.this.finish();
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);



        }
        else{
            Intent i = new Intent(LoginActivity.this,KYCActivity.class);
            LoginActivity.this.finish();
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }

    }

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(LoginActivity.this);
        aldb.setTitle("Login Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        showExitDialog();
    }

    //Exit Dialog
    public void showExitDialog(){

        final AlertDialog.Builder aldb = new AlertDialog.Builder(LoginActivity.this);
        aldb.setTitle("Smartrac");
        aldb.setMessage("Would you like to Exit now?");
        aldb.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                LoginActivity.this.finish();
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
