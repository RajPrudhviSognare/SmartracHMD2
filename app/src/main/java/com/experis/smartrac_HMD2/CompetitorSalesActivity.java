package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CompetitorSalesActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private EditText competitorSalesCompetitorListsSpinner;
    private EditText competitorSalesPageModelNameEditTextID;
    private EditText competitorSalesPageSalePriceEditTextID;
    private EditText competitorSalesPageCustomerNameEditTextID;
    private EditText competitorSalesPageMobileNumberEditTextID;
    private EditText competitorSalesPageEmailEditTextID;

    private ImageView competitorSalesPageSubmitImageViewID;
    private ImageView competitorsalestopbarbackImageViewID;
    private ImageView competitorsalestopbarusericonImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_COMPETITORINFOLIST = "competitor_info";

    //Competitor DETAILS TAGS
    private String TAG_COMPETITORID = "id";
    private String TAG_COMPETITORNAME = "competitor_name";

    private JSONObject JOBJECT_DATA = null;

    private List<String> competitor_idList = null;
    private List<String> competitor_nameList = null;

    private String comp_IDSelected = "";
    private String comp_NameSelected = "";

    private ImageView attendancePagePhotoPreviewAreaImageViewID;
    private static final int REQ_IMAGE_CAPTURE = 1511;
    private Uri capturedImageUri = null;
    private String realpath = null;
    private int CAMERA_REQUEST  = 1111;
    private Bitmap bitmap = null;

    private String encodedImageIntoString = null;
    
    private  Spinner manageStockSubCategoryListSpinnerID;
    private List<String> subcategory_idList = null;
    private List<String> subcategory_nameList = null;
    private String TAG_JARRAY_SUBCATEGORYINFOLIST = "outlet";
    private String TAG_SUBCATEGORYID = "id";
    private String TAG_OUTLETNAME = "outlet_name";
    private String TAG_OUTLETCODE = "outlet_code";
    private String subcategory_IDSelected = "";
    private String subcategory_NameSelected = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_sales);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.competitor_sales_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

       /* if(CommonUtils.isInternelAvailable(CompetitorSalesActivity.this)){
            requestCompetitorList();
        }
        else{
            Toast.makeText(CompetitorSalesActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }*/

        /*competitorSalesCompetitorListsSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                comp_NameSelected = parent.getItemAtPosition(position).toString();
                comp_IDSelected = competitor_idList.get(position);
                System.out.println("comp_NameSelected: "+ comp_NameSelected);
                System.out.println("comp_IDSelected: "+ comp_IDSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //SubCategory
        manageStockSubCategoryListSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subcategory_NameSelected = parent.getItemAtPosition(position).toString();
                subcategory_IDSelected = subcategory_idList.get(position);
                System.out.println("subcategory_NameSelected: "+ subcategory_NameSelected);
                System.out.println("subcategory_IDSelected: "+ subcategory_IDSelected);
                /*if(position==0){
                }
                else{
                    requestProductCategoryList();
                }*/


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        attendancePagePhotoPreviewAreaImageViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera2();
            }
        });

        //Submit Button
        competitorSalesPageSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(CompetitorSalesActivity.this)){

                        //sendSellingData();
                        validateData();
                    }
                    else{
                        Toast.makeText(CompetitorSalesActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //Back Button
        competitorsalestopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        competitorsalestopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        competitorsalestopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(CompetitorSalesActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });



    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        competitorSalesCompetitorListsSpinner = (EditText)findViewById(R.id.competitorSalesCompetitorListsSpinner);
        attendancePagePhotoPreviewAreaImageViewID=(ImageView)findViewById(R.id.attendancePagePhotoPreviewAreaImageViewID);
        competitorSalesPageModelNameEditTextID = (EditText)findViewById(R.id.competitorSalesPageModelNameEditTextID);
        competitorSalesPageSalePriceEditTextID = (EditText)findViewById(R.id.competitorSalesPageSalePriceEditTextID);
        competitorSalesPageCustomerNameEditTextID = (EditText)findViewById(R.id.competitorSalesPageCustomerNameEditTextID);
        competitorSalesPageMobileNumberEditTextID = (EditText)findViewById(R.id.competitorSalesPageMobileNumberEditTextID);
        competitorSalesPageEmailEditTextID = (EditText)findViewById(R.id.competitorSalesPageEmailEditTextID);

        competitorSalesPageSubmitImageViewID = (ImageView)findViewById(R.id.competitorSalesPageSubmitImageViewID);
        competitorsalestopbarbackImageViewID = (ImageView)findViewById(R.id.competitorsalestopbarbackImageViewID);
        competitorsalestopbarusericonImageViewID = (ImageView)findViewById(R.id.competitorsalestopbarusericonImageViewID);
        manageStockSubCategoryListSpinnerID = (Spinner)findViewById(R.id.manageStockSubCategoryListSpinnerID);

        competitor_idList = new ArrayList<String>(0);
        competitor_nameList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(CompetitorSalesActivity.this);
        subcategory_idList = new ArrayList<String>(0);
        subcategory_nameList = new ArrayList<String>(0);
        if(CommonUtils.isInternelAvailable(CompetitorSalesActivity.this)){
            requestOutletList();
        }
        else{
            Toast.makeText(CompetitorSalesActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }


    }



    //All Sub Category Type
    private void requestOutletList(){
        subcategory_idList.clear();
        subcategory_nameList.clear();

        //progressDialog.setTitle("SubCategory Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETOUTLET_RELATIVE_URI);

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

                receiveDataForServerResponse5(client.jObj);
                handler5.sendEmptyMessage(0);

            }

        }).start();

    }
    private void receiveDataForServerResponse5(JSONObject jobj){

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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_SUBCATEGORYINFOLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;

                        System.out.println("##########All SUBCATEGORY List details###################");

                        subcategory_idList.clear();
                        subcategory_nameList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_SUBCATEGORYID);
                                        name = c.getString(TAG_OUTLETNAME)+"( "+c.getString(TAG_OUTLETCODE)+" )";

                                        subcategory_idList.add(id);
                                        subcategory_nameList.add(name);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total subcategory_idList: " + subcategory_idList.size());
                        System.out.println("Total subcategory_nameList: " + subcategory_nameList.size());
                        System.out.println("##########End Of All SUBCATEGORY List details###################");


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
    Handler handler5 = new Handler(){

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
                    //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showSubCategoryDetails();
                    // requestProductCategoryList();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){
                //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    private void showSubCategoryDetails(){
        SetDropDownItemsForSubCategory();
    }
    private void SetDropDownItemsForSubCategory(){
        if(subcategory_nameList.size()!=0){
            subcategory_idList.add(0,"0");
            subcategory_nameList.add(0,"Select Outlet");
            ArrayAdapter dataAdapter = new ArrayAdapter (CompetitorSalesActivity.this, android.R.layout.simple_spinner_item, subcategory_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockSubCategoryListSpinnerID.setAdapter(dataAdapter);

            subcategory_IDSelected = subcategory_nameList.get(0);
            System.out.println("subcategory_IDSelected Initially: "+ subcategory_IDSelected);

        }
    }


    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;
        comp_IDSelected=competitorSalesCompetitorListsSpinner.getText().toString();

        if(TextUtils.isEmpty(competitorSalesPageModelNameEditTextID.getText().toString()))
        {
            competitorSalesPageModelNameEditTextID.setError("Required field!");
            focusView = competitorSalesPageModelNameEditTextID;
            cancel = true;
        }
        if(TextUtils.isEmpty(competitorSalesCompetitorListsSpinner.getText().toString()))
        {
            competitorSalesCompetitorListsSpinner.setError("Required field!");
            focusView = competitorSalesCompetitorListsSpinner;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        else
        {
            sendSellingData();
        }

    }//validateData

    private void sendSellingData(){
        String modelName = competitorSalesPageModelNameEditTextID.getText().toString();
        String salePrice = competitorSalesPageSalePriceEditTextID.getText().toString();
        String cName = competitorSalesPageCustomerNameEditTextID.getText().toString();
        String pNumber = competitorSalesPageMobileNumberEditTextID.getText().toString();
        String email = competitorSalesPageEmailEditTextID.getText().toString();

        String image=encodedImageIntoString;

        if(!image.equalsIgnoreCase("")){

            //progressDialog.setTitle("Sell Update");
            progressDialog.setMessage("Submitting... Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            client = new RestFullClient(Constants.BASE_URL+Constants.SETCOMPETITORSALES_RELATIVE_URI);
            client.AddParam("outlet_id", subcategory_IDSelected);
            client.AddParam("associate_id", prefs.getString("USERID",""));
            client.AddParam("competitor_name", comp_IDSelected);
            client.AddParam("model_name", modelName);
            client.AddParam("sale_price", salePrice);
            client.AddParam("customer_name", cName);
            client.AddParam("mobile", pNumber);
            client.AddParam("email", email);
            client.AddParam("image", image);

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
        else{
            Toast.makeText(CompetitorSalesActivity.this, "Add Image!", Toast.LENGTH_SHORT).show();
        }

    }

    private void receiveDataForServerResponse1(JSONObject jobj) {

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

                //Success
                if(STATUS.equalsIgnoreCase("true")){
                    showSuccessDialog();
                    //Toast.makeText(CompetitorSalesActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showRetryDialog();
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(CompetitorSalesActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(CompetitorSalesActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CompetitorSalesActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();

    }


    private void requestCompetitorList(){
        competitor_idList.clear();
        competitor_nameList.clear();

        //progressDialog.setTitle("Competitor Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETCOMPETITORLIST_RELATIVE_URI);
        client.AddParam("outlet_id", prefs.getString("USEROUTLETID",""));
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_COMPETITORINFOLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;

                        System.out.println("##########All Competitor List details###################");

                        competitor_idList.clear();
                        competitor_nameList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_COMPETITORID);
                                        name = c.getString(TAG_COMPETITORNAME);

                                        competitor_idList.add(id);
                                        competitor_nameList.add(name);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total competitor_idList: " + competitor_idList.size());
                        System.out.println("Total competitor_nameList: " + competitor_nameList.size());
                        System.out.println("##########End Of All Competitor List details###################");


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
                    //Toast.makeText(CompetitorSalesActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                  //  showAttendanceDetails();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){
                //Toast.makeText(CompetitorSalesActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

   /* private void showAttendanceDetails(){
        SetDropDownItems();
    }*/

   /* private void SetDropDownItems(){
        if(competitor_nameList.size()!=0){
            ArrayAdapter dataAdapter = new ArrayAdapter (CompetitorSalesActivity.this, android.R.layout.simple_spinner_item, competitor_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            competitorSalesCompetitorListsSpinner.setAdapter(dataAdapter);

            comp_IDSelected = competitor_idList.get(0);
            System.out.println("comp_IDSelected Initially: "+ comp_IDSelected);

        }
    }*/

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(CompetitorSalesActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }


    @Override
    public void onBackPressed()
    {
        CompetitorSalesActivity.this.finish();
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

    private void callCamera2(){
      /*  Intent intent = new Intent(getActivity(), ImageCapture.class);
        intent.putExtra("ATTENDANCE_TYPE","in");
        getActivity().startActivity(intent);*/
        //getActivity().startActivityForResult(intent,CAMERA_REQUEST1);
        int checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "Permission Granted");
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            } else {
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            }
*/

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE);
            }
        } else {
            Log.d("PERMISSION", "Permission Denied");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){


            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            attendancePagePhotoPreviewAreaImageViewID.setImageBitmap(imageBitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encodedImageIntoString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        }
        if(resultCode == RESULT_CANCELED){

            if (requestCode == CAMERA_REQUEST) {
                capturedImageUri = null;
                realpath = null;
                System.out.println("capturedImageUri: "+capturedImageUri);
                System.out.println("realpath: "+realpath);
            }

        }

    }

}//CompetitorSalesActivity
