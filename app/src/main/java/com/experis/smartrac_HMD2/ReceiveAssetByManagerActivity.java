package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceiveAssetByManagerActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ListView ReceiveAssetByManagerDetailsListViewID;
    private ImageView ReceiveAssetByManagerPageSubmitImageViewID;
    private ImageView ReceiveAssettopbarbackImageViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_ASSETSTOCKLIST = "asset_stock";

    private JSONObject JOBJECT_DATA = null;

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTID = "asset_id";
    private String TAG_PRODUCTNAME = "name";
    private String TAG_PRODUCTTARGETQUANTITY = "asset_stock";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;
    private List<String> producttarget_quantityList = null;
    private List<String> producttarget_quantityDefaultValueList = null;
    private List<String> selectionListProductQuantityTarget = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_asset_by_manager);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.receive_asset_topbar_title);

        initAllViews();

        if(CommonUtils.isInternelAvailable(ReceiveAssetByManagerActivity.this)){
            requestProductDetailsList();
        }
        else{
            Toast.makeText(ReceiveAssetByManagerActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        //Back Button
        ReceiveAssettopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Submit Button
        ReceiveAssetByManagerPageSubmitImageViewID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                selectionListProductQuantityTarget.clear();

                for(int i=0;i<producttarget_quantityDefaultValueList.size();i++){
                    String tmp = producttarget_quantityDefaultValueList.get(i).toString();
                    selectionListProductQuantityTarget.add("\""+tmp+"\"");
                    System.out.println("product_id Value: "+product_idList.get(i).toString());
                    System.out.println("selectionListProductQuantityTarget Value: "+selectionListProductQuantityTarget.get(i).toString());
                    System.out.println("producttarget_quantityDefaultValue Value: "+producttarget_quantityDefaultValueList.get(i).toString());
                }

                if(selectionListProductQuantityTarget.size()!=0&&product_idList.size()!=0){
                    if(CommonUtils.isInternelAvailable(ReceiveAssetByManagerActivity.this)){
                        setProductStock();
                    }
                    else{
                        Toast.makeText(ReceiveAssetByManagerActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        ReceiveAssetByManagerDetailsListViewID = (ListView)findViewById(R.id.ReceiveAssetByManagerDetailsListViewID);
        ReceiveAssetByManagerPageSubmitImageViewID = (ImageView)findViewById(R.id.ReceiveAssetByManagerPageSubmitImageViewID);
        ReceiveAssettopbarbackImageViewID = (ImageView)findViewById(R.id.ReceiveAssettopbarbackImageViewID);

        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);
        producttarget_quantityList = new ArrayList<String>(0);
        producttarget_quantityDefaultValueList = new ArrayList<String>(0);
        selectionListProductQuantityTarget = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(ReceiveAssetByManagerActivity.this);
    }

    //Set Product Stock
    private void setProductStock(){
        progressDialog.setMessage("Submitting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SET_ASSET_STOCK_BY_ASSET_MANAGER_RELATIVE_URI);

        client.AddParam("associate_id", prefs.getString("USERID",""));
        System.out.println("associate_id: "+prefs.getString("USERID",""));
        client.AddParam("role_id", prefs.getString("USERROLEID",""));
        System.out.println("role_id: "+prefs.getString("USERROLEID",""));
        client.AddParam("aset_ids", product_idList.toString());
        System.out.println("aset_ids: "+product_idList.toString());
        client.AddParam("asset_no", selectionListProductQuantityTarget.toString());
        System.out.println("asset_no: "+selectionListProductQuantityTarget.toString());
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
                    ReceiveAssetByManagerDetailsListViewID.setAdapter(null); //Added Later
                    showSuccessDialog();
                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog1();
                }
            }
            //Failed
            if(client.responseCode!=200){
                showFailureDialog1();
            }

        }//handleMessage(Message msg)

    };
    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(ReceiveAssetByManagerActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestProductDetailsList();
            }
        });

        aldb.show();
    }

    //All Product/Stock Details
    private void requestProductDetailsList(){

        product_idList.clear();
        product_nameList.clear();
        producttarget_quantityList.clear();
        producttarget_quantityDefaultValueList.clear();
        selectionListProductQuantityTarget.clear();

        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.GET_ASSET_STOCK_BY_ASSET_MANAGER_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));
        System.out.println("associate_id: "+prefs.getString("USERID",""));
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_ASSETSTOCKLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;
                        String quantity = null;

                        System.out.println("##########All PRODUCT List details###################");

                        product_idList.clear();
                        product_nameList.clear();
                        producttarget_quantityList.clear();
                        producttarget_quantityDefaultValueList.clear();
                        selectionListProductQuantityTarget.clear();

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
                                        producttarget_quantityDefaultValueList.add("0");
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total product_idList: " + product_idList.size());
                        System.out.println("Total product_nameList: " + product_nameList.size());
                        System.out.println("Total producttarget_quantityList: " + producttarget_quantityList.size());
                        System.out.println("Total producttarget_quantityDefaultValueList: " + producttarget_quantityDefaultValueList.size());
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
        if(product_idList!=null) {
            if(product_idList.size()!=0){
                ReceiveAssetByManagerDetailsListViewID.setVisibility(View.VISIBLE);
                setProductTargetListView();
            }
            else{
                ReceiveAssetByManagerDetailsListViewID.setVisibility(View.GONE);
            }
        }
        else{
            ReceiveAssetByManagerDetailsListViewID.setVisibility(View.GONE);
        }
    }
    private void setProductTargetListView(){
        ReceiveAssetByManagerDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetails(ReceiveAssetByManagerActivity.this));
    }
    /*
    * CustomAdapterForProductSTOCKDetails
    */
    public class CustomAdapterForProductSTOCKDetails extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForProductSTOCKDetails(Context context){
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
                view = inflater.inflate(R.layout.customlayout_for_receive_asset_bymanager, null);
            }
            else{
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = position;

            final TextView nameTextView = (TextView)view.findViewById(R.id.customlayout_receive_asset_bymanager_NameValueTextViewID1001);
            final TextView stockTextView = (TextView)view.findViewById(R.id.customlayout_receive_asset_bymanager_STOCK_ValueTextViewID1001);
            viewHolder.editText = (EditText)view.findViewById(R.id.customlayout_receive_asset_bymanager_ASSIGN_ValueEditTextID);

            final ViewHolder holder = (ViewHolder)view.getTag();

            nameTextView.setText(product_nameList.get(position));
            stockTextView.setText(producttarget_quantityList.get(position));
            holder.editText.setText(producttarget_quantityDefaultValueList.get(position));

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
                    producttarget_quantityDefaultValueList.set(pos1,s.toString()); //Updating with a new value
                }
            });


            return view;

        }

        class ViewHolder{
            EditText editText;
        }

    }//CustomAdapterForProductSTOCKDetails Class


    //Show failure Dialog
    private void showFailureDialog() {
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ReceiveAssetByManagerActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }
    //Show failure Dialog
    private void showFailureDialog1(){
        selectionListProductQuantityTarget.clear();
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ReceiveAssetByManagerActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        ReceiveAssetByManagerActivity.this.finish();
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
