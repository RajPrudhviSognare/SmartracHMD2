package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.experis.smartrac_HMD2.adapter.MyCategoriesExpandableListAdapter;
import com.experis.smartrac_HMD2.model.ConstantManager;
import com.experis.smartrac_HMD2.model.DataItem;
import com.experis.smartrac_HMD2.model.SubCategoryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagesForTLActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private CheckBox messagesTL_checkBox_listheaderID;
    private ListView messagesTLAssociatesListViewID;
    private TextView messagesTLNoDataTextViewID;
    private EditText messagesTLMsgBoxEdittextID;
    private ImageView messagesTLSubmitImageViewID;
    private LinearLayout messagesTLBottombarLinearLayoutID;

    private ImageView messagesTLtopbarbackImageViewID;
    private ImageView messagesTLtopbarusericonImageViewID;

    /**
     * To save checked items, and re-add while scrolling.
     */
    private SparseBooleanArray mChecked = new SparseBooleanArray();
    private boolean checkedItems[];
    private boolean isAllChecked = false;
    private Set<String> selectionSet = null;
    private List<String> selectionList = null;
    private JSONArray jsonArray = new JSONArray();

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private String TAG_JARRAY_ASSOCIATELIST = "associate";

    private JSONObject JOBJECT_DATA = null;

    //ASSOCIATE DETAILS TAGS
    private String TAG_ASSOCIATEID = "id";
    private String TAG_ASSOCIATEISDCODE = "isd_code";
    private String TAG_ASSOCIATEFIRSTNAME = "first_name";
    private String TAG_ASSOCIATELASTNAME = "last_name";
    private List<String> associate_idList = null;
    private List<String> associate_isdList = null;
    private List<String> associate_firstnameList = null;
    private List<String> associate_lastnameList = null;

    private String msg = "";

    private Button btn;
    private ExpandableListView lvCategory;
    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_for_tl);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.messagetl_topbar_layout);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();



        if(CommonUtils.isInternelAvailable(MessagesForTLActivity.this)){
            requestAssociateList();
        }
        else{
            Toast.makeText(MessagesForTLActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }

      //  setupReferences();
        messagesTL_checkBox_listheaderID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    for(int i=0;i<associate_idList.size();i++){
                        checkedItems[i] = true;
                    }
                    selectionSet.clear();
                    selectionList.clear();
                    isAllChecked = true;
                    messagesTLAssociatesListViewID.setAdapter(null);
                    messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(MessagesForTLActivity.this));

                    for(int j=0;j<associate_idList.size();j++){
                        selectionSet.add(associate_idList.get(j));
                        selectionList.add(associate_idList.get(j));
                    }
                    System.out.println("Selection ID set: "+ selectionSet.toString());
                    System.out.println("Selection ID set size: "+ selectionSet.size());
                    System.out.println("Selection ID list: "+ selectionList.toString());
                    System.out.println("Selection ID list size: "+ selectionList.size());
                }
                if(!isChecked){
                    selectionSet.clear();
                    selectionList.clear();
                    isAllChecked = false;
                    System.out.println("Selection ID set: "+ selectionSet.toString());
                    System.out.println("Selection ID set size: "+ selectionSet.size());
                    System.out.println("Selection ID list: "+ selectionList.toString());
                    System.out.println("Selection ID list size: "+ selectionList.size());
                    messagesTLAssociatesListViewID.setAdapter(null);
                    messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(MessagesForTLActivity.this));
                }

            }
        });

        messagesTLSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {

               //
                selectionList=new ArrayList<>();
                for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++ ){

                    String isChecked = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

                   /* if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
                    {
                        tvParent.setText(tvParent.getText() + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
                    }*/

                    for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++ ){

                        String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                        if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE))
                        {
                            selectionList.add(MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.CATEGORY_ID));
                           // tvChild.setText(tvChild.getText() +" , " + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " "+(j+1));
                        }

                    }

                }




                if(selectionList.size()==0){
                    //Toast.makeText(MessagesForTLActivity.this, "No Associate is selected!", Toast.LENGTH_LONG).show();
                    showNoAssociateDialog("Kindly select an associate!");
                }//selectionList.size()==0
                if(selectionList.size()!=0){
                    try {
                        if(CommonUtils.isInternelAvailable(MessagesForTLActivity.this)){

                            validateData();
                        }
                        else{
                            Toast.makeText(MessagesForTLActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }//if(selectionList.size()!=0)



            }
        });

        //Back Button
        messagesTLtopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        messagesTLtopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        messagesTLtopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MessagesForTLActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });


    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        messagesTL_checkBox_listheaderID = (CheckBox)findViewById(R.id.messagesTL_checkBox_listheaderID);
        messagesTLAssociatesListViewID = (ListView)findViewById(R.id.messagesTLAssociatesListViewID);
        messagesTLNoDataTextViewID = (TextView)findViewById(R.id.messagesTLNoDataTextViewID);
        messagesTLMsgBoxEdittextID = (EditText)findViewById(R.id.messagesTLMsgBoxEdittextID);
        messagesTLSubmitImageViewID = (ImageView)findViewById(R.id.messagesTLSubmitImageViewID);
        messagesTLBottombarLinearLayoutID = (LinearLayout)findViewById(R.id.messagesTLBottombarLinearLayoutID);

        messagesTLtopbarbackImageViewID = (ImageView)findViewById(R.id.messagesTLtopbarbackImageViewID);
        messagesTLtopbarusericonImageViewID = (ImageView)findViewById(R.id.messagesTLtopbarusericonImageViewID);
        btn = (Button)findViewById(R.id.btn);
        lvCategory = (ExpandableListView) findViewById(R.id.lvCategory);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagesForTLActivity.this,AddGroupActivity.class);
                startActivity(intent);
            }
        });



        associate_idList = new ArrayList<String>(0);
        associate_isdList = new ArrayList<String>(0);
        associate_firstnameList = new ArrayList<String>(0);
        associate_lastnameList = new ArrayList<String>(0);

        selectionSet = new HashSet<String>(0);
        selectionList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(MessagesForTLActivity.this);

    }

    private void setupReferences() {
       // lvCategory = findViewById(R.id.lvCategory);
/*
        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Adventure");

        arSubCategory = new ArrayList<>();
        for(int i = 1; i < 6; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Adventure: "+i);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Art");
        arSubCategory = new ArrayList<>();
        for(int j = 1; j < 6; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Art: "+j);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Cooking");
        arSubCategory = new ArrayList<>();
        for(int k = 1; k < 6; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Cooking: "+k);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);*/

        Log.d("TAG", "setupReferences: "+arCategory.size());

        for(DataItem data : arCategory){
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList =new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID,data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME,data.getCategoryName());

            int countIsChecked = 0;
            for(SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                 mapChild.put(ConstantManager.Parameter.SUB_ID,subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME,subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID,subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED,subCategoryItem.getIsChecked());

                if(subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if(countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            }else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED,data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(MessagesForTLActivity.this,parentItems,childItems,false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(messagesTLMsgBoxEdittextID.getText().toString()))
        {
            messagesTLMsgBoxEdittextID.setError("Message Box is Empty!");
            focusView = messagesTLMsgBoxEdittextID;
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

        msg = messagesTLMsgBoxEdittextID.getText().toString();

        if(!msg.equalsIgnoreCase("")){
            sendMessageToServer();
        }
        else{
            //Toast.makeText(this, "Message Box is Empty!", Toast.LENGTH_LONG).show();
            showNoAssociateDialog("Kindly write a message!");
        }

    }

    //sendMessageToServer
    private void sendMessageToServer(){

        //progressDialog.setTitle("Login");
        progressDialog.setMessage("Sending... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SENDMESSAGE_BY_TL_RELATIVE_URI);
        client.AddParam("tl_id",prefs.getString("USERID",""));
        System.out.println("tl_id:"+prefs.getString("USERID",""));
        client.AddParam("associate_ids",selectionList.toString());
        System.out.println("associate_ids:"+selectionList.toString());
        client.AddParam("message",msg);
        System.out.println("message:"+msg);

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

            progressDialog.dismiss();

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
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };
    //Show Success Dialog
    private void showSuccessDialog(){
        //Alert Dialog Builder
        android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(MessagesForTLActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        aldb.setNegativeButton("Send Another", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MessagesForTLActivity.this,MessagesForTLActivity.class);
                MessagesForTLActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        aldb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MessagesForTLActivity.this,MessagesForTLActivity.class);
                MessagesForTLActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();
    }


    //All Associate List
    private void requestAssociateList(){
        associate_idList.clear();
        associate_isdList.clear();
        associate_firstnameList.clear();
        associate_lastnameList.clear();

        //progressDialog.setTitle("Associate Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SENDMESSAGE_EXISTING_GROUPLIST_URI);
        client.AddParam("tl_id",prefs.getString("USERID",""));
        System.out.println("tl_id:"+prefs.getString("USERID",""));
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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JARRAY_ASSOCIATELIST);
                        JSONArray pendinggroupArray = JOBJECT_DATA.getJSONArray("group_lists");
                        JSONObject c = null;
                        String id = null;
                        String isd = null;
                        String fname = null;
                        String lname = null;

                        System.out.println("##########All ASSOCIATE List details###################");

                        associate_idList.clear();
                        associate_isdList.clear();
                        associate_firstnameList.clear();
                        associate_lastnameList.clear();

                        arCategory = new ArrayList<>();
                        arSubCategory = new ArrayList<>();
                        parentItems = new ArrayList<>();
                        childItems = new ArrayList<>();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){
                            DataItem dataItem = new DataItem();
                            dataItem.setCategoryId("0");
                            dataItem.setCategoryName("All associates");

                            arSubCategory = new ArrayList<>();

                            for(int k = 0; k < pendingDateArray.length(); k++) {

                                try {
                                    c = pendingDateArray.getJSONObject(k);
                                    System.out.println("C is : " + c);
                                    if (c != null) {




                                        id = c.getString(TAG_ASSOCIATEID);
                                        isd = c.getString(TAG_ASSOCIATEISDCODE);
                                        fname = c.getString(TAG_ASSOCIATEFIRSTNAME);
                                        lname = c.getString(TAG_ASSOCIATELASTNAME);

                                        SubCategoryItem subCategoryItem = new SubCategoryItem();
                                        subCategoryItem.setCategoryId(id);
                                        subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                                        subCategoryItem.setSubCategoryName(fname+" "+lname);
                                        arSubCategory.add(subCategoryItem);

                                      /*  associate_idList.add(id);
                                        associate_isdList.add(isd);
                                        associate_firstnameList.add(fname);
                                        associate_lastnameList.add(lname);*/
                                    }
                                } catch (Exception e) {
                                }

                            }//for
                            dataItem.setSubCategory(arSubCategory);
                            arCategory.add(dataItem);

                        }//if(pendingDateArray.length()!=0)

                      /*  System.out.println("Total associate_idList: " + associate_idList.size());
                        System.out.println("Total associate_isdList: " + associate_isdList.size());
                        System.out.println("Total associate_firstnameList: " + associate_firstnameList.size());
                        System.out.println("Total associate_lastnameList: " + associate_lastnameList.size());
                        System.out.println("##########End Of All ASSOCIATE List details###################");*/

                        if(pendinggroupArray.length()==0){
                        }
                        if(pendinggroupArray.length()!=0){


                            for(int j=0;j<pendinggroupArray.length();j++) {
                                DataItem dataItem = new DataItem();
                                dataItem.setCategoryId(pendinggroupArray.optJSONObject(j).optString("group_id"));
                                dataItem.setCategoryName(pendinggroupArray.optJSONObject(j).optString("group_name"));
                                arSubCategory = new ArrayList<>();
                                JSONArray ar=pendinggroupArray.optJSONObject(j).optJSONArray("associate_lists");
                                for (int i = 0; i < ar.length(); i++) {
                                    //arSubCategory = new ArrayList<>();
                                    try {
                                        c = ar.getJSONObject(i);
                                        System.out.println("C is : " + c);
                                        if (c != null) {


                                            id = c.getString("associate_id");
                                            isd = c.getString(TAG_ASSOCIATEISDCODE);
                                            fname = c.getString(TAG_ASSOCIATEFIRSTNAME);
                                            lname = c.getString(TAG_ASSOCIATELASTNAME);



                                            SubCategoryItem subCategoryItem = new SubCategoryItem();
                                            subCategoryItem.setCategoryId(id);
                                            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                                            subCategoryItem.setSubCategoryName(fname+" "+lname);
                                            arSubCategory.add(subCategoryItem);

                                           /* associate_idList.add(id);
                                            associate_isdList.add(isd);
                                            associate_firstnameList.add(fname);
                                            associate_lastnameList.add(lname);*/
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }//for
                                dataItem.setSubCategory(arSubCategory);
                                arCategory.add(dataItem);

                            }

                        }//if(pendingDateArray.length()!=0)




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

            progressDialog.dismiss();

            //Success
            if(client.responseCode==200){

                //Toast.makeText(SignupActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showAssociateDetails();
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

    private void showAssociateDetails(){
        if(arCategory.size()!=0){
            //messagesTLAssociatesListViewID.setVisibility(View.VISIBLE);
           // messagesTLNoDataTextViewID.setVisibility(View.GONE);
            messagesTLBottombarLinearLayoutID.setVisibility(View.VISIBLE);
           // setAssociateListView();
            setupReferences();
        }
        if(arCategory.size()==0){
           // messagesTLAssociatesListViewID.setVisibility(View.GONE);
          //  messagesTLNoDataTextViewID.setVisibility(View.VISIBLE);
            messagesTLBottombarLinearLayoutID.setVisibility(View.GONE);
        }
    }

    private void setAssociateListView(){
        checkedItems = new boolean[associate_idList.size()];
        messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(MessagesForTLActivity.this));
    }

    /*
    * CustomAdapterForAssociatesList
    */
    public class CustomAdapterForAssociatesList extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForAssociatesList(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return associate_idList.size();
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
                view = inflater.inflate(R.layout.custom_layout_for_messages_tl, null);
            }
            else{
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = position;

            final TextView nameTextView = (TextView)view.findViewById(R.id.custom_messagesTL_AssociateNameTextViewID);
            final TextView idTextView = (TextView)view.findViewById(R.id.custom_messagesTL_AssociateIDTextViewID);
            viewHolder.checkbox = (CheckBox)view.findViewById(R.id.custom_messagesTL_checkBoxID);

            final ViewHolder holder = (ViewHolder)view.getTag();

            nameTextView.setText(associate_firstnameList.get(position)+" "+associate_lastnameList.get(position));
            idTextView.setText(associate_isdList.get(position));

            if(isAllChecked){
                holder.checkbox.setChecked(checkedItems[position]);
            }

            //Checkbox is checked
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub

                    if(isChecked){

                        //Toast.makeText(getActivity(), "Checked at "+String.valueOf(pos1), Toast.LENGTH_SHORT).show();
                        selectionSet.add(associate_idList.get(pos1));
                        selectionList.add(associate_idList.get(pos1));
                        System.out.println("Selection ID list: "+ selectionSet.toString());
                        System.out.println("Total Set size(After Add): "+selectionSet.size());
                        System.out.println("Selection ID list: "+ selectionList.toString());
                        System.out.println("Total List size(After Add): "+selectionList.size());
                    }
                    if(!isChecked){

                        //Toast.makeText(getActivity(), "UnChecked at "+String.valueOf(pos1), Toast.LENGTH_SHORT).show();
                        selectionSet.remove(associate_idList.get(pos1));
                        selectionList.remove(associate_idList.get(pos1));
                        System.out.println("Selection ID list: "+ selectionSet.toString());
                        System.out.println("Total Set size(After Remove): "+selectionSet.size());
                        System.out.println("Selection ID list: "+ selectionList.toString());
                        System.out.println("Total List size(After Remove): "+selectionList.size());

                    }//if

                }

            });

            return view;

        }

        class ViewHolder{
            CheckBox checkbox;
        }


    }//CustomAdapterForAssociatesList Class

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        AlertDialog.Builder aldb = new AlertDialog.Builder(MessagesForTLActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    //Show No Associate Dialog
    private void showNoAssociateDialog(String msg){
        //Alert Dialog Builder
        AlertDialog.Builder aldb = new AlertDialog.Builder(MessagesForTLActivity.this);
        aldb.setMessage(msg);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        MessagesForTLActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public void onResume(){
        super.onResume();
    }





}//Main Class
