package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssociateReportsTargetAchievementFragment extends Fragment {

    private ListView associatereportsTargetDetailsListViewID;
    private TextView associatereportsTargetNoDataTextViewID;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private JSONObject JOBJECT_DATA = null;

    private String TAG_JSONARRY_PRODUCTS = "products";

    private String TAG_PRODUCT_NAME = "product_name";
    private String TAG_TARGET_YTD = "target_ytd";
    private String TAG_ACHIEVE_YTD = "achieve_ytd";
    private String TAG_TARGET_MTD = "target_mtd";
    private String TAG_ACHIEVE_MTD = "achieve_mtd";
    private String TAG_PERCENTAGE = "percentage";

    private List<String> product_nameList = null;
    private List<String> target_ytdList = null;
    private List<String> achieve_ytdList = null;
    private List<String> target_mtdList = null;
    private List<String> achieve_mtdList = null;
    private List<String> percentageList = null;


    public AssociateReportsTargetAchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAllViews();

        /*if(CommonUtils.isInternelAvailable(getActivity())){
            requestAttendanceList();
        }
        else{
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity(),"In Time Fragment",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.associatereportstargetachievementfragment, container, false);

        associatereportsTargetDetailsListViewID = (ListView)view.findViewById(R.id.associatereportsTargetDetailsListViewID);
        associatereportsTargetNoDataTextViewID = (TextView)view.findViewById(R.id.associatereportsTargetNoDataTextViewID);

        return view;
    }

    private void initAllViews(){

        //shared preference
        prefs = getActivity().getSharedPreferences(CommonUtils.PREFERENCE_NAME,getActivity().MODE_PRIVATE);
        prefsEditor = prefs.edit();

        product_nameList = new ArrayList<String>(0);
        target_ytdList = new ArrayList<String>(0);
        achieve_ytdList = new ArrayList<String>(0);
        target_mtdList = new ArrayList<String>(0);
        achieve_mtdList = new ArrayList<String>(0);
        percentageList = new ArrayList<String>(0);

        //Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(CommonUtils.isInternelAvailable(getActivity())){
            requestAssociateReportsTargetAchievementList();
        }
        else{
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }

    }//onActivityCreated(Bundle savedInstanceState)

    //requestAssociateReportsTargetAchievementList()
    private void requestAssociateReportsTargetAchievementList() {

        //progressDialog.setTitle("Target Vs Achievement Details");
        progressDialog.setMessage("Loading... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        client = new RestFullClient(Constants.BASE_URL+Constants.ASSOCIATE_REPORTS_TARGETACHIEVEMENT_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));

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

    }//requestAssociateReportsTargetAchievementList()


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

                        JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray(TAG_JSONARRY_PRODUCTS);
                        System.out.println("Products List: " + pendingDateArray.toString());
                        System.out.println("Total Products List: " + pendingDateArray.length());

                        JSONObject c = null;

                        String product_name = null;
                        String target_ytd = null;
                        String achieve_ytd = null;
                        String target_mtd = null;
                        String achieve_mtd = null;
                        String percentage = null;

                        System.out.println("##########All Products List details###################");

                        product_nameList.clear();
                        target_ytdList.clear();
                        achieve_ytdList.clear();
                        target_mtdList.clear();
                        achieve_mtdList.clear();
                        percentageList.clear();

                        if(pendingDateArray.length()==0){
                        }
                        if(pendingDateArray.length()!=0){

                            for(int i = 0; i < pendingDateArray.length(); i++) {

                                try {
                                    c = pendingDateArray.getJSONObject(i);
                                    System.out.println("C is : " + c);
                                    if (c != null) {
                                        product_name = c.getString(TAG_PRODUCT_NAME);
                                        target_ytd = c.getString(TAG_TARGET_YTD);
                                        achieve_ytd = c.getString(TAG_ACHIEVE_YTD);
                                        target_mtd = c.getString(TAG_TARGET_MTD);
                                        achieve_mtd = c.getString(TAG_ACHIEVE_MTD);
                                        percentage = c.getString(TAG_PERCENTAGE);

                                        if(target_ytd.equalsIgnoreCase("null")){
                                            target_ytd = "0";
                                        }
                                        if(achieve_ytd.equalsIgnoreCase("null")){
                                            achieve_ytd = "0";
                                        }
                                        if(target_mtd.equalsIgnoreCase("null")){
                                            target_mtd = "0";
                                        }
                                        if(achieve_mtd.equalsIgnoreCase("null")){
                                            achieve_mtd = "0";
                                        }

                                        product_nameList.add(product_name);
                                        target_ytdList.add(target_ytd);
                                        achieve_ytdList.add(achieve_ytd);
                                        target_mtdList.add(target_mtd);
                                        achieve_mtdList.add(achieve_mtd);
                                        percentageList.add(percentage+"%");
                                    }
                                } catch (Exception e) {
                                  }

                            }//for

                        }//if(pendingDateArray.length()!=0)

                        System.out.println("Total product_nameList: " + product_nameList.size());
                        System.out.println("Total target_ytdList: " + target_ytdList.size());
                        System.out.println("Total achieve_ytdList: " + achieve_ytdList.size());
                        System.out.println("Total target_mtdList: " + target_mtdList.size());
                        System.out.println("Total achieve_mtdList: " + achieve_mtdList.size());
                        System.out.println("Total percentageList: " + percentageList.size());
                        System.out.println("##########End Of All Products List details###################");



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
        catch(Exception e){e.printStackTrace();}

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

                //Success
                if(STATUS.equalsIgnoreCase("true")){
                    //showSuccessDialog();
                    //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                    showProductDetails();
                }

                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }

            //Failed
            if(client.responseCode!=200){
                //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();

    }

    private void showProductDetails(){
        if(product_nameList.size()!=0){
            associatereportsTargetDetailsListViewID.setVisibility(View.VISIBLE);
            associatereportsTargetNoDataTextViewID.setVisibility(View.GONE);
            associatereportsTargetDetailsListViewID.setAdapter(new CustomAdapterForProductDetails(getActivity()));
        }
        else{
            associatereportsTargetDetailsListViewID.setVisibility(View.GONE);
            associatereportsTargetNoDataTextViewID.setVisibility(View.VISIBLE);
        }
    }

    /*
    * CustomAdapterForProductDetails
    */
    public class CustomAdapterForProductDetails extends BaseAdapter {

        public Context cntx;

        public CustomAdapterForProductDetails(Context context){
            cntx = context;
        }

        @Override
        public int getCount() {
            return product_nameList.size();
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

            if(convertView==null){

                LayoutInflater inflater = (LayoutInflater)cntx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_layout_for_associatereports_targetachievement, null);
            }
            else{

                view = convertView;
            }

            final TextView productNameTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_TargetProductNameTextViewID);
            final TextView percentageTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_TargetPercentageTextViewID);
            final TextView targetYTDTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_TargetYTDValueTextViewID);
            final TextView achieveYTDTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_AchieveYTDValueTextViewID);
            final TextView targetMTDTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_TargetMTDValueTextViewID);
            final TextView achieveMTDTextView = (TextView)view.findViewById(R.id.customlayoutassociatereports_AchieveMTDValueTextViewID);

            productNameTextView.setText(product_nameList.get(position));
            percentageTextView.setText(percentageList.get(position));
            targetYTDTextView.setText(target_ytdList.get(position));
            achieveYTDTextView.setText(achieve_ytdList.get(position));
            targetMTDTextView.setText(target_mtdList.get(position));
            achieveMTDTextView.setText(achieve_mtdList.get(position));


            return view;

        }


    }//CustomAdapterForAssociatereportsAttendanceDetails

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy() {
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

}
