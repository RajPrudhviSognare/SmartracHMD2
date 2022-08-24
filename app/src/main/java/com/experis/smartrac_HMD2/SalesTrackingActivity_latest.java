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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalesTrackingActivity_latest extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ImageView managedailystocktopbarbackImageViewID;
    private ImageView managedailystocktopbarusericonImageViewID;
    private TextView mytextID;

    private ListView SalesTrackingActivity_latestDetailsListViewID;
    private TextView SalesTrackingActivity_latestNoDataTextViewID;
    private ImageView SalesTrackingActivity_latestPageSubmitImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_ITEMLIST = "list";

    private JSONObject JOBJECT_DATA = null;

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTID = "id";
    private String TAG_PRODUCTNAME = "name";
    private String TAG_PRODUCTTARGETQUANTITY = "qun";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;
    private List<String> producttarget_quantityList = null;

    private List<String> selectionListProductQuantityTarget = null;

    private String type = "";

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
        setContentView(R.layout.activity_sales_tracking_latest);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.sales_tracking_topbar_title);

        type = getIntent().getStringExtra("type");
        Log.v("type: ",type.toString());

        initAllViews();

        if(type.equalsIgnoreCase("display")){
            mytextID.setText("");
            mytextID.setText("Display Info");
        }
        if(type.equalsIgnoreCase("counter")){
            mytextID.setText("");
            mytextID.setText("Counter Info");
        }

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

                if(CommonUtils.isInternelAvailable(SalesTrackingActivity_latest.this)){
                    requestDisplayList();
                }
                else{
                    Toast.makeText(SalesTrackingActivity_latest.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Back Button
        managedailystocktopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //User Icon Click Event
        managedailystocktopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SalesTrackingActivity_latest.this,ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });




        //Submit Button
        SalesTrackingActivity_latestPageSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {

                selectionListProductQuantityTarget.clear();

                /*EditText et;
                for (int i = 0; i < SalesTrackingActivity_latestDetailsListViewID.getCount(); i++) {
                    et = (EditText) SalesTrackingActivity_latestDetailsListViewID.getChildAt(i).findViewById(R.id.customlayout_targetsetting_ProductQuantityTargetEditTextID);
                    if (et!=null) {
                        if(et.getText().toString().length()!=0){
                            String tmp = String.valueOf(et.getText().toString());
                            System.out.println("EditText Value: "+String.valueOf(et.getText().toString()));
                            Log.v("EditText Value: ",String.valueOf(et.getText().toString()));
                            selectionListProductQuantityTarget.add("\""+tmp+"\"");
                            System.out.println("product_idList.get(i) Value: "+product_idList.get(i).toString());
                            Log.v("product_idList: ",product_idList.get(i).toString());
                        }
                        else{
                            selectionListProductQuantityTarget.add("0");
                            System.out.println("EditText Value: "+String.valueOf(et.getText().toString()));
                            Log.v("EditText Value: ",String.valueOf(et.getText().toString()));
                            System.out.println("product_idList.get(i) Value: "+product_idList.get(i).toString());
                            Log.v("product_idList: ",product_idList.get(i).toString());
                        }
                    }
                }
                System.out.println("selectionListProductQuantityTarget: "+selectionListProductQuantityTarget.toString());
                Log.v("selectionListProd...: ",selectionListProductQuantityTarget.toString());
                System.out.println("product_idList Value: "+product_idList.toString());
                Log.v("product_idList Value: ",product_idList.toString());*/

                for (int i = 0; i < producttarget_quantityList.size(); i++) {
                    String tmp = producttarget_quantityList.get(i).toString();
                    System.out.println("tmp: "+tmp);
                    Log.v("tmp: ",tmp);
                    selectionListProductQuantityTarget.add("\""+tmp+"\"");
                    System.out.println("product_idList.get(i) Value: "+product_idList.get(i).toString());
                    Log.v("product_idList: ",product_idList.get(i).toString());
                }
                System.out.println("selectionListProductQuantityTarget: "+selectionListProductQuantityTarget.toString());
                Log.v("selectionListProd...: ",selectionListProductQuantityTarget.toString());
                System.out.println("product_idList Value: "+product_idList.toString());
                Log.v("product_idList Value: ",product_idList.toString());

                if(selectionListProductQuantityTarget.size()!=0&&product_idList.size()!=0){
                    if(CommonUtils.isInternelAvailable(SalesTrackingActivity_latest.this)){
                        setProductTarget();
                    }
                    else{
                        Toast.makeText(SalesTrackingActivity_latest.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        managedailystocktopbarbackImageViewID = (ImageView)findViewById(R.id.managedailystocktopbarbackImageViewID);
        managedailystocktopbarusericonImageViewID = (ImageView)findViewById(R.id.managedailystocktopbarusericonImageViewID);
        mytextID = (TextView)findViewById(R.id.mytextID);

        SalesTrackingActivity_latestDetailsListViewID = (ListView)findViewById(R.id.SalesTrackingActivity_latestDetailsListViewID);
        SalesTrackingActivity_latestNoDataTextViewID = (TextView)findViewById(R.id.SalesTrackingActivity_latestNoDataTextViewID);
        SalesTrackingActivity_latestPageSubmitImageViewID = (ImageView)findViewById(R.id.SalesTrackingActivity_latestPageSubmitImageViewID);
        manageStockSubCategoryListSpinnerID = (Spinner)findViewById(R.id.manageStockSubCategoryListSpinnerID);
        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);
        producttarget_quantityList = new ArrayList<String>(0);
        selectionListProductQuantityTarget = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(SalesTrackingActivity_latest.this);
        subcategory_idList = new ArrayList<String>(0);
        subcategory_nameList = new ArrayList<String>(0);
        if(CommonUtils.isInternelAvailable(SalesTrackingActivity_latest.this)){
            requestOutletList();
        }
        else{
            Toast.makeText(SalesTrackingActivity_latest.this, "No internet connection!", Toast.LENGTH_LONG).show();
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
            ArrayAdapter dataAdapter = new ArrayAdapter (SalesTrackingActivity_latest.this, android.R.layout.simple_spinner_item, subcategory_nameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            manageStockSubCategoryListSpinnerID.setAdapter(dataAdapter);

            subcategory_IDSelected = subcategory_nameList.get(0);
            System.out.println("subcategory_IDSelected Initially: "+ subcategory_IDSelected);

        }
    }

    //Set Product Target
    private void setProductTarget(){
        progressDialog.setMessage("Submitting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SET_DISPLAY_COUNTER_DETAILS_RELATIVE_URI);
        client.AddParam("type", type);
        client.AddParam("associate_id", prefs.getString("USERID",""));
        client.AddParam("outlet_id", subcategory_IDSelected);
        client.AddParam("product_ids", product_idList.toString());
        client.AddParam("qty", selectionListProductQuantityTarget.toString());
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
                    showFailureDialog1();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog1();
            }

        }//handleMessage(Message msg)

    };

    //All Product Details
    private void requestDisplayList(){

            product_idList.clear();
            product_nameList.clear();
            producttarget_quantityList.clear();

            //progressDialog.setTitle("Product Target Details");
            progressDialog.setMessage("Loading... Please wait!");
            progressDialog.setCancelable(false);
            progressDialog.show();
            client = new RestFullClient(Constants.BASE_URL+Constants.GET_DISPLAY_COUNTER_DETAILS_RELATIVE_URI);
            if(type.equalsIgnoreCase("display")){
                client.AddParam("type", "display");
            }
            if(type.equalsIgnoreCase("counter")){
                client.AddParam("type", "counter");
            }

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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_ITEMLIST);
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

                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }
            //Failed
            if(client.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    private void showProductTargetDetails(){
        if(product_idList.size()!=0){
            SalesTrackingActivity_latestDetailsListViewID.setVisibility(View.VISIBLE);
            SalesTrackingActivity_latestNoDataTextViewID.setVisibility(View.GONE);
            setProductTargetListView();
        }
    }
    private void setProductTargetListView(){
        SalesTrackingActivity_latestDetailsListViewID.setAdapter(new CustomAdapterForProductTargetDetails(SalesTrackingActivity_latest.this));
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
                    Log.v("afterTextChanged: ",s.toString());
                    producttarget_quantityList.set(pos1,s.toString()); //Updating with a new value
                }
            });


            return view;

        }

        class ViewHolder{
            EditText editText;
        }

    }//CustomAdapterForAttendanceDetails Class

    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(SalesTrackingActivity_latest.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        aldb.show();
    }

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        /*AlertDialog.Builder aldb = new AlertDialog.Builder(SalesTrackingActivity_latest.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();*/

        SalesTrackingActivity_latestNoDataTextViewID.setVisibility(View.VISIBLE);
    }

    //Show failure Dialog
    private void showFailureDialog1(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(SalesTrackingActivity_latest.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        SalesTrackingActivity_latest.this.finish();
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

}//Main Class
