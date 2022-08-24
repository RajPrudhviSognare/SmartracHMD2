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

public class ManageStocksActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private Spinner manageStockCategoryListSpinnerID;
    private Spinner manageStockSubCategoryListSpinnerID;
    private Spinner manageStockProductNameListSpinnerID;

    private EditText manageStockPageSKUCodeEditTextID;
    private EditText manageStockPageIMEINumber1EditTextID;
    //private EditText manageStockPageIMEINumber2EditTextID;

    private ImageView manageStockPageSubmitImageViewID;
    private ImageView managestocktopbarbackImageViewID;
    private ImageView managestocktopbarusericonImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_CATEGORYINFOLIST = "category";
    private String TAG_JARRAY_SUBCATEGORYINFOLIST = "subcategory";
    private String TAG_JARRAY_PRODUCTSINFOLIST = "product_info";

    private JSONObject JOBJECT_DATA = null;

    //CATEGORY DETAILS TAGS
    private String TAG_CATEGORYID = "id";
    private String TAG_CATEGORYNAME = "category_name";
    private List<String> category_idList = null;
    private List<String> category_nameList = null;

    //SUBCATEGORY DETAILS TAGS
    private String TAG_SUBCATEGORYID = "id";
    private String TAG_SUBCATEGORYNAME = "category_name";
    private List<String> subcategory_idList = null;
    private List<String> subcategory_nameList = null;

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTID = "product_id";
    private String TAG_PRODUCTNAME = "product_name";
    private String TAG_SKUCODE = "sku_code";
    private String TAG_JOBJECT_PRODUCTDETAILS = "productDtls";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;

    private String category_IDSelected = "";
    private String category_NameSelected = "";
    private String subcategory_IDSelected = "";
    private String subcategory_NameSelected = "";
    private String product_IDSelected = "";
    private String product_NameSelected = "";

    String skucode = "";
    String color = "";

    private ImageView attendancePagePhotoPreviewAreaImageViewID;
    private static final int REQ_IMAGE_CAPTURE = 1511;
    private Uri capturedImageUri = null;
    private String realpath = null;
    private int CAMERA_REQUEST  = 1111;
    private Bitmap bitmap = null;

    private String encodedImageIntoString = null;

    private  Spinner manageStockSubCategoryListSpinnerIDold;
    private List<String> subcategory_idListold = null;
    private List<String> subcategory_nameListold = null;
    private String TAG_JARRAY_SUBCATEGORYINFOLISTOLD = "outlet";
    private String TAG_SUBCATEGORYIDold = "id";
    private String TAG_OUTLETNAMEold = "outlet_name";
    private String TAG_OUTLETCODEold = "outlet_code";
    private String subcategory_IDSelectedold = "";
    private String subcategory_NameSelectedold = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_stock);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.manage_stock_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        if(CommonUtils.isInternelAvailable(ManageStocksActivity.this)){
            requestCategoryList();
        }
        else{
            Toast.makeText(ManageStocksActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }


        //SubCategory
        manageStockSubCategoryListSpinnerIDold.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subcategory_NameSelectedold = parent.getItemAtPosition(position).toString();
                subcategory_IDSelectedold = subcategory_idListold.get(position);
                System.out.println("subcategory_NameSelected: "+ subcategory_NameSelectedold);
                System.out.println("subcategory_IDSelected: "+ subcategory_IDSelectedold);
                if(position==0){
                }
                else{
                    if(CommonUtils.isInternelAvailable(ManageStocksActivity.this)){
                        requestProductCategoryList();
                    }
                    else{
                        Toast.makeText(ManageStocksActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }
                }





            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Category
        manageStockCategoryListSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category_NameSelected = parent.getItemAtPosition(position).toString();
                category_IDSelected = category_idList.get(position);
                System.out.println("category_NameSelected: "+ category_NameSelected);
                System.out.println("category_IDSelected: "+ category_IDSelected);
                if(position==0){
                }
                else{
                     requestSubCategoryList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SubCategory
        manageStockSubCategoryListSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subcategory_NameSelected = parent.getItemAtPosition(position).toString();
                subcategory_IDSelected = subcategory_idList.get(position);
                System.out.println("subcategory_NameSelected: "+ subcategory_NameSelected);
                System.out.println("subcategory_IDSelected: "+ subcategory_IDSelected);
                if(position==0){
                }
                else{
                    // requestProductCategoryList();
                    if(CommonUtils.isInternelAvailable(ManageStocksActivity.this)){
                        requestOutletList();
                    }
                    else{
                        Toast.makeText(ManageStocksActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Product
        manageStockProductNameListSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                product_NameSelected = parent.getItemAtPosition(position).toString();
                product_IDSelected = product_idList.get(position);
                System.out.println("product_NameSelected: "+ product_NameSelected);
                System.out.println("product_IDSelected: "+ product_IDSelected);
                if(position==0){
                }
                else{
                     requestProductInfo();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Submit Button
        manageStockPageSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(ManageStocksActivity.this)){

                        validateData();
                    }
                    else{
                        Toast.makeText(ManageStocksActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        attendancePagePhotoPreviewAreaImageViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCamera2();
            }
        });

        //Back Button
        managestocktopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        managestocktopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        managestocktopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageStocksActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        manageStockCategoryListSpinnerID = (Spinner)findViewById(R.id.manageStockCategoryListSpinnerID);
        manageStockSubCategoryListSpinnerID = (Spinner)findViewById(R.id.manageStockSubCategoryListSpinnerID);
        manageStockProductNameListSpinnerID = (Spinner)findViewById(R.id.manageStockProductNameListSpinnerID);
        manageStockPageIMEINumber1EditTextID=(EditText)findViewById(R.id.manageStockPageIMEINumber1EditTextID);
        manageStockPageSKUCodeEditTextID = (EditText)findViewById(R.id.manageStockPageSKUCodeEditTextID);
        attendancePagePhotoPreviewAreaImageViewID=(ImageView)findViewById(R.id.attendancePagePhotoPreviewAreaImageViewID);

        manageStockPageSubmitImageViewID = (ImageView)findViewById(R.id.manageStockPageSubmitImageViewID);
        managestocktopbarbackImageViewID = (ImageView)findViewById(R.id.managestocktopbarbackImageViewID);
        managestocktopbarusericonImageViewID = (ImageView)findViewById(R.id.managestocktopbarusericonImageViewID);
        manageStockSubCategoryListSpinnerIDold = (Spinner)findViewById(R.id.manageStockSubCategoryListSpinnerIDold);

        category_idList = new ArrayList<String>(0);
        category_nameList = new ArrayList<String>(0);
        subcategory_idList = new ArrayList<String>(0);
        subcategory_nameList = new ArrayList<String>(0);
        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(ManageStocksActivity.this);

        subcategory_idListold = new ArrayList<String>(0);
        subcategory_nameListold = new ArrayList<String>(0);

    }




    //All Sub Category Type
    private void requestOutletList(){
        subcategory_idListold.clear();
        subcategory_nameListold.clear();

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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_SUBCATEGORYINFOLISTOLD);
                        JSONObject c = null;
                        String id = null;
                        String name = null;

                        System.out.println("##########All SUBCATEGORY List details###################");

                        subcategory_idListold.clear();
                        subcategory_nameListold.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_SUBCATEGORYIDold);
                                        name = c.getString(TAG_OUTLETNAMEold)+"( "+c.getString(TAG_OUTLETCODEold)+" )";

                                        subcategory_idListold.add(id);
                                        subcategory_nameListold.add(name);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total subcategory_idList: " + subcategory_idListold.size());
                        System.out.println("Total subcategory_nameList: " + subcategory_nameListold.size());
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
                    showSubCategoryDetailsold();
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

    private void showSubCategoryDetailsold(){
        SetDropDownItemsForSubCategoryold();
    }
    private void SetDropDownItemsForSubCategoryold(){
        if(subcategory_nameListold.size()!=0){
            subcategory_idListold.add(0,"0");
            subcategory_nameListold.add(0,"Select Outlet");
            ArrayAdapter dataAdapter = new ArrayAdapter (ManageStocksActivity.this, android.R.layout.simple_spinner_item, subcategory_nameListold);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockSubCategoryListSpinnerIDold.setAdapter(dataAdapter);

            subcategory_IDSelectedold = subcategory_nameListold.get(0);
            System.out.println("subcategory_IDSelected Initially: "+ subcategory_IDSelectedold);

        }
    }



    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(manageStockPageIMEINumber1EditTextID.getText().toString()))
        {
            manageStockPageIMEINumber1EditTextID.setError("Required field!");
            focusView = manageStockPageIMEINumber1EditTextID;
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
       String count = manageStockPageIMEINumber1EditTextID.getText().toString();
       /*  String imei2 = manageStockPageIMEINumber2EditTextID.getText().toString();*/
String image=encodedImageIntoString;

        if(product_IDSelected!=""&&skucode!=""){

            encodedImageIntoString = null;
            attendancePagePhotoPreviewAreaImageViewID.setImageBitmap(null);
            attendancePagePhotoPreviewAreaImageViewID.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp));

            //progressDialog.setTitle("Sell Update");
            progressDialog.setMessage("Submitting... Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            client = new RestFullClient(Constants.BASE_URL+Constants.SETPRODUCT_IMEI_RELATIVE_URI);
            client.AddParam("product_id", product_IDSelected);
            client.AddParam("outlet_id", subcategory_IDSelectedold);
            client.AddParam("stock_count", count);
            /*client.AddParam("imei_no1", imei1);
            client.AddParam("imei_no2", imei2);*/

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

                    receiveDataForServerResponse4(client.jObj);
                    handler4.sendEmptyMessage(0);

                }

            }).start();
        }
        else{
            Toast.makeText(ManageStocksActivity.this, "Please add a image", Toast.LENGTH_LONG).show();
        }

    }
    private void receiveDataForServerResponse4(JSONObject jobj) {

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
    Handler handler4 = new Handler(){

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
                    //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showRetryDialog();
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(ManageStocksActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*ManageStocksActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/
                manageStockPageIMEINumber1EditTextID.setText("");
               // manageStockPageIMEINumber2EditTextID.setText("");
                //manageStockPageSKUCodeEditTextID.setText("");
                //skucode = "";
            }
        });

        aldb.show();
    }

    //Product Info
    private void requestProductInfo(){

        //progressDialog.setTitle("Product Info");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETPRODUCT_INFO_RELATIVE_URI);
        client.AddParam("product_id", product_IDSelected);
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

                receiveDataForServerResponse3(client.jObj);
                handler3.sendEmptyMessage(0);

            }

        }).start();

    }
    private void receiveDataForServerResponse3(JSONObject jobj){

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

                        JSONObject c = JOBJECT_DATA.getJSONObject(TAG_JOBJECT_PRODUCTDETAILS);
                        System.out.println("JSONObject c: " + c.toString());

                        System.out.println("##########All Product info details###################");

                        if(c!= null){
                            skucode = c.getString(TAG_SKUCODE);
                            //color = c.getString(TAG_PRODUCTNAME);
                        }//c!= null

                        System.out.println("skucode: " + skucode);
                        System.out.println("##########End Of All Product info details###################");


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
    Handler handler3 = new Handler(){

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
                    showProductInfo();
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
    private void showProductInfo(){
        manageStockPageSKUCodeEditTextID.setText("");
        manageStockPageSKUCodeEditTextID.setText(skucode);
    }

    //All Product Type
    private void requestProductCategoryList(){
        product_idList.clear();
        product_nameList.clear();

        //progressDialog.setTitle("Product Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETPRODUCT_RELATIVE_URI);
        client.AddParam("subcategory_id", subcategory_IDSelected);
        client.AddParam("outlet_id", subcategory_IDSelectedold);
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
    private void receiveDataForServerResponse2(JSONObject jobj){

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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_PRODUCTSINFOLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;

                        System.out.println("##########All Product List details###################");

                        product_idList.clear();
                        product_nameList.clear();

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

                                        product_idList.add(id);
                                        product_nameList.add(name);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total product_idList: " + product_idList.size());
                        System.out.println("Total product_nameList: " + product_nameList.size());
                        System.out.println("##########End Of All Product List details###################");


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

                //Toast.makeText(SignupActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showProductDetails();
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
    private void showProductDetails(){
        SetDropDownItemsForProducts();
    }
    private void SetDropDownItemsForProducts(){
        if(product_nameList.size()!=0){
            product_idList.add(0,"0");
            product_nameList.add(0,"Select Product");
            ArrayAdapter dataAdapter = new ArrayAdapter (ManageStocksActivity.this, android.R.layout.simple_spinner_item, product_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockProductNameListSpinnerID.setAdapter(dataAdapter);

            product_IDSelected = product_nameList.get(0);
            System.out.println("product_IDSelected Initially: "+ product_IDSelected);

        }
    }

    //All Sub Category Type
    private void requestSubCategoryList(){
        subcategory_idList.clear();
        subcategory_nameList.clear();

        //progressDialog.setTitle("SubCategory Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETSUBCATEGORY_RELATIVE_URI);
        client.AddParam("category_id", category_IDSelected);
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
                                        name = c.getString(TAG_SUBCATEGORYNAME);

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
                    //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showSubCategoryDetails();
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
            subcategory_nameList.add(0,"Select Sub-Category");
            ArrayAdapter dataAdapter = new ArrayAdapter (ManageStocksActivity.this, android.R.layout.simple_spinner_item, subcategory_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockSubCategoryListSpinnerID.setAdapter(dataAdapter);

            subcategory_IDSelected = subcategory_nameList.get(0);
            System.out.println("subcategory_IDSelected Initially: "+ subcategory_IDSelected);

        }
    }

    //All Category Type
    private void requestCategoryList(){
        category_idList.clear();
        category_nameList.clear();

        //progressDialog.setTitle("Category Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GETCATEGORY_RELATIVE_URI);
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_CATEGORYINFOLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;

                        System.out.println("##########All CATEGORY List details###################");

                        category_idList.clear();
                        category_nameList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_CATEGORYID);
                                        name = c.getString(TAG_CATEGORYNAME);

                                        category_idList.add(id);
                                        category_nameList.add(name);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total category_idList: " + category_idList.size());
                        System.out.println("Total category_nameList: " + category_nameList.size());
                        System.out.println("##########End Of All CATEGORY List details###################");


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
                    //Toast.makeText(ManageStocksActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showCategoryDetails();
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
    private void showCategoryDetails(){
        SetDropDownItemsForCategory();
    }
    private void SetDropDownItemsForCategory(){
        if(category_nameList.size()!=0){
            category_idList.add(0,"0");
            category_nameList.add(0,"Select Category");
            ArrayAdapter dataAdapter = new ArrayAdapter (ManageStocksActivity.this, android.R.layout.simple_spinner_item, category_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockCategoryListSpinnerID.setAdapter(dataAdapter);

            category_IDSelected = category_nameList.get(0);
            System.out.println("category_IDSelected Initially: "+ category_IDSelected);

        }
    }

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ManageStocksActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        ManageStocksActivity.this.finish();
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


}//ManageStocksActivity
