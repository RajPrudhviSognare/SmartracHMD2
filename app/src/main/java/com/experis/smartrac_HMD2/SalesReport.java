package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SalesReport extends AppCompatActivity {
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
    private Button btnSave;
    private EditText edtFrameNumber;
    private TextView lytFrameNumber;
    private EditText edtSoldPrice;
    public static final int REQUEST_SCANNER_TEXT = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        GeolocationService geolocationService = new GeolocationService();
        geolocationService.startLocationUpdates();
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();
        progressDialog = new ProgressDialog(SalesReport.this);
        btnBack = findViewById(R.id.employeeinfotopbarbackImageViewID);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        btnSave = findViewById(R.id.btn_assign);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (CommonUtils.isInternelAvailable(SalesReport.this)) {
                        submitCustomerSalesReport();
                    } else {
                        Toast.makeText(SalesReport.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        edtFrameNumber = findViewById(R.id.edt_frame_number);
        lytFrameNumber = findViewById(R.id.txt_frame_number);
        edtSoldPrice = findViewById(R.id.edt_sold_price);
        lytFrameNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesReport.this, ScannedBarcodeActivity.class);
                startActivityForResult(intent, REQUEST_SCANNER_TEXT);
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
        RestFullClient client = new RestFullClient(Constants.BASE_URL + Constants.SALES_REPORT);
        Log.e("associate_id", Constants.ASSOCIATE_ID);
        client.AddParam("associate_id", Constants.ASSOCIATE_ID);
        Log.e("associate_code", Constants.ASSOCIATE_CODE);
        client.AddParam("associate_code", Constants.ASSOCIATE_CODE);
        Log.e("sale_price", edtSoldPrice.getText().toString());
        client.AddParam("sale_price", edtSoldPrice.getText().toString());
        Log.e("product_details", edtFrameNumber.getText().toString());
        client.AddParam("product_details", edtFrameNumber.getText().toString());

        client.AddParam("latitude", Constants.UNIV_LAT);
        Log.e("latitude", Constants.UNIV_LAT);

        client.AddParam("longitude", Constants.UNIV_LAT);
        Log.e("longitude", Constants.UNIV_LAT);


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
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SalesReport.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + msg);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    private void showSuccessDialog(final String msg) {
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SalesReport.this);
        aldb.setMessage(msg);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();

            }
        });
        aldb.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String resultString = data.getStringExtra("SCANNED_TEXT");
            edtFrameNumber.setText(resultString);
        }

    }

}