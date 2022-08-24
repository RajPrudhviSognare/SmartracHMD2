package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReceiveAssetByAssociateActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private Spinner ReceiveAssetByAssociateSpinnerID;

    private ListView ReceiveAssetByAssociateDetailsListViewID;
    private ImageView ReceiveAssetByAssociatePageSubmitImageViewID;
    private ImageView ReceiveAssettopbarbackImageViewID;

    private TextView ReceiveAssetByAssociateNoDataTextViewID;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_RECEIVEASSETSLIST = "recieve_assets";

    private JSONObject JOBJECT_DATA = null;

    //PRODUCT DETAILS TAGS
    private String TAG_PRODUCTID = "id";
    private String TAG_PRODUCTNAME = "name";
    private String TAG_PRODUCTTARGETQUANTITY = "no_of_asset";
    private String TAG_DATE = "sub_time_distribute";
    private List<String> product_idList = null;
    private List<String> product_nameList = null;
    private List<String> producttarget_quantityList = null;
    private List<String> producttarget_dateList = null;
    private List<String> producttarget_quantityDefaultValueList = null;
    private List<String> selectionListProductQuantityTarget1 = null;
    private List<String> selectionListProductQuantityTarget2 = null;
    private List<String> selectionListProductQuantityTarget3 = null;
    private List<String> producttarget_commentsList = null;
    private List<String> producttarget_imagesList = null;

    private boolean checkedItems[];
    private boolean isAllChecked = false;
    private List<String> selectionSet = null;
    private List<String> selectionSet1 = null;
    private List<String> selectionSet2 = null;
    private List<String> selectionSet3 = null;

    private String RECEIVE_STATUS = "accept";
    private boolean Is_All_Valid = true;

    private boolean already_checked = false;
    private int already_checked_counter = 0;
    private String checked_Position_for_Image = "null";

    private Uri capturedImageUri = null;
    private String realpath = null;
    private int CAMERA_REQUEST  = 321;
    private Bitmap bitmap = null;
    private String encodedImageIntoString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_asset_by_associate);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.receive_asset_topbar_title);

        initAllViews();

        if(CommonUtils.isInternelAvailable(ReceiveAssetByAssociateActivity.this)){
            requestProductDetailsList();
        }
        else{
            Toast.makeText(ReceiveAssetByAssociateActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        //Back Button
        ReceiveAssettopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //RECEIVE_STATUS Spinner
        ReceiveAssetByAssociateSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    RECEIVE_STATUS = "accept";
                    System.out.println("RECEIVE_STATUS: "+RECEIVE_STATUS);
                }
                if(position==1){
                    RECEIVE_STATUS = "reject";
                    System.out.println("RECEIVE_STATUS: "+RECEIVE_STATUS);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Submit Button Click Logic
        ReceiveAssetByAssociatePageSubmitImageViewID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectionListProductQuantityTarget1.clear();
                selectionListProductQuantityTarget2.clear();
                selectionListProductQuantityTarget3.clear();

                if(selectionSet.size()==0){
                    Toast.makeText(ReceiveAssetByAssociateActivity.this, "No item is selected!", Toast.LENGTH_SHORT).show();
                }//if(selectionSet.size()==0)

                if(selectionSet.size()!=0){

                    for(int i=0;i<selectionSet1.size();i++){
                        String tmp = selectionSet1.get(i).toString();
                        selectionListProductQuantityTarget1.add("\""+tmp+"\"");
                        System.out.println("selectionListProductQuantityTarget1 Value: "+selectionListProductQuantityTarget1.get(i).toString());
                    }
                    for(int i=0;i<selectionSet2.size();i++){
                        String tmp = selectionSet2.get(i).toString();
                        selectionListProductQuantityTarget2.add("\""+tmp+"\"");
                        System.out.println("selectionListProductQuantityTarget2 Value: "+selectionListProductQuantityTarget2.get(i).toString());
                    }
                    for(int i=0;i<selectionSet3.size();i++){
                        String tmp = selectionSet3.get(i).toString();
                        selectionListProductQuantityTarget3.add("\""+tmp+"\""); //With ""
                        //selectionListProductQuantityTarget3.add(tmp); //Without ""
                        System.out.println("selectionListProductQuantityTarget3 Value: "+selectionListProductQuantityTarget3.get(i).toString());
                    }

                    System.out.println("Total selectionListProductQuantityTarget1 Value: "+selectionListProductQuantityTarget1.toString());
                    System.out.println("Total selectionListProductQuantityTarget2 Value: "+selectionListProductQuantityTarget2.toString());
                    System.out.println("Total selectionListProductQuantityTarget3 Value: "+selectionListProductQuantityTarget3.toString());

                    if(CommonUtils.isInternelAvailable(ReceiveAssetByAssociateActivity.this)){
                        if(Is_All_Valid){
                            if(encodedImageIntoString!=null){
                                setReceivedProductStock();
                            }
                            else{
                                Toast.makeText(ReceiveAssetByAssociateActivity.this, "Item's Image is Required!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(ReceiveAssetByAssociateActivity.this, "Invalid Entry in Receive Value!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ReceiveAssetByAssociateActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                }//if(selectionSet.size()!=0)

            }
        });

    }//onCreate()

    private void initAllViews(){

        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        ReceiveAssetByAssociateSpinnerID = (Spinner)findViewById(R.id.ReceiveAssetByAssociateSpinnerID);

        ReceiveAssetByAssociateDetailsListViewID = (ListView)findViewById(R.id.ReceiveAssetByAssociateDetailsListViewID);
        ReceiveAssetByAssociatePageSubmitImageViewID = (ImageView)findViewById(R.id.ReceiveAssetByAssociatePageSubmitImageViewID);
        ReceiveAssettopbarbackImageViewID = (ImageView)findViewById(R.id.ReceiveAssettopbarbackImageViewID);

        ReceiveAssetByAssociateNoDataTextViewID = (TextView)findViewById(R.id.ReceiveAssetByAssociateNoDataTextViewID);

        product_idList = new ArrayList<String>(0);
        product_nameList = new ArrayList<String>(0);
        producttarget_quantityList = new ArrayList<String>(0);
        producttarget_dateList = new ArrayList<String>(0);
        producttarget_quantityDefaultValueList = new ArrayList<String>(0);
        selectionListProductQuantityTarget1 = new ArrayList<String>(0);
        selectionListProductQuantityTarget2 = new ArrayList<String>(0);
        selectionListProductQuantityTarget3 = new ArrayList<String>(0);
        producttarget_commentsList = new ArrayList<String>(0);
        producttarget_imagesList = new ArrayList<String>(0);

        selectionSet = new ArrayList<String>(0);
        selectionSet1 = new ArrayList<String>(0);
        selectionSet2 = new ArrayList<String>(0);
        selectionSet3 = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(ReceiveAssetByAssociateActivity.this);
    }

    //Set Received Product Stock
    private void setReceivedProductStock(){
        progressDialog.setMessage("Submitting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SET_RECEIVE_ASSETS_FROM_USER_RELATIVE_URI1);

        /*client.AddParam("asset_img", selectionListProductQuantityTarget3.toString());
        System.out.println("asset_img: "+selectionListProductQuantityTarget3.toString());*/
        client.AddParam("asset_img", encodedImageIntoString);
        System.out.println("asset_img: "+encodedImageIntoString);
        client.AddParam("receive_status", RECEIVE_STATUS);
        System.out.println("receive_status: "+RECEIVE_STATUS);
        client.AddParam("ids", selectionSet.toString());
        System.out.println("ids: "+selectionSet.toString());
        client.AddParam("receive_qtn", selectionListProductQuantityTarget1.toString());
        System.out.println("receive_qtn: "+selectionListProductQuantityTarget1.toString());
        client.AddParam("receive_comment", selectionListProductQuantityTarget2.toString());
        System.out.println("receive_comment: "+selectionListProductQuantityTarget2.toString());

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

        already_checked_counter = 0;
        already_checked = true;

        checked_Position_for_Image = "null";
        System.out.println("checked_Position_for_Image: "+ checked_Position_for_Image);

        selectionSet.clear();
        selectionSet1.clear();
        selectionSet2.clear();
        selectionSet3.clear();

        checkedItems = null;
        bitmap = null;
        encodedImageIntoString = null;
        realpath = null;

        //Alert Dialog Builder
        final android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(ReceiveAssetByAssociateActivity.this);
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

    //All Received Product Details
    private void requestProductDetailsList(){

        product_idList.clear();
        product_nameList.clear();
        producttarget_quantityList.clear();
        producttarget_dateList.clear();
        producttarget_quantityDefaultValueList.clear();
        selectionListProductQuantityTarget1.clear();
        selectionListProductQuantityTarget2.clear();
        selectionListProductQuantityTarget3.clear();
        producttarget_commentsList.clear();
        producttarget_imagesList.clear();

        selectionSet.clear();
        selectionSet1.clear();
        selectionSet2.clear();
        selectionSet3.clear();

        checkedItems = null;

        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.RECEIVE_ASSETS_FROM_USER_RELATIVE_URI);
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_RECEIVEASSETSLIST);
                        JSONObject c = null;
                        String id = null;
                        String name = null;
                        String quantity = null;
                        String date = null;

                        System.out.println("##########All PRODUCT List details###################");

                        product_idList.clear();
                        product_nameList.clear();
                        producttarget_quantityList.clear();
                        producttarget_dateList.clear();
                        producttarget_quantityDefaultValueList.clear();
                        selectionListProductQuantityTarget1.clear();
                        selectionListProductQuantityTarget2.clear();
                        selectionListProductQuantityTarget3.clear();
                        producttarget_commentsList.clear();
                        producttarget_imagesList.clear();

                        selectionSet.clear();
                        selectionSet1.clear();
                        selectionSet2.clear();
                        selectionSet3.clear();

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
                                        date = c.getString(TAG_DATE);

                                        product_idList.add(id);
                                        product_nameList.add(name);
                                        producttarget_quantityList.add(quantity);
                                        producttarget_quantityDefaultValueList.add(quantity);
                                        producttarget_dateList.add(date);
                                        producttarget_commentsList.add("");
                                        producttarget_imagesList.add("");
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total product_idList: " + product_idList.size());
                        System.out.println("Total product_nameList: " + product_nameList.size());
                        System.out.println("Total producttarget_quantityList: " + producttarget_quantityList.size());
                        System.out.println("Total producttarget_quantityDefaultValueList: " + producttarget_quantityDefaultValueList.size());
                        System.out.println("Total producttarget_dateList: " + producttarget_dateList.size());
                        System.out.println("Total producttarget_commentsList: " + producttarget_commentsList.size());
                        System.out.println("Total producttarget_imagesList: " + producttarget_imagesList.size());
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
                    showProductTargetDetails();
                }
                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }
            }
            //Login failed
            if(client.responseCode!=200){
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    private void showProductTargetDetails(){
        if(product_idList!=null) {
            if(product_idList.size()!=0){
                ReceiveAssetByAssociateDetailsListViewID.setVisibility(View.VISIBLE);
                ReceiveAssetByAssociateNoDataTextViewID.setVisibility(View.GONE);
                setProductTargetListView();
            }
            else{
                ReceiveAssetByAssociateDetailsListViewID.setVisibility(View.GONE);
                ReceiveAssetByAssociateNoDataTextViewID.setVisibility(View.VISIBLE);
            }
        }
        else{
            ReceiveAssetByAssociateDetailsListViewID.setVisibility(View.GONE);
            ReceiveAssetByAssociateNoDataTextViewID.setVisibility(View.VISIBLE);
        }
    }
    private void setProductTargetListView(){
        if(product_idList.size()!=0){
            checkedItems = new boolean[product_idList.size()];
            for(int i=0;i<product_idList.size();i++){
                checkedItems[i] = false;
            }
            System.out.println("All checkedItems[] values: "+checkedItems);
            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));
        }
    }
    /*
    * CustomAdapterForProductSTOCKDetailsAssociate
    */
    public class CustomAdapterForProductSTOCKDetailsAssociate extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForProductSTOCKDetailsAssociate(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            if(product_idList!=null){
                return product_idList.size();
            }
            else{
                return 0;
            }
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
                view = inflater.inflate(R.layout.customlayout_receive_assets_by_associate, null);
            }
            else{
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = position;

            final TextView dateTextView = (TextView)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_DateTextViewID);
            final TextView nameTextView = (TextView)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_NameTextViewID);
            viewHolder.editText = (EditText)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_ASSIGN_ValueEditTextID);
            viewHolder.editText1 = (EditText)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_Comments_ValueEditTextID);
            viewHolder.checkbox = (CheckBox)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_checkBoxID);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.customlayout_receiveAssets_ByAssociate_ImageViewID);

            final ViewHolder holder = (ViewHolder)view.getTag();

            dateTextView.setText(producttarget_dateList.get(position));
            nameTextView.setText(product_nameList.get(position));
            holder.editText.setText(producttarget_quantityList.get(position));
            holder.editText1.setText(producttarget_commentsList.get(position));

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
                    System.out.println("afterTextChanged(Editable s) - Quantity : " + s.toString());
                    producttarget_quantityList.set(pos1,s.toString()); //Updating with a new value

                    if(s.toString().length()!=0){
                        if(Integer.parseInt(producttarget_quantityDefaultValueList.get(pos1).toString())<Integer.parseInt(s.toString())){
                            //holder.editText.setError("Entered value is higher then the Assigned value");
                            Is_All_Valid = true;
                            producttarget_quantityList.set(pos1,producttarget_quantityDefaultValueList.get(pos1).toString());
                            holder.editText.setText(producttarget_quantityDefaultValueList.get(pos1).toString());
                        }
                        else{
                            Is_All_Valid = true;
                            producttarget_quantityList.set(pos1,s.toString());
                        }
                    }//if
                    else{
                        holder.editText.setError("Receive Value can not be empty");
                        Is_All_Valid = true;
                        //producttarget_quantityList.set(pos1,producttarget_quantityDefaultValueList.get(pos1).toString());
                        //holder.editText.setText(producttarget_quantityDefaultValueList.get(pos1).toString());
                    }//else


                    if(!checked_Position_for_Image.equals("null")) {
                        if (Integer.parseInt(checked_Position_for_Image) == pos1) {
                            selectionSet1.set(0,producttarget_quantityList.get(pos1).toString());
                            System.out.println("selectionSet1 - Quantity  after updating: " + selectionSet1.toString());
                        }
                    }


                }
            });

            holder.editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    //holder.editText.setText(s.toString());
                    System.out.println("afterTextChanged(Editable s) - Comments  : " + s.toString());
                    producttarget_commentsList.set(pos1,s.toString()); //Updating with a new value


                    if(!checked_Position_for_Image.equals("null")) {
                        if (Integer.parseInt(checked_Position_for_Image) == pos1) {
                            selectionSet2.set(0,producttarget_commentsList.get(pos1).toString());
                            System.out.println("selectionSet2 - Comments  after updating: " + selectionSet2.toString());
                        }
                    }

                }
            });

            /*if(isAllChecked){
                holder.checkbox.setChecked(checkedItems[position]);
            }*/

            if(already_checked){
                holder.checkbox.setChecked(checkedItems[position]);
            }

            //Checkbox is checked
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){

                        if(already_checked_counter==0){

                            selectionSet.add(product_idList.get(pos1));
                            selectionSet1.add(producttarget_quantityList.get(pos1));
                            selectionSet2.add(producttarget_commentsList.get(pos1));
                            selectionSet3.add(producttarget_imagesList.get(pos1));
                            System.out.println("Selection ID list: "+ selectionSet.toString());
                            System.out.println("Total Set size(After Add): "+selectionSet.size());
                            System.out.println("Selection Quantity list: "+ selectionSet1.toString());
                            System.out.println("Total Set size(After Add): "+selectionSet1.size());
                            System.out.println("Selection Comments list: "+ selectionSet2.toString());
                            System.out.println("Total Set size(After Add): "+selectionSet2.size());
                            System.out.println("Selection Images list: "+ selectionSet3.toString());
                            System.out.println("Total Set size(After Add): "+selectionSet3.size());

                            checkedItems[pos1] = true;
                            already_checked_counter++;
                            already_checked = true;

                            checked_Position_for_Image = String.valueOf(pos1);
                            System.out.println("checked_Position_for_Image: "+ checked_Position_for_Image);

                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));

                        }//if
                        else{
                            Toast.makeText(ReceiveAssetByAssociateActivity.this,"One Item is already selected!",Toast.LENGTH_LONG).show();
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));
                        }

                    }
                    if(!isChecked){

                        if(already_checked_counter==1){

                            selectionSet.remove(product_idList.get(pos1));
                            selectionSet1.remove(producttarget_quantityList.get(pos1));
                            selectionSet2.remove(producttarget_commentsList.get(pos1));
                            selectionSet3.remove(producttarget_imagesList.get(pos1));
                            System.out.println("Selection ID list: "+ selectionSet.toString());
                            System.out.println("Total Set size(After Remove): "+selectionSet.size());
                            System.out.println("Selection Quantity list: "+ selectionSet1.toString());
                            System.out.println("Total Set size(After Remove): "+selectionSet1.size());
                            System.out.println("Selection Comments list: "+ selectionSet2.toString());
                            System.out.println("Total Set size(After Remove): "+selectionSet2.size());
                            System.out.println("Selection Images list: "+ selectionSet3.toString());
                            System.out.println("Total Set size(After Remove): "+selectionSet3.size());

                            checkedItems[pos1] = false;
                            already_checked_counter--;
                            already_checked = true;

                            checked_Position_for_Image = "null";
                            System.out.println("checked_Position_for_Image: "+ checked_Position_for_Image);

                            selectionSet.clear();
                            selectionSet1.clear();
                            selectionSet2.clear();
                            selectionSet3.clear();
                            bitmap = null;
                            encodedImageIntoString = null;
                            realpath = null;

                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));

                        }//if
                        else{
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));
                        }
                    }
                }

            });

            holder.imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(!checked_Position_for_Image.equals("null")){
                        if(Integer.parseInt(checked_Position_for_Image)==pos1){
                            System.out.println("Valid Position for Image: "+ pos1);
                            if(already_checked_counter!=0){
                                //Open Camera & Take Picture
                                openCameraToTakePicture();
                            }
                            else{
                                System.out.println("Please 'Select/Check' the item first befor taking any picture of the item: "+ pos1);
                                Toast.makeText(ReceiveAssetByAssociateActivity.this, "Please 'Select/Check' the item first before taking any picture of it", Toast.LENGTH_LONG).show();
                            }

                        }
                        else{
                            System.out.println("InValid Position for Image 1: "+ pos1);
                        }
                    }
                    else{
                        System.out.println("InValid Position for Image 2: "+ pos1);
                        System.out.println("Please 'Select/Check' the item first befor taking any picture of the item: "+ pos1);
                        Toast.makeText(ReceiveAssetByAssociateActivity.this, "Please 'Select/Check' the item first before taking any picture of it", Toast.LENGTH_LONG).show();
                    }

                }
            });

            if(!checked_Position_for_Image.equals("null")) {
                if (bitmap != null && Integer.parseInt(checked_Position_for_Image) == pos1) {
                    holder.imageView.setImageBitmap(bitmap);
                    System.out.println("Bitmap Image: " + bitmap.toString());
                    System.out.println("Valid Position for Image: " + pos1);
                }
            }


            return view;

        }

        class ViewHolder{
            EditText editText;
            EditText editText1;
            CheckBox checkbox;
            ImageView imageView;
        }

    }//CustomAdapterForProductSTOCKDetailsAssociate Class

    private void openCameraToTakePicture(){

        /*Calendar cal = Calendar.getInstance();
        File file = null;
        try{
            file = new File(Environment.getExternalStorageDirectory()+"/Self_Pictures", (cal.getTimeInMillis() + ".jpg"));
        }catch (Exception e) {
            System.out.println("e.printStackTrace():file: "+e.toString());
        }

        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("e.printStackTrace(): "+e.toString());
            }
        }else{
            file.delete();
            try{
                file.createNewFile();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("e.printStackTrace(): "+e.toString());
            }
        }
        capturedImageUri = Uri.fromFile(file);
        System.out.println("capturedImageUri: "+capturedImageUri);
        realpath = file.getAbsolutePath().toString();
        System.out.println("realpath: "+realpath);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        //i.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(i, CAMERA_REQUEST);*/

        callCamera2();
    }

    private void callCamera2(){
        Intent intent = new Intent(ReceiveAssetByAssociateActivity.this, ImageCapture1.class);
        ReceiveAssetByAssociateActivity.this.startActivity(intent);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == ReceiveAssetByAssociateActivity.this.RESULT_OK){

            if (requestCode == CAMERA_REQUEST) {
                try {
                     System.out.println("capturedImageUri: "+capturedImageUri);
                     if(capturedImageUri!=null){
                        Log.v("capturedImageUri:",capturedImageUri.toString());
                        System.out.println("data!=null: "+data);
                        //Log.v("data!=null: ",data.toString());
                        bitmap = MediaStore.Images.Media.getBitmap(ReceiveAssetByAssociateActivity.this.getContentResolver(), capturedImageUri);
                        //BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inSampleSize = 3;

                        //For MarshMallow(6.0) API 23//
                        if(bitmap==null){
                            //Log.v("realpath: ",realpath.toString());
                            File f = new File(realpath);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            try {
                                 bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        //For MarshMallow(6.0) API 23//

                        if(bitmap!=null){
                            System.out.println("Bitmap Image: "+bitmap.toString());
                            if(realpath!=null){
                                encodedImageIntoString = CommonUtils.imageToString(realpath);
                                selectionSet3.set(0,encodedImageIntoString);
                                System.out.println("\n\n\n\n\n\n\n\n\n\nselectionSet3: After adding image: "+selectionSet3.toString());
                            }
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                            ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));
                        }

                    }//if(capturedImageUri!=null)

                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }
        if(resultCode == ReceiveAssetByAssociateActivity.this.RESULT_CANCELED){

            if (requestCode == CAMERA_REQUEST) {
                capturedImageUri = null;
                realpath = null;
                System.out.println("capturedImageUri: "+capturedImageUri);
                System.out.println("realpath: "+realpath);
            }

        }

    }*/


    //Show failure Dialog
    private void showFailureDialog() {
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ReceiveAssetByAssociateActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }
    //Show failure Dialog
    private void showFailureDialog1(){
        selectionListProductQuantityTarget1.clear();
        selectionListProductQuantityTarget2.clear();
        selectionListProductQuantityTarget3.clear();
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(ReceiveAssetByAssociateActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: " + MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        ReceiveAssetByAssociateActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(ImageCapture1.camera2_status1){
            ImageCapture1.camera2_status1 = false;

            realpath = ImageCapture1.CAMERA2_IMAGEPATH1;
            System.out.println("realpath: "+realpath);

            File f = new File(realpath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(bitmap!=null){
                System.out.println("Bitmap Image: "+bitmap.toString());
                if(realpath!=null){
                    encodedImageIntoString = CommonUtils.imageToString(realpath);
                    if(selectionSet3!=null){
                        if(selectionSet3.size()!=0){
                            selectionSet3.set(0,encodedImageIntoString);
                            System.out.println("\n\n\n\n\n\n\n\n\n\nselectionSet3: After adding image: "+selectionSet3.toString());
                        }
                    }
                }
                ReceiveAssetByAssociateDetailsListViewID.setAdapter(null);
                ReceiveAssetByAssociateDetailsListViewID.setAdapter(new CustomAdapterForProductSTOCKDetailsAssociate(ReceiveAssetByAssociateActivity.this));
            }

            //attendancePagePhotoPreviewAreaImageViewID.setImageURI(Uri.parse(realpath));

            ImageCapture1.CAMERA2_IMAGEPATH1 = null;

        }//if
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
