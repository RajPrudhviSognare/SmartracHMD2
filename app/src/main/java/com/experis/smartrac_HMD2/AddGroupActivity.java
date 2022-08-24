package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.experis.smartrac_HMD2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddGroupActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private CheckBox messagesTL_checkBox_listheaderID;
    private ListView messagesTLAssociatesListViewID,allgroup;
    private TextView messagesTLNoDataTextViewID;
    private EditText messagesTLMsgBoxEdittextID,groupname;
    private ImageView messagesTLSubmitImageViewID;
    private LinearLayout messagesTLBottombarLinearLayoutID;

    private ImageView messagesTLtopbarbackImageViewID;
    private ImageView messagesTLtopbarusericonImageViewID;
    private LinearLayout group_layout,existing_layout;
    private TextView txt,txts;
    /**
     * To save checked items, and re-add while scrolling.
     */
    private SparseBooleanArray mChecked = new SparseBooleanArray();
    private boolean checkedItems[];
    private boolean isAllChecked = false;
    private Set<String> selectionSet = null;
    private List<String> selectionList = null;
    private JSONArray jsonArray = new JSONArray();

    private RestFullClient client,client1,client3;
    private String associate_name="",associate_id="";
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
    private List<String> existing_associate_idList=null;
    private List<String> existing_associate_nameList=null;
    private List<String> associate_isdList = null;
    private List<String> associate_firstnameList = null;
    private List<String> associate_lastnameList = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.topbar_addgroup);

        initAllViews();

        if(CommonUtils.isInternelAvailable(AddGroupActivity.this)){
            requestAssociateListexisting();
        }
        else{
            Toast.makeText(AddGroupActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        messagesTL_checkBox_listheaderID = (CheckBox)findViewById(R.id.messagesTL_checkBox_listheaderID);
        messagesTLAssociatesListViewID = (ListView)findViewById(R.id.messagesTLAssociatesListViewID);
        allgroup=(ListView)findViewById(R.id.allgroup);
        messagesTLNoDataTextViewID = (TextView)findViewById(R.id.messagesTLNoDataTextViewID);
        messagesTLMsgBoxEdittextID = (EditText)findViewById(R.id.messagesTLMsgBoxEdittextID);
        groupname=(EditText)findViewById(R.id.groupname);
        messagesTLSubmitImageViewID = (ImageView)findViewById(R.id.submit);


        messagesTLtopbarbackImageViewID = (ImageView)findViewById(R.id.messagesTLtopbarbackImageViewID);
        messagesTLtopbarusericonImageViewID = (ImageView)findViewById(R.id.messagesTLtopbarusericonImageViewID);
        existing_layout=(LinearLayout)findViewById(R.id.existing_layout);
        group_layout=(LinearLayout)findViewById(R.id.group_layout);
        txt=(TextView)findViewById(R.id.txt);
        txts=(TextView)findViewById(R.id.txts);


        associate_idList = new ArrayList<String>(0);
        existing_associate_idList=new ArrayList<>();
        existing_associate_nameList=new ArrayList<>();
        associate_isdList = new ArrayList<String>(0);
        associate_firstnameList = new ArrayList<String>(0);
        associate_lastnameList = new ArrayList<String>(0);

        selectionSet = new HashSet<String>(0);
        selectionList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(AddGroupActivity.this);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_layout.getVisibility()==View.VISIBLE){
                    group_layout.setVisibility(View.GONE);
                    messagesTLSubmitImageViewID.setVisibility(View.GONE);
                    //existing_layout.setVisibility(View.VISIBLE);
                }
                else{
                    if(CommonUtils.isInternelAvailable(AddGroupActivity.this)){
                        requestAssociateList();
                    }
                    else{
                        Toast.makeText(AddGroupActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }

                    group_layout.setVisibility(View.VISIBLE);
                    messagesTLSubmitImageViewID.setVisibility(View.VISIBLE);

                   // existing_layout.setVisibility(View.GONE);
                }
            }
        });

        txts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(existing_layout.getVisibility()==View.VISIBLE){
                    existing_layout.setVisibility(View.GONE);
                   // group_layout.setVisibility(View.VISIBLE);
                }
                else{
                    if(CommonUtils.isInternelAvailable(AddGroupActivity.this)){
                        requestAssociateListexisting();
                    }
                    else{
                        Toast.makeText(AddGroupActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                    }
                    existing_layout.setVisibility(View.VISIBLE);
                   // group_layout.setVisibility(View.GONE);
                }
            }
        });

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
                    messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(AddGroupActivity.this));

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
                    messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(AddGroupActivity.this));
                }

            }
        });



        messagesTLSubmitImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(selectionList.size()==0){
                    //Toast.makeText(MessagesForTLActivity.this, "No Associate is selected!", Toast.LENGTH_LONG).show();
                    showNoAssociateDialog("Kindly select an associate!");
                }//selectionList.size()==0
                if(selectionList.size()!=0){
                    try {
                        if(CommonUtils.isInternelAvailable(AddGroupActivity.this)){

                            validateData();
                        }
                        else{
                            Toast.makeText(AddGroupActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(AddGroupActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });


    }


    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {

        boolean cancel = false;
        View focusView = null;

       /* if(selectionList.size()==0)
        {
            groupname.setError("Please select at least one associate for creating group");
            focusView = groupname;
            cancel = true;
        }*/
        if(TextUtils.isEmpty(groupname.getText().toString().trim()))
        {
            groupname.setError("Please enter group name");
            focusView = groupname;
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


       int size = selectionList.size();

        if(size!=0){
            sendMessageToServer();
        }
        else{
            //Toast.makeText(this, "Message Box is Empty!", Toast.LENGTH_LONG).show();
            showNoAssociateDialog("Please select at least one associate for creating group");
        }

    }

    //sendMessageToServer
    private void sendMessageToServer(){

        //progressDialog.setTitle("Login");
        progressDialog.setMessage("Sending... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SENDMESSAGE_CREATE_GROUP_URI);
        client.AddParam("tl_id",prefs.getString("USERID",""));
        System.out.println("tl_id:"+prefs.getString("USERID",""));
        client.AddParam("group_name",groupname.getText().toString());
        System.out.println("group_name:"+groupname.getText().toString());

        client.AddParam("associate_ids",selectionList.toString());
        System.out.println("associate_ids:"+selectionList.toString());


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
        android.app.AlertDialog.Builder aldb = new android.app.AlertDialog.Builder(AddGroupActivity.this);
        aldb.setTitle("Success!");
        aldb.setMessage(MESSAGE);
        aldb.setCancelable(false);
        /*aldb.setNegativeButton("Send Another", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(AddGroupActivity.this,AddGroupActivity.class);
                AddGroupActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });*/
        aldb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i=new Intent(AddGroupActivity.this,MessagesForTLActivity.class);
                AddGroupActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.show();
    }


    //All existing Associate List
    private void requestAssociateListexisting(){
        existing_associate_idList.clear();
        existing_associate_nameList.clear();


        //progressDialog.setTitle("Associate Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client1 = new RestFullClient(Constants.BASE_URL+Constants.SENDMESSAGE_EXISTING_GROUPLIST_URI);
        client1.AddParam("tl_id",prefs.getString("USERID",""));
        System.out.println("tl_id:"+prefs.getString("USERID",""));
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    client1.Execute(1); //POST Request

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                receiveDataForServerResponseexisting(client1.jObj);
                handler2.sendEmptyMessage(0);

            }

        }).start();

    }
    private void receiveDataForServerResponseexisting(JSONObject jobj){

        try{

            if(client1.responseCode==200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

                if(STATUS.equalsIgnoreCase("true")) {

                    JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                    System.out.println("JOBJECT_DATA: " + JOBJECT_DATA.toString());

                    if (JOBJECT_DATA != null) {

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray("group_lists");
                        JSONObject c = null;
                        String id = null;
                        String isd = null;
                        String fname = null;
                        String lname = null;

                        System.out.println("##########All ASSOCIATE List details###################");

                        existing_associate_idList.clear();
                        existing_associate_nameList.clear();


                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString("group_id");
                                        isd = c.getString("group_name");


                                        existing_associate_idList.add(id);
                                        existing_associate_nameList.add(isd);

                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)



                    }//if (JOBJECT_DATA != null)
                    else{
                        System.out.println("JOBJECT_DATA is Null");
                    }

                }//if(STATUS.equalsIgnoreCase("true")

            }//if(client.responseCode==200)
            if(client1.responseCode!=200){

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

            progressDialog.dismiss();

            //Success
            if(client1.responseCode==200){

                //Toast.makeText(SignupActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showAssociateDetailsexisting();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client1.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    private void showAssociateDetailsexisting(){
        if(existing_associate_idList.size()!=0){
            allgroup.setVisibility(View.VISIBLE);
          //  messagesTLNoDataTextViewID.setVisibility(View.GONE);
            // messagesTLBottombarLinearLayoutID.setVisibility(View.VISIBLE);
            setAssociateListViewexisting();
        }
        if(existing_associate_idList.size()==0){
            allgroup.setVisibility(View.GONE);
           // messagesTLNoDataTextViewID.setVisibility(View.VISIBLE);
            // messagesTLBottombarLinearLayoutID.setVisibility(View.GONE);
        }
    }

    private void setAssociateListViewexisting(){
        //checkedItems = new boolean[associate_idList.size()];
        allgroup.setAdapter(new CustomAdapterForExistingAssociatesList(AddGroupActivity.this));
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
        client = new RestFullClient(Constants.BASE_URL+Constants.GETASSOCIATE_BY_TEAMLEAD_RELATIVE_URI);
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

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        id = c.getString(TAG_ASSOCIATEID);
                                        isd = c.getString(TAG_ASSOCIATEISDCODE);
                                        fname = c.getString(TAG_ASSOCIATEFIRSTNAME);
                                        lname = c.getString(TAG_ASSOCIATELASTNAME);

                                        associate_idList.add(id);
                                        associate_isdList.add(isd);
                                        associate_firstnameList.add(fname);
                                        associate_lastnameList.add(lname);
                                    }
                                } catch (Exception e) {
                                }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total associate_idList: " + associate_idList.size());
                        System.out.println("Total associate_isdList: " + associate_isdList.size());
                        System.out.println("Total associate_firstnameList: " + associate_firstnameList.size());
                        System.out.println("Total associate_lastnameList: " + associate_lastnameList.size());
                        System.out.println("##########End Of All ASSOCIATE List details###################");


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
        if(associate_idList.size()!=0){
            messagesTLAssociatesListViewID.setVisibility(View.VISIBLE);
            messagesTLNoDataTextViewID.setVisibility(View.GONE);
           // messagesTLBottombarLinearLayoutID.setVisibility(View.VISIBLE);
            setAssociateListView();
        }
        if(associate_idList.size()==0){
            messagesTLAssociatesListViewID.setVisibility(View.GONE);
            messagesTLNoDataTextViewID.setVisibility(View.VISIBLE);
           // messagesTLBottombarLinearLayoutID.setVisibility(View.GONE);
        }
    }

    private void setAssociateListView(){
        checkedItems = new boolean[associate_idList.size()];
        messagesTLAssociatesListViewID.setAdapter(new CustomAdapterForAssociatesList(AddGroupActivity.this));
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
            AddGroupActivity.CustomAdapterForAssociatesList.ViewHolder viewHolder = new CustomAdapterForAssociatesList.ViewHolder();

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

            final AddGroupActivity.CustomAdapterForAssociatesList.ViewHolder holder = (AddGroupActivity.CustomAdapterForAssociatesList.ViewHolder)view.getTag();

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



    /*
     * CustomAdapterForAssociatesList
     */
    public class CustomAdapterForExistingAssociatesList extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForExistingAssociatesList(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return existing_associate_idList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View view;
            AddGroupActivity.CustomAdapterForExistingAssociatesList.ViewHolder viewHolder = new CustomAdapterForExistingAssociatesList.ViewHolder();

            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_messages_tl_existing, null);
            }
            else{
                view = convertView;
            }

            view.setTag(viewHolder);
            final int pos1 = position;

            final TextView nameTextView = (TextView)view.findViewById(R.id.group_name_txt);
            final ImageView edit = (ImageView)view.findViewById(R.id.edit);
            final ImageView delete = (ImageView)view.findViewById(R.id.delete);



            final AddGroupActivity.CustomAdapterForExistingAssociatesList.ViewHolder holder = (AddGroupActivity.CustomAdapterForExistingAssociatesList.ViewHolder)view.getTag();

            nameTextView.setText(existing_associate_nameList.get(position));
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    associate_id=existing_associate_idList.get(position);
                    associate_name=existing_associate_nameList.get(position);
                    Intent i=new Intent(AddGroupActivity.this,UpdateGroupActivity.class);
                    i.putExtra("associate_id",associate_id);
                    i.putExtra("associate_name",associate_name);
                    startActivity(i);

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogfordeleteassociate(existing_associate_nameList.get(position),existing_associate_idList.get(position));

                }
            });



            return view;

        }

        class ViewHolder{
            //CheckBox checkbox;
        }


    }//CustomAdapterForAssociatesList Class

    private void dialogfordeleteassociate(final String s, final String s1) {
        //Alert Dialog Builder
        AlertDialog.Builder aldb = new AlertDialog.Builder(AddGroupActivity.this);
        aldb.setTitle(s);
        aldb.setMessage("Do you want to delete this group");
        aldb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(CommonUtils.isInternelAvailable(AddGroupActivity.this)){
                    existing_deleteassociate(s1,s);
                }
                else{
                    Toast.makeText(AddGroupActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
                }

            }
        });
        aldb.setNegativeButton("CANCEL", null);
        aldb.show();

    }

    private void existing_deleteassociate(final String ass_id, final String ass_name){

        associate_name=ass_name;
        associate_id=ass_id;
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client3 = new RestFullClient(Constants.BASE_URL+Constants.SENDMESSAGE_DELETE_GROUP_URI);
        client3.AddParam("group_id",ass_id);
        System.out.println("group_id:"+ass_id);
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    client3.Execute(1); //POST Request

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                receiveDataForServerResponseexistingdelete(client3.jObj);
                handler3.sendEmptyMessage(0);

            }

        }).start();
    }


    private void receiveDataForServerResponseexistingdelete(JSONObject jobj){

        try{

            if(client3.responseCode==200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

                if(STATUS.equalsIgnoreCase("true")) {

                    existing_associate_nameList.remove(associate_name);
                    existing_associate_idList.remove(associate_id);
                    CustomAdapterForExistingAssociatesList listadapter=new CustomAdapterForExistingAssociatesList(AddGroupActivity.this);
                    listadapter.notifyDataSetChanged();

                }

            }//if(client.responseCode==200)
            if(client3.responseCode!=200){

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

            progressDialog.dismiss();

            //Success
            if(client3.responseCode==200){

                //Toast.makeText(SignupActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();

                //Login success
                if(STATUS.equalsIgnoreCase("true")){
                    //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    showAssociateDetailsexisting();
                }

                //Login failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Login failed
            if(client3.responseCode!=200){
                //Toast.makeText(TargetSettingActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        AlertDialog.Builder aldb = new AlertDialog.Builder(AddGroupActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    //Show No Associate Dialog
    private void showNoAssociateDialog(String msg){
        //Alert Dialog Builder
        AlertDialog.Builder aldb = new AlertDialog.Builder(AddGroupActivity.this);
        aldb.setMessage(msg);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(AddGroupActivity.this, MessagesForTLActivity.class);

        AddGroupActivity.this.finish();
        startActivity(i);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public void onResume(){
        super.onResume();
    }
}
