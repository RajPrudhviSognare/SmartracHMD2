package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

public class SOSActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView changePasswordtopbarbackImageViewID;

    private EditText changePasswordPageOldPasswordEditTextID;
    private EditText changePasswordPageNewPasswordEditTextID;
    private EditText changePasswordPageConfirmPasswordEditTextID;
    private Button changePasswordSubmitBtnID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String old_passwd = "";
    private String passwd = "";
    private String passwd1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.sos_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        //Topbar Back Button
        changePasswordtopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Submit Button Logic
        changePasswordSubmitBtnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();


            }
        });

    }//onCreate()

    private void initAllViews(){
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        changePasswordPageOldPasswordEditTextID = (EditText)findViewById(R.id.changePasswordPageOldPasswordEditTextID);
        changePasswordPageNewPasswordEditTextID = (EditText)findViewById(R.id.changePasswordPageNewPasswordEditTextID);
        changePasswordPageConfirmPasswordEditTextID = (EditText)findViewById(R.id.changePasswordPageConfirmPasswordEditTextID);
        changePasswordSubmitBtnID = (Button)findViewById(R.id.changePasswordSubmitBtnID);

        changePasswordtopbarbackImageViewID = (ImageView)findViewById(R.id.changePasswordtopbarbackImageViewID);

        changePasswordPageOldPasswordEditTextID.setText(prefs.getString("FIRSTSOS",""));
        changePasswordPageNewPasswordEditTextID.setText(prefs.getString("SECONDSOS",""));
        changePasswordPageConfirmPasswordEditTextID.setText(prefs.getString("THIRDSOS",""));

        //Progress Dialog
        progressDialog = new ProgressDialog(SOSActivity.this);
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(changePasswordPageOldPasswordEditTextID.getText().toString()))
        {
            changePasswordPageOldPasswordEditTextID.setError("Required field!");
            focusView = changePasswordPageOldPasswordEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(changePasswordPageNewPasswordEditTextID.getText().toString()))
        {
            changePasswordPageNewPasswordEditTextID.setError("Required field!");
            focusView = changePasswordPageNewPasswordEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(changePasswordPageConfirmPasswordEditTextID.getText().toString()))
        {
            changePasswordPageConfirmPasswordEditTextID.setError("Required field!");
            focusView = changePasswordPageConfirmPasswordEditTextID;
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

        old_passwd = changePasswordPageOldPasswordEditTextID.getText().toString();
        passwd = changePasswordPageNewPasswordEditTextID.getText().toString();
        passwd1 = changePasswordPageConfirmPasswordEditTextID.getText().toString();

        prefsEditor.putString("FIRSTSOS", old_passwd);
        prefsEditor.putString("SECONDSOS", passwd);
        prefsEditor.putString("THIRDSOS", passwd1);

        prefsEditor.commit();


        final AlertDialog.Builder aldb = new AlertDialog.Builder(SOSActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage("Numbers saved for SOS Service");
        aldb.setCancelable(false);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                SOSActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();



    }
    //For Changing the Password
    private void sendDataToSetPassword(){



        //progressDialog.setTitle("Password Retrieval");
        progressDialog.setMessage("Updating your Password... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.CHANGE_PASSWORD_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));
        client.AddParam("old_password", CommonUtils.md5(old_passwd));
        client.AddParam("new_password", CommonUtils.md5(passwd));
        client.AddParam("confirm_password", CommonUtils.md5(passwd1));
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

                //Updating Password success
                if(STATUS.equalsIgnoreCase("true")){
                    showSuccessDialog();
                }

                //Updating Password failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Updating Password failed
            if(client.responseCode!=200){
                //Toast.makeText(ForgotPasswordActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SOSActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                prefsEditor.putString("LOGGEDIN","no");
                prefsEditor.commit();
                SOSActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();

    }
    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SOSActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        SOSActivity.this.finish();
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
