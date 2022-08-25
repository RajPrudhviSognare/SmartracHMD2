package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView btnClick;
    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";
    private ImageView btnBack;
    private TextView txtHeading;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();
        progressDialog = new ProgressDialog(MainActivity.this);
        btnClick = findViewById(R.id.txt_frame_number);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CommonUtils.isInternelAvailable(MainActivity.this)) {
                        submitCustomerSalesReport();
                    } else {
                        Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submitCustomerSalesReport() {
        Constants.ASSOCIATE_ID = prefs.getString("USERID", "");
        Constants.ASSOCIATE_CODE = prefs.getString("USERISDCODE", "");
        Constants.OUTLET_ID = prefs.getString("USEROUTLETID", "");
        progressDialog.setMessage("Saving details... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RestFullClient client = new RestFullClient(Constants.BASE_URL + Constants.SAVE_INPUT);

//        Log.e("associate_id", Constants.ASSOCIATE_ID);
//        client.AddParam("associate_id", Constants.ASSOCIATE_ID);

        Log.e("outlet_code", "1234");
        client.AddParam("outlet_code", "1234");

        Log.e("device_details", "NOKIA C3");
        client.AddParam("device_details", "NOKIA C3");
        String imei = "";


//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        try {
//            imei = telephonyManager.getDeviceId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Log.e("imie", imei);
        client.AddParam("imie", "1234567");

        client.AddParam("latitude", Constants.UNIV_LAT);
        Log.e("latitude", Constants.UNIV_LAT);

        client.AddParam("longitude", Constants.UNIV_LONG);
        Log.e("longitude", Constants.UNIV_LONG);


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

                receiveDataForServerResponse(client, client.jObj);

                handler.sendEmptyMessage(0);

            }

        }).start();
    }

    private void receiveDataForServerResponse(RestFullClient client, JSONObject jobj) {
        Log.e("Response rajprudhvi", "receiveDataForServerResponse: " + jobj);
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

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            try {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                //Success
                if (STATUS.equalsIgnoreCase("true")) {
                    showSuccessDialog(MESSAGE);
                    //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                }
                //Failed
                if (STATUS.equalsIgnoreCase("false")) {
                    //showRetryDialog();
                    showFailureDialog(MESSAGE);
                }

            } catch (final Exception e) {
                e.printStackTrace();
            }

        }//handleMessage(Message msg)

    };

    private void showFailureDialog(String msg) {
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(MainActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + msg);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    private void showSuccessDialog(final String msg) {
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(MainActivity.this);
        aldb.setMessage(msg);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

            }
        });
        aldb.show();
    }

}