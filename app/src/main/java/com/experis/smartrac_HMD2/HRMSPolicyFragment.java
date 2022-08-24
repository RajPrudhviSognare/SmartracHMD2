package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HRMSPolicyFragment extends Fragment {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private ListView policylistview;

    private Spinner hrmsPayslipMonthSelectionSpinnerID;
    private Spinner hrmsPayslipYearSelectionSpinnerID;
    private AutoCompleteTextView hrmsPayslipYearSelectionAutoCompleteTextViewID;
    private TextView hrmsPayslipPagePaySlipLinkTextViewID;
    private TextView hrmsPayslipOpenMessageTagTextViewID;
    private Button hrmsPayslipBtnID;

    private LinearLayout hrmsPayslipDownloadLinkLayoutID;
    private TextView hrmsPayslipDownloadLinkTagTextViewID;
    private ImageView appointmentdownload,incrementdownload,incrementstructuredownload;
    private ImageView leavepolicydownload,travelpolicydownload,formatdownload;
    private ImageView docdownload1,docdownload2,docdownload3,docdownload4;



    private String MONTH[] = {"Select Month","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] YEAR = { "2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"};
    String[] YEAR1 = new String[3];

    private String yearSelected = "";
    private String monthSelected = "";
    private boolean validYear = false;
    private boolean validMonth = false;

    private int CURRENT_MONTH;

    private RestFullClient client;

    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private String TAG_JOBJECT_DATA = "data";
    private JSONObject JOBJECT_DATA = null;


    private String baseURL;
    private String SOAPRequestXML;
    private HttpResponse httpResponse = null;

    private String TAG_PayslipResult = "GetLetterResult";

    private String AppointmentLetter = "";
    private String IncrementLetterC = "";
    private String IncrementLetterD = "";
  //  private String docother1 ="";
  //  private String docother2 ="";
 //   private String docother3 ="";
 //   private String docother4 ="";




    private String TAG_LEAVE_POLICY = "leave_policy";
    private String TAG_TRAVEL_POLICY = "travel_policy";
    private String TAG_OTHER_POLICY = "formats";

    private String TAG_TITLE = "title";
    private String TAG_FILE_NAME = "file_name";
    private String TAG_FILE_EXT = "doc_file_ext";
    private String TAG_DOC_TYPE = "doc_type";
    private String TAG_DOC_URL = "file_url";

    private String downloadUri ="";

    private String policyTitle ="";
    private String fileName ="";
    private String docFileExt = "";
    private String docType = "";
    private String docUrl = "";

    private List<String> docUrlList= null;
    private List<String> policyTitleList = null;
    private List<String> fileNameList = null;
    private List<String> docTypeList = null;
    private List<String> docFileExtList = null;





    private Calendar calendar;
    private ArrayList<String> doc;
    private TextView apoointletter,incrementletter,incrementstructure,docother1,docother2,docother3,docother4;
    private String tag="0";

    public HRMSPolicyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initAllViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity(),"In Time Fragment",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.hrms_policy_fragment, container, false);

        apoointletter=(TextView)view.findViewById(R.id.apoointletter);
        incrementletter=(TextView)view.findViewById(R.id.incrementletter);
        incrementstructure=(TextView)view.findViewById(R.id.incrementstructure);
        docother1 = (TextView)view.findViewById(R.id.docother1);
        docother2 = (TextView)view.findViewById(R.id.docother2);
        docother3 = (TextView)view.findViewById(R.id.docother3);
        docother4 = (TextView)view.findViewById(R.id.docother4);


     //   policylistview = (ListView)view.findViewById(R.id.policylistview);

      /*  hrmsPayslipMonthSelectionSpinnerID = (Spinner)view.findViewById(R.id.hrmsPayslipMonthSelectionSpinnerID);
        hrmsPayslipYearSelectionSpinnerID = (Spinner)view.findViewById(R.id.hrmsPayslipYearSelectionSpinnerID);
        hrmsPayslipYearSelectionAutoCompleteTextViewID = (AutoCompleteTextView)view.findViewById(R.id.hrmsPayslipYearSelectionAutoCompleteTextViewID);

        hrmsPayslipBtnID = (Button)view.findViewById(R.id.hrmsPayslipBtnID);*/
       /* hrmsPayslipPagePaySlipLinkTextViewID = (TextView)view.findViewById(R.id.hrmsPayslipPagePaySlipLinkTextViewID);
        hrmsPayslipDownloadLinkLayoutID = (LinearLayout)view.findViewById(R.id.hrmsPayslipDownloadLinkLayoutID);
        hrmsPayslipDownloadLinkTagTextViewID = (TextView)view.findViewById(R.id.hrmsPayslipDownloadLinkTagTextViewID);
        hrmsPayslipOpenMessageTagTextViewID = (TextView)view.findViewById(R.id.hrmsPayslipOpenMessageTagTextViewID);*/
        appointmentdownload = (ImageView)view.findViewById(R.id.appointmentdownload);
        incrementdownload = (ImageView)view.findViewById(R.id.incrementdownload);
        incrementstructuredownload = (ImageView)view.findViewById(R.id.incrementstructuredownload);

        docdownload1 = (ImageView)view.findViewById(R.id.docdownload1);
        docdownload2 = (ImageView)view.findViewById(R.id.docdownload2);
        docdownload3 = (ImageView)view.findViewById(R.id.docdownload3);
        docdownload4 = (ImageView)view.findViewById(R.id.docdownload4);

        docFileExtList = new ArrayList<String>(0);
        docTypeList = new ArrayList<String>(0);
        docUrlList = new ArrayList<String>(0);
        fileNameList = new ArrayList<String>(0);
        policyTitleList = new ArrayList<String>(0);




        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

      //  getPaySlipLink();

        getPolicyLink();

//---------------------------------------------------------------------
/*
        appointmentdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(docUrl.toString().startsWith("http")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(docUrl.toString()));
                    getActivity().startActivity(i);
                }
                else{
                    Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                }

            }
        });

 */

/*        policylistview.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    //Toast.makeText(getActivity(), "position:"+ String.valueOf(position), Toast.LENGTH_SHORT).show();
                    //   loadMonthlyRecords(CURRENT_MONTH);
                }
                if(position==1){
                    //Toast.makeText(getActivity(), "position:"+ String.valueOf(position), Toast.LENGTH_SHORT).show();
                    //   loadMonthlyRecords(PREVIOUS_MONTH);
                }
            }

        });

 */

        /*
        incrementdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IncrementLetterC.toString().startsWith("http")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(IncrementLetterC.toString()));
                    getActivity().startActivity(i);
                }
                else{
                    Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                }
            }
        });

         */
/*
        incrementstructuredownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IncrementLetterD.toString().startsWith("http")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(IncrementLetterD.toString()));
                    getActivity().startActivity(i);
                }
                else{
                    Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                }
            }
        });

 */
//---------------------------------------------------


    }

    private void getPolicyLink(){
        progressDialog.setMessage("Requesting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String EmpID = prefs.getString("USERISDCODE","");
        /* String EmpID = "100407695";*/
        System.out.println("EmpID: "+EmpID);

        baseURL = Constants.base_url_default;

        client = new RestFullClient(Constants.BASE_URL+Constants.GET_POLICY_DOCUMENTS_URI);
    //    client.AddParam("category", prefs.getString("USERID",""));
       // System.out.println("test  "+ prefs.getString("USERID",""));
       // client.AddParam("req_month", MONTH);
       // System.out.println("test  "+ MONTH);
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

                if(STATUS.equalsIgnoreCase("true")){

                    if (jobj.has(TAG_JOBJECT_DATA)) {

                        System.out.println("jobj.has(TAG_JOBJECT_DATA): in STATUS_CODE = 1: "+ jobj.has(TAG_JOBJECT_DATA));
                        System.out.println("hello");
                        JOBJECT_DATA = jobj.getJSONObject(TAG_JOBJECT_DATA);
                        System.out.println("hello2");
                        System.out.println("JOBJECT_DATA:new "+JOBJECT_DATA.toString());

                        docFileExtList.clear();
                        docTypeList.clear();
                        docUrlList.clear();
                        fileNameList.clear();
                        policyTitleList.clear();

                        if(JOBJECT_DATA!=null){
                            System.out.println("entered qqqqq");
                            JSONArray pendingDateArray = JOBJECT_DATA.getJSONArray("documents");
                            System.out.println("entered qqqqq111111");
                            JSONObject c = null;

                            String attendance_date_sub = null;
                            String attendance_associateid = null;
                            String attendance_fname = null;
                            String attendance_lname = null;
                            String attendance_type = null;
                            String attendance_date = null;
                            String reason = null;
                            String leave_type = null;
                            String attendance_time = null;
                            String status = null;
                          //  downloadUri = c.getString("download_url");
                          //  System.out.println("download uri 1>> "+ downloadUri);




                            if(pendingDateArray.length()==0){
                                System.out.println("entered wwww");
                            }
                            if(pendingDateArray.length()!=0){
                                System.out.println("entered qqqq");
                                System.out.println("entered aaaa  "+pendingDateArray.length());

                                for(int i = 0; i < pendingDateArray.length(); i++) {
                                    System.out.println("entered aaaa  "+pendingDateArray.length());

                                    try {
                                        c = pendingDateArray.getJSONObject(i);
                                        System.out.println("C is : " + c);
                                        if (c != null) {
                                            policyTitle = c.getString(TAG_TITLE);
                                            System.out.println("policyTitle: " + policyTitle);

                                            fileName = c.getString(TAG_FILE_NAME);
                                            System.out.println("fileName: " + fileName);

                                            docFileExt = c.getString(TAG_FILE_EXT);
                                            System.out.println("docFileExt: " + docFileExt);

                                            docType = c.getString(TAG_DOC_TYPE);
                                            System.out.println("docType: " + docType);

                                            docUrl = c.getString(TAG_DOC_URL);
                                            System.out.println("DOCurl: " + docUrl);

                                            fileNameList.add(fileName);
                                            policyTitleList.add(policyTitle);
                                            docFileExtList.add(docFileExt);
                                            docTypeList.add(docType);
                                            docUrlList.add(docUrl);


                                            if (docType.equalsIgnoreCase("leave_policy")){
                                             //   appointmentdownload = downloadUri+fileName;
                                            }
                                        }
                                    } catch (Exception e) {
                                    }

                                }//for

                            }//if(pendingDateArray.length()!=0)


                        }//if(JOBJECT_DATA!=null)

                    }//if(jobj.has(TAG_JOBJECT_DATA))

                }//if(STATUS.equalsIgnoreCase("true"))

            }//if(client.responseCode==200)

            if(client.responseCode!=200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

            }//if(client.responseCode!=200)

        }
        catch(Exception e){}

    }

    private void getPaySlipLink(){
        progressDialog.setMessage("Requesting... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String EmpID = prefs.getString("USERISDCODE","");
       /* String EmpID = "100407695";*/
        System.out.println("EmpID: "+EmpID);

        baseURL = Constants.base_url_default;
        SOAPRequestXML = Constants.soapRequestHeader+
                "<soapenv:Header/>"
                +"<soapenv:Body>"
                +"<tem:GetLetter>"
                //+"<tem:emp_code>"+100172254+"</tem:emp_code>"
                +"<tem:emp_code>"+EmpID+"</tem:emp_code>"
                +"</tem:GetLetter>"
                +"</soapenv:Body>"
                +"</soapenv:Envelope>";

        //String msgLength = String.format("%1$d", SOAPRequestXML.length());
        System.out.println("Request== "+SOAPRequestXML);

        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {

                    HttpPost httppost = new HttpPost(baseURL);
                    StringEntity se = new StringEntity(SOAPRequestXML, HTTP.UTF_8);
                    se.setContentType("text/xml");
                    httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
                    httppost.setEntity(se);
                    HttpClient httpclient = new DefaultHttpClient();
                    httpResponse = null;
                    httpResponse = (HttpResponse) httpclient.execute(httppost);
                    String Response = new BasicResponseHandler().handleResponse(httpResponse);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new StringReader(Response));
                    //int eventType = xpp.getEventType();

                    System.out.println("Server Response = "+Response);
                    StatusLine status = httpResponse.getStatusLine();
                    System.out.println("Server status code = "+status.getStatusCode());
                    System.out.println("Server httpResponse.getStatusLine() = "+httpResponse.getStatusLine().toString());
                    System.out.println("Server Staus = "+httpResponse.getEntity().toString());

                 //   getParsingElementsForPaySlip(xpp);

                } catch (HttpResponseException e) {
                    Log.i("httpResponse Error = ", e.getMessage());
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);

            }

        }).start();

    }



   /* private ArrayList<String> parseJokes(XmlPullParser xpp) {
        ArrayList<String> jokes = new ArrayList<String>();
      //  XmlResourceParser xpp = getResources().getXml(R.xml.jokes);


        try {
            int eventType=xpp.getEventType();
            while (eventType!=XmlPullParser.END_DOCUMENT) {
                if(eventType==XmlPullParser.START_TAG){
                    if (xpp.getName().equals("joke")) {
                        jokes.add(xpp.nextText());
                    }
                }

                eventType= xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("XmlPullParserException", e.toString());
        }
        xpp.close();
        return jokes;
    }*/
    
    
    //getParsingElementsForPaySlip(xpp);
    public void getParsingElementsForPaySlip(XmlPullParser xpp){
        String text = "";
        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagname;
                // = xpp.getName()
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagname=xpp.getName();
                        if(tagname.equalsIgnoreCase(TAG_PayslipResult)) {

           /*to handle nested tags.
           "xpp.nextTag()" goes to the next starting tag  immediately following "ItemArray",
           and "itemID = xpp.nextText()" assigns the text within that tag
            to the string itemID*/

                            xpp.nextTag();
                            AppointmentLetter = xpp.nextText();
                            Log.d("Listing ", AppointmentLetter);


                            xpp.nextTag();
                            IncrementLetterC = xpp.nextText();
                            Log.d("Listing ", IncrementLetterC);

                            xpp.nextTag();
                            IncrementLetterD = xpp.nextText();
                            Log.d("Listing ", IncrementLetterD);

                           /* xpp.nextTag();
                            listingType = xpp.nextText();
                            Log.d("Listing ", listingType);*/

                        }


                        break;

                    case XmlPullParser.TEXT:
                      /*  text = xpp.getText().trim().toString();
                        System.out.println("Text data: "+text);*/
                        break;


                    case XmlPullParser.END_TAG:

                        /*if(tagname.equalsIgnoreCase(TAG_PayslipResult)){
                            PayslipResult = text;
                            text = "";
                            System.out.println("PayslipResult: "+PayslipResult);
                        }*/
                        break;

                    default:
                        break;

                }//switch

                eventType = xpp.next();

            }//while()

        }//try
        catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//getParsingElementsForLogin(xpp);

    Handler handler = new Handler(){

        public void handleMessage(Message msg){
            try {
                if((progressDialog != null) && progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
            }catch (final Exception e) {
                e.printStackTrace();
            }
            setPolicyLink();
          //  setPaySlipLink();
        }//handleMessage(Message msg)

    };
    private void setPolicyLink(){

        if (docTypeList.size() != 0){
            for(int j =0; j < docTypeList.size(); j++){
              //  String  ptl = policyTitleList(j);
                String urllistvalue;
                  //  urllistvalue = docUrlList.get(j);



                if ( j == 0){
                    urllistvalue = docUrlList.get(j);
                    apoointletter.setText(policyTitleList.get(j));
                    apoointletter.setVisibility(View.VISIBLE);
                    System.out.println("icontest" + docFileExtList.get(j));
                    if (docFileExtList.get(j).equalsIgnoreCase(".pdf")){
                        System.out.println("icontest" + docFileExtList.get(j));
                        appointmentdownload.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase(".doc")){
                        appointmentdownload.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                   }
                    else if (docFileExtList.get(j).equalsIgnoreCase(".xls")){
                       // System.out.println("icontest2" + docFileExtList.get(j));
                        appointmentdownload.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        appointmentdownload.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    appointmentdownload.setClickable(true);
                    appointmentdownload.setVisibility(View.VISIBLE);

                    if ( urllistvalue!= null) {
                        appointmentdownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (urllistvalue.startsWith("http")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(urllistvalue.toString()));
                                    getActivity().startActivity(i);


                                } else {
                                    Toast.makeText(getActivity(), "Invalid Download Link!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }


                }
                else if (j == 1){
                    incrementletter.setText(policyTitleList.get(j));
                    incrementletter.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                        //  System.out.println("icontest");
                        incrementdownload.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase(".doc")){
                        incrementdownload.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        incrementdownload.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        incrementdownload.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    incrementdownload.setClickable(true);
                    incrementdownload.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);
                    incrementdownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else if ( j == 2){

                    incrementstructure.setText(policyTitleList.get(j));
                    incrementstructure.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                        //  System.out.println("icontest");
                        incrementstructuredownload.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".doc")){
                        incrementstructuredownload.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        incrementstructuredownload.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        incrementstructuredownload.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    incrementstructuredownload.setClickable(true);
                    incrementstructuredownload.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);

                    incrementstructuredownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else if( j == 3){

                    docother1.setText(policyTitleList.get(j));
                    docother1.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                        //  System.out.println("icontest");
                        docdownload1.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".doc")){
                        docdownload1.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        docdownload1.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        docdownload1.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    docdownload1.setClickable(true);
                    docdownload1.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);

                    docdownload1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else if( j == 4){

                    docother2.setText(policyTitleList.get(j));
                    docother2.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                        //  System.out.println("icontest");
                        docdownload2.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".doc")){
                        docdownload2.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        docdownload2.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        docdownload2.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    docdownload2.setClickable(true);
                    docdownload2.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);

                    docdownload2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else if( j == 5){

                    docother3.setText(policyTitleList.get(j));
                    docother3.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                        //  System.out.println("icontest");
                        docdownload3.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".doc")){
                        docdownload3.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        docdownload3.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        docdownload3.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }
                    docdownload3.setClickable(true);
                    docdownload3.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);

                    docdownload3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else if( j == 6){

                    docother4.setText(policyTitleList.get(j));
                    docother4.setVisibility(View.VISIBLE);
                    if (docFileExtList.get(j).equalsIgnoreCase( ".pdf")){
                          System.out.println("icontest");
                        docdownload4.setImageDrawable(getResources().getDrawable(R.drawable.pdf_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".doc")){
                        docdownload4.setImageDrawable(getResources().getDrawable(R.drawable.word_icon));
                    }
                    else if (docFileExtList.get(j).equalsIgnoreCase( ".xls")){
                        docdownload4.setImageDrawable(getResources().getDrawable(R.drawable.excel_icon));
                    }
                    else{
                        docdownload4.setImageDrawable(getResources().getDrawable(R.drawable.file_download));
                    }

                    docdownload4.setClickable(true);
                    docdownload4.setVisibility(View.VISIBLE);

                    urllistvalue = docUrlList.get(j);

                    docdownload1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(urllistvalue.toString().startsWith("http")){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(urllistvalue.toString()));
                                getActivity().startActivity(i);
                            }
                            else{
                                Toast.makeText(getActivity(),"Invalid Download Link!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getActivity(),"Download Link not found!",Toast.LENGTH_SHORT).show();

                }

            }

        }

    }
    private void setPaySlipLink(){

        if(AppointmentLetter.startsWith("http")){
            //hrmsPayslipDownloadLinkTagTextViewID.setVisibility(View.VISIBLE); //Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setPaintFlags(hrmsPayslipPagePaySlipLinkTextViewID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//Adds Underline;//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setText(PayslipResult);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setTextColor(Color.BLUE);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setClickable(true);//Used Earlier with Textview link


            appointmentdownload.setClickable(true);
            appointmentdownload.setVisibility(View.VISIBLE);

        }
        else{
            appointmentdownload.setClickable(false);
            appointmentdownload.setVisibility(View.GONE);
            if(!AppointmentLetter.equalsIgnoreCase("")){

             //   showPayslipStatusDialog(AppointmentLetter);
            }
            else{

                showPayslipStatusDialog("Document is not available!");
            }

        }


//increment
        if(IncrementLetterC.startsWith("http")){
            //hrmsPayslipDownloadLinkTagTextViewID.setVisibility(View.VISIBLE); //Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setPaintFlags(hrmsPayslipPagePaySlipLinkTextViewID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//Adds Underline;//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setText(PayslipResult);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setTextColor(Color.BLUE);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setClickable(true);//Used Earlier with Textview link


            incrementdownload.setClickable(true);
            incrementdownload.setVisibility(View.VISIBLE);

        }
        else{
            incrementdownload.setClickable(false);
            incrementdownload.setVisibility(View.GONE);
            if(!IncrementLetterC.equalsIgnoreCase("")){

             //   showPayslipStatusDialog(IncrementLetterC);
            }
            else{

                showPayslipStatusDialog("Document is not available!");
            }

        }


        ///structure
        if(IncrementLetterD.startsWith("http")){
            //hrmsPayslipDownloadLinkTagTextViewID.setVisibility(View.VISIBLE); //Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setPaintFlags(hrmsPayslipPagePaySlipLinkTextViewID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//Adds Underline;//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setText(PayslipResult);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setTextColor(Color.BLUE);//Used Earlier with Textview link
            //hrmsPayslipPagePaySlipLinkTextViewID.setClickable(true);//Used Earlier with Textview link


            incrementstructuredownload.setClickable(true);
            incrementstructuredownload.setVisibility(View.VISIBLE);

        }
        else{
            incrementstructuredownload.setClickable(false);
            incrementstructuredownload.setVisibility(View.GONE);
            if(!IncrementLetterD.equalsIgnoreCase("")){

            //    showPayslipStatusDialog(IncrementLetterD);
            }
            else{

                showPayslipStatusDialog("Document is not available!");
            }

        }




        /* doc=new ArrayList<>();
        doc.add(AppointmentLetter);
        doc.add(IncrementLetterC);
        doc.add(IncrementLetterD);*/




    }

    private void initAllViews(){
        //shared preference
        prefs = getActivity().getSharedPreferences(CommonUtils.PREFERENCE_NAME,getActivity().MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //Progress Dialog
        progressDialog = new ProgressDialog(getActivity());

        calendar = Calendar.getInstance();
    }

    //Show Payslip Status Dialog
    private void showPayslipStatusDialog(String msg){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
        aldb.setMessage(msg);
        aldb.setPositiveButton("OK", null);
        aldb.show();
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

}//Main Class
