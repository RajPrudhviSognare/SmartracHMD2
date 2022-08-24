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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SalesTrackingActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

   // private EditText salesTrackingPageIMEIEditTextID;
    private Button salesTrackingPagecheckIMEIEBtnID;
    private TextView salesTrackingPageProductNameTextViewID;
    private TextView salesTrackingPageProductCategoryTextViewID;
    private TextView salesTrackingPageProductSubCategoryTextViewID;
    private TextView salesTrackingPageProductColorTextViewID;
    private TextView salesTrackingPageProductSKUTextViewID,salesTrackingPageProductDESCTextViewID,salesTrackingPageProductWEIGHTTextViewID,salesTrackingPageProductUNITTextViewID;

    private EditText salesTrackingPageSalePriceEditTextID;
    private EditText salesTrackingPageCustomerNameEditTextID;
    private EditText salesTrackingPagePhoneNumberEditTextID;
    private EditText salesTrackingPageEmailEditTextID,salesTrackingPageSaleQuantityEditTextID;

    private ImageView salesTrackingPageSubmitImageViewID;
    private ImageView managedailystocktopbarbackImageViewID;
    private ImageView managedailystocktopbarusericonImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_PRODUCTINFOLIST = "product_info";

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTDESCRIPTION = "product_description";
    private String TAG_PRODUCTID = "product_id";
    private String TAG_OUTLETID = "outlet_id";
    private String TAG_PRODUCTPRICE = "price";
    private String TAG_PRODUCTSTOCK = "available_stock";
    private String TAG_PRODUCTNAME = "product_name";
    private String TAG_CATEGORYID = "category_id";
    private String TAG_SKUCODE = "sku_code";
    private String TAG_PRODUCTWEIGHT = "weight";
    private String TAG_PRODUCTUNIT = "unit";
    private String TAG_SUBCATEGORY = "sub_category";
    private String TAG_CATEGORY = "category";

    private JSONObject JOBJECT_DATA = null;

    private String PRODUCTDESC = "";
    private String PRODUCTID = "";
    private String OUTLETID = "";
    private String PRODUCTPRICE = "";
    private String PRODUCTSTOCK = "";
    private String PRODUCTNAME = "";
    private String CATEGORYID = "";
    private String SKUCODE = "";
    private String PRODUCTWEIGHT = "";
    private String SUBCATEGORY = "";
    private String CATEGORY = "";
    private String PRODUCTUNIT="";

    private String imeiFromUser = "";

    private LinearLayout salesTrackingPageProductDetailsLayoutID;
    private LinearLayout salesTrackingPageSaleInfoDetailsLayoutID;

    private ImageView attendancePagePhotoPreviewAreaImageViewID;
    private static final int REQ_IMAGE_CAPTURE = 1511;
    private Uri capturedImageUri = null;
    private String realpath = null;
    private int CAMERA_REQUEST  = 1111;
    private Bitmap bitmap = null;

    private String encodedImageIntoString = null;
    private Spinner manageStockSubCategoryListSpinnerID;
    private Spinner manageStockProductNameListSpinnerID;

    private String TAG_JARRAY_SUBCATEGORYINFOLIST = "outlet";
    private String TAG_JARRAY_PRODUCTSINFOLIST = "product_info";
    //SUBCATEGORY DETAILS TAGS
    private String TAG_SUBCATEGORYID = "id";
    private String TAG_OUTLETNAME = "outlet_name";
    private String TAG_OUTLETCODE = "outlet_code";
    private List<String> subcategory_idList = null;
    private List<String> subcategory_nameList = null;

    //PRODUCT DETAILS TAGS
  /*  private String TAG_PRODUCTID = "id";
    private String TAG_PRODUCTNAME = "product_name";
    private String TAG_SKUCODE = "sku_code";*/
    private String TAG_JOBJECT_PRODUCTDETAILS = "productDtls";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;

    private String category_IDSelected = "";
    private String category_NameSelected = "";
    private String subcategory_IDSelected = "";
    private String subcategory_NameSelected = "";
    private String product_IDSelected = "";
    private String product_NameSelected = "";
    private  double saleprice=0.00;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_tracking);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.sales_tracking_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        if(CommonUtils.isInternelAvailable(SalesTrackingActivity.this)){
            requestOutletList();
        }
        else{
            Toast.makeText(SalesTrackingActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }



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
                    requestProductCategoryList();
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
                /*if(position==0){
                }
                else{
                    requestProductInfo();
                }*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Check IMEI
        salesTrackingPagecheckIMEIEBtnID.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(SalesTrackingActivity.this)){

                        validateData();
                    }
                    else{
                        Toast.makeText(SalesTrackingActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
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




        //Submit
        salesTrackingPageSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(SalesTrackingActivity.this)){

                        sendSellingData();
                    }
                    else{
                        Toast.makeText(SalesTrackingActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //Back Button
        managedailystocktopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        managedailystocktopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        managedailystocktopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SalesTrackingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

       // salesTrackingPageIMEIEditTextID = (EditText)findViewById(R.id.salesTrackingPageIMEIEditTextID);
        manageStockSubCategoryListSpinnerID = (Spinner)findViewById(R.id.manageStockSubCategoryListSpinnerID);
        manageStockProductNameListSpinnerID = (Spinner)findViewById(R.id.manageStockProductNameListSpinnerID);
        salesTrackingPagecheckIMEIEBtnID = (Button)findViewById(R.id.salesTrackingPagecheckIMEIEBtnID);
        attendancePagePhotoPreviewAreaImageViewID=(ImageView)findViewById(R.id.attendancePagePhotoPreviewAreaImageViewID);
        salesTrackingPageProductNameTextViewID = (TextView)findViewById(R.id.salesTrackingPageProductNameTextViewID);
        salesTrackingPageProductCategoryTextViewID = (TextView)findViewById(R.id.salesTrackingPageProductCategoryTextViewID);
        salesTrackingPageProductSubCategoryTextViewID = (TextView)findViewById(R.id.salesTrackingPageProductSubCategoryTextViewID);
        salesTrackingPageProductColorTextViewID = (TextView)findViewById(R.id.salesTrackingPageProductColorTextViewID);
        salesTrackingPageProductSKUTextViewID = (TextView)findViewById(R.id.salesTrackingPageProductSKUTextViewID);

        salesTrackingPageSalePriceEditTextID = (EditText)findViewById(R.id.salesTrackingPageSalePriceEditTextID);
        salesTrackingPageSaleQuantityEditTextID=(EditText)findViewById(R.id.salesTrackingPageSaleQuantityEditTextID);
        salesTrackingPageCustomerNameEditTextID = (EditText)findViewById(R.id.salesTrackingPageCustomerNameEditTextID);
        salesTrackingPagePhoneNumberEditTextID = (EditText)findViewById(R.id.salesTrackingPagePhoneNumberEditTextID);
        salesTrackingPageEmailEditTextID = (EditText)findViewById(R.id.salesTrackingPageEmailEditTextID);

        salesTrackingPageSubmitImageViewID = (ImageView)findViewById(R.id.salesTrackingPageSubmitImageViewID);
        managedailystocktopbarbackImageViewID = (ImageView)findViewById(R.id.managedailystocktopbarbackImageViewID);
        managedailystocktopbarusericonImageViewID = (ImageView)findViewById(R.id.managedailystocktopbarusericonImageViewID);
        salesTrackingPageProductDESCTextViewID=(TextView)findViewById(R.id.salesTrackingPageProductDESCTextViewID);
        salesTrackingPageProductWEIGHTTextViewID=(TextView)findViewById(R.id.salesTrackingPageProductWEIGHTTextViewID);
        salesTrackingPageProductUNITTextViewID=(TextView)findViewById(R.id.salesTrackingPageProductUNITTextViewID);

        salesTrackingPageProductDetailsLayoutID = (LinearLayout)findViewById(R.id.salesTrackingPageProductDetailsLayoutID);
        salesTrackingPageSaleInfoDetailsLayoutID = (LinearLayout)findViewById(R.id.salesTrackingPageSaleInfoDetailsLayoutID);
        subcategory_idList = new ArrayList<String>(0);
        subcategory_nameList = new ArrayList<String>(0);
        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);
        salesTrackingPageSaleQuantityEditTextID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus) {
                    String q = salesTrackingPageSaleQuantityEditTextID.getText().toString();
                    int qu = 0;
                    double pr = 0;
                    if (!q.equalsIgnoreCase("") && !PRODUCTPRICE.equalsIgnoreCase("")) {
                        qu = Integer.parseInt(q);
                        pr = Double.parseDouble(PRODUCTPRICE);
                        saleprice = qu * pr;
                        salesTrackingPageSalePriceEditTextID.setText(String.valueOf(saleprice));
                    } else {
                        saleprice = 0;
                        salesTrackingPageSalePriceEditTextID.setText("0");
                    }
                }
                else{

                }
            }
        });

        //Progress Dialog
        progressDialog = new ProgressDialog(SalesTrackingActivity.this);
    }

    private void sendSellingData(){


        String quantity=salesTrackingPageSaleQuantityEditTextID.getText().toString();
        String cName = salesTrackingPageCustomerNameEditTextID.getText().toString();
        String pNumber = salesTrackingPagePhoneNumberEditTextID.getText().toString();
        String email = salesTrackingPageEmailEditTextID.getText().toString();
        String salingPrice = salesTrackingPageSalePriceEditTextID.getText().toString();
        String image=encodedImageIntoString;



        /*salesTrackingPageProductNameTextViewID.setText("");
        salesTrackingPageProductCategoryTextViewID.setText("");
        salesTrackingPageProductSubCategoryTextViewID.setText("");
        salesTrackingPageProductColorTextViewID.setText("");
        salesTrackingPageProductSKUTextViewID.setText("");*/

        if(!image.equalsIgnoreCase("")||salesTrackingPageProductNameTextViewID.getText().toString()!=""||salesTrackingPageProductCategoryTextViewID.getText().toString()!=""||
                salesTrackingPageProductSubCategoryTextViewID.getText().toString()!=""||salesTrackingPageProductColorTextViewID.getText().toString()!=""||
                salesTrackingPageProductSKUTextViewID.getText().toString()!=""){


            encodedImageIntoString = null;
            attendancePagePhotoPreviewAreaImageViewID.setImageBitmap(null);
            attendancePagePhotoPreviewAreaImageViewID.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp));

            //progressDialog.setTitle("Sell Update");
            progressDialog.setMessage("Processing... Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            client = new RestFullClient(Constants.BASE_URL+Constants.SALES_TRACKING_SET_SELLINFO_RELATIVE_URI);
            client.AddParam("outlet_id", OUTLETID);
            client.AddParam("associate_id", prefs.getString("USERID",""));
            client.AddParam("product_id", PRODUCTID);
            client.AddParam("quantity", quantity);
            client.AddParam("sale_price", salingPrice);
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

        }//if
        else{
            Toast.makeText(SalesTrackingActivity.this, "No product to sell!", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(SalesTrackingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showRetryDialog();
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(SalesTrackingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(SalesTrackingActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SalesTrackingActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();

    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(subcategory_IDSelected))
        {

          //  salesTrackingPageIMEIEditTextID.setError("IMEI Number Can't Be Empty");
          //  focusView = salesTrackingPageIMEIEditTextID;
            Toast.makeText(this, "Please select Outlet first!", Toast.LENGTH_SHORT).show();
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




        if(!product_IDSelected.equalsIgnoreCase("")){

            sendDataForProductDetails();
        }
        else{

            Toast.makeText(this, "Please select product first!", Toast.LENGTH_SHORT).show();
        }
    }

    //For Product Details
    private void sendDataForProductDetails(){

        //progressDialog.setTitle("Product Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SALES_TRACKING_GETPRODUCT_BYIMEI_RELATIVE_URI);
        client.AddParam("product_id", product_IDSelected);
        client.AddParam("outlet_id", subcategory_IDSelected);
       // client.AddParam("imei_no", imeiFromUser);

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

                        JSONArray productArray = JOBJECT_DATA.getJSONArray("product_details");
                        System.out.println("productArray List: " + productArray.toString());
                        System.out.println("Total productArray List: " + productArray.length());

                        JSONObject c = null;
                        /*String PRODUCTIMEIID1 = null;
                        String PRODUCTID1 = null;
                        String OUTLETID1 = null;
                        String IMEIID11 = null;
                        String IMEIID21 = null;
                        String PRODUCTNAME1 = null;
                        String CATEGORYID1 = null;
                        String SKUCODE1 = null;
                        String COLOR1 = null;
                        String SUBCATEGORY1 = null;
                        String CATEGORY1 = null;*/

                        System.out.println("##########All Products List details###################");

                        if(productArray.length()==0){
                            //salesTrackingPageProductDetailsLayoutID.setVisibility(View.GONE);
                            //salesTrackingPageSaleInfoDetailsLayoutID.setVisibility(View.GONE);
                        }
                        if(productArray.length()!=0){
                            /*salesTrackingPageProductDetailsLayoutID.setVisibility(View.VISIBLE);
                            salesTrackingPageSaleInfoDetailsLayoutID.setVisibility(View.VISIBLE);*/
                            for(int i = 0; i < productArray.length(); i++) {

                                try {
                                    c = productArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        PRODUCTDESC = c.optString(TAG_PRODUCTDESCRIPTION);
                                        PRODUCTID = c.optString(TAG_PRODUCTID);
                                        OUTLETID = c.optString(TAG_OUTLETID);
                                        PRODUCTPRICE = c.optString(TAG_PRODUCTPRICE);
                                        PRODUCTSTOCK = c.optString(TAG_PRODUCTSTOCK);
                                        PRODUCTNAME = c.optString(TAG_PRODUCTNAME);
                                        CATEGORYID = c.optString(TAG_CATEGORYID);
                                        SKUCODE = c.optString(TAG_SKUCODE);
                                        PRODUCTWEIGHT = c.optString(TAG_PRODUCTWEIGHT)+" "+c.optString(TAG_PRODUCTUNIT);
                                        PRODUCTUNIT = c.optString("available_stock");
                                        SUBCATEGORY = c.optString(TAG_SUBCATEGORY);
                                        CATEGORY = c.optString(TAG_CATEGORY);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("PRODUCTDESC: " +PRODUCTDESC);
                        System.out.println("PRODUCTID: " +PRODUCTID);
                        System.out.println("OUTLETID: " +OUTLETID);
                        System.out.println("PRODUCTPRICE: " +PRODUCTPRICE);
                        System.out.println("PRODUCTSTOCK: " +PRODUCTSTOCK);
                        System.out.println("PRODUCTNAME: " +PRODUCTNAME);
                        System.out.println("CATEGORYID: " +CATEGORYID);
                        System.out.println("SKUCODE: " +SKUCODE);
                        System.out.println("PRODUCTWEIGHT: " +PRODUCTWEIGHT);
                        System.out.println("SUBCATEGORY: " +SUBCATEGORY);
                        System.out.println("CATEGORY: " +CATEGORY);

                        System.out.println("##########End Of All Pending Date List details###################");

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
                    //showSuccessDialog();
                    //Toast.makeText(SalesTrackingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showProductDetails1();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client.responseCode!=200){

                //Toast.makeText(SalesTrackingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();

            }

        }//handleMessage(Message msg)

    };


    private void showProductDetails1(){

        if(!PRODUCTNAME.equalsIgnoreCase("")||!CATEGORYID.equalsIgnoreCase("")||!SUBCATEGORY.equalsIgnoreCase("")||!PRODUCTWEIGHT.equalsIgnoreCase("")||!SKUCODE.equalsIgnoreCase("")){

            salesTrackingPageProductDetailsLayoutID.setVisibility(View.VISIBLE);
            salesTrackingPageSaleInfoDetailsLayoutID.setVisibility(View.VISIBLE);

            salesTrackingPageProductNameTextViewID.setText("");
            salesTrackingPageProductCategoryTextViewID.setText("");
            salesTrackingPageProductSubCategoryTextViewID.setText("");
            salesTrackingPageProductColorTextViewID.setText("");
            salesTrackingPageProductSKUTextViewID.setText("");
            salesTrackingPageProductDESCTextViewID.setText("");
            salesTrackingPageProductWEIGHTTextViewID.setText("");
            salesTrackingPageProductUNITTextViewID.setText("");

            salesTrackingPageProductNameTextViewID.setText(PRODUCTNAME);
            salesTrackingPageProductCategoryTextViewID.setText(CATEGORY);
            salesTrackingPageProductSubCategoryTextViewID.setText(SUBCATEGORY);
            salesTrackingPageProductColorTextViewID.setText(PRODUCTPRICE);
            salesTrackingPageProductSKUTextViewID.setText(SKUCODE);
            salesTrackingPageProductDESCTextViewID.setText(PRODUCTDESC);
            salesTrackingPageProductWEIGHTTextViewID.setText(PRODUCTWEIGHT);
            salesTrackingPageProductUNITTextViewID.setText(PRODUCTUNIT);

        }
        else{
             //Toast.makeText(SalesTrackingActivity.this, "Invalid IMEI Code, Try again.", Toast.LENGTH_SHORT).show();
             showInvalidDialog();
        }

    }

    private void showInvalidDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SalesTrackingActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("Invalid IMEI Code");
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SalesTrackingActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();

    }

    @Override
    public void onBackPressed()
    {
        SalesTrackingActivity.this.finish();
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
            ArrayAdapter dataAdapter = new ArrayAdapter (SalesTrackingActivity.this, android.R.layout.simple_spinner_item, subcategory_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockSubCategoryListSpinnerID.setAdapter(dataAdapter);

            subcategory_IDSelected = subcategory_nameList.get(0);
            System.out.println("subcategory_IDSelected Initially: "+ subcategory_IDSelected);

        }
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
        client.AddParam("outlet_id", subcategory_IDSelected);
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
            ArrayAdapter dataAdapter = new ArrayAdapter (SalesTrackingActivity.this, android.R.layout.simple_spinner_item, product_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockProductNameListSpinnerID.setAdapter(dataAdapter);

            product_IDSelected = product_nameList.get(0);
            System.out.println("product_IDSelected Initially: "+ product_IDSelected);

        }
    }




}//Main Class
