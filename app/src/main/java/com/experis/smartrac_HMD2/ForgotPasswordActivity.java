package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private EditText forgotPasswordPageISDEditTextID;
    private EditText forgotPasswordPageEmailEditTextID;
    private Button forgotPasswordSubmitBtnID;

    private ImageView forgotPasswordtopbarbackImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String ISD_CODE = "";
    private String EMAILID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);

        /*getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.forgot_password_topbar_title);*/

        /*PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        //Back Button
        forgotPasswordtopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Forgot Password Button Click
        forgotPasswordSubmitBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(CommonUtils.isInternelAvailable(ForgotPasswordActivity.this)){

                        validateData();
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }//onCreate()

    private void initAllViews(){
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        forgotPasswordPageISDEditTextID = (EditText)findViewById(R.id.forgotPasswordPageISDEditTextID);
        forgotPasswordPageEmailEditTextID = (EditText)findViewById(R.id.forgotPasswordPageEmailEditTextID);
        forgotPasswordSubmitBtnID = (Button)findViewById(R.id.forgotPasswordSubmitBtnID);

        forgotPasswordtopbarbackImageViewID = (ImageView)findViewById(R.id.forgotPasswordtopbarbackImageViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(forgotPasswordPageISDEditTextID.getText().toString()))
        {
            forgotPasswordPageISDEditTextID.setError("Required field!");
            focusView = forgotPasswordPageISDEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(forgotPasswordPageEmailEditTextID.getText().toString())){
            forgotPasswordPageEmailEditTextID.setError("Required field!");
            focusView = forgotPasswordPageEmailEditTextID;
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

        ISD_CODE = forgotPasswordPageISDEditTextID.getText().toString();
        EMAILID = forgotPasswordPageEmailEditTextID.getText().toString();

        if(!ISD_CODE.equalsIgnoreCase("")&&!EMAILID.equalsIgnoreCase("")){
            sendDataToRetrievePassword();
        }
        else{
            Toast.makeText(this, "Both the fields are mandatory!", Toast.LENGTH_SHORT).show();
        }
    }
    //For Retrieving Password
    private void sendDataToRetrievePassword(){

        //progressDialog.setTitle("Password Retrieval");
        progressDialog.setMessage("Resending your Password... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.FORGOT_PASSWORD_RELATIVE_URI);
        client.AddParam("isd_code", ISD_CODE);
        client.AddParam("email", EMAILID);
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

                //Sending Password success
                if(STATUS.equalsIgnoreCase("true")){
                    showSuccessDialog();
                }

                //Sending Password failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Sending Password failed
            if(client.responseCode!=200){
                //Toast.makeText(ForgotPasswordActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };


    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ForgotPasswordActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                //Intent i = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                ForgotPasswordActivity.this.finish();
                //startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();

    }
    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ForgotPasswordActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        //Intent i = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        ForgotPasswordActivity.this.finish();
        //startActivity(i);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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
