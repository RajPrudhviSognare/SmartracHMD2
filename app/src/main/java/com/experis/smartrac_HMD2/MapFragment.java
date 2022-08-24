package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.experis.smartrac_HMD2.geofenceing.SimpleGeofence;
import com.experis.smartrac_HMD2.geofenceing.SimpleGeofenceStore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {

    protected GoogleMap map;
    protected Marker myPositionMarker;

    private RestFullClient client;
    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAllViews();
        if(CommonUtils.isInternelAvailable(getActivity())){
        }
        else{
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void initAllViews(){

        //shared preference
        prefs = getActivity().getSharedPreferences(CommonUtils.PREFERENCE_NAME,getActivity().MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.map);

        //Used Earlier below play-services to 9.2
        /*if (mapFragment != null) {
            map = mapFragment.getMap();
            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    map.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                    displayGeofences();
                }
            });
        }*/
        //End Of Used Earlier below play-services to 9.2

        //Added Later
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                displayGeofences();
            }
        });
        //End Of Added Later

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(CommonUtils.isInternelAvailable(getActivity())){
            if(Constants.ATTENDANCE_TYPE.equalsIgnoreCase("in")||Constants.ATTENDANCE_TYPE.equalsIgnoreCase("out")){
                System.out.println("Constants.GEOFENCE_ENABLED: inside giveAttendanceforInOuttime(): " + Constants.GEOFENCE_ENABLED);
                giveAttendanceforInOuttime();
            }
        }
        else{
            Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void giveAttendanceforInOuttime(){

        System.out.println("Constants.GEOFENCE_ENABLED: inside giveAttendanceforInOuttime(): " + Constants.GEOFENCE_ENABLED);

      //  if(Constants.GEOFENCE_ENABLED){

            //progressDialog.setTitle("Attendance");
            if(Constants.ATTENDANCE_TYPE.equalsIgnoreCase("in")){
                progressDialog.setMessage("Submitting Your Intime... Please wait!");
            }
            if(Constants.ATTENDANCE_TYPE.equalsIgnoreCase("out")){
                progressDialog.setMessage("Submitting Your Outtime... Please wait!");
            }
            progressDialog.setCancelable(false);
            progressDialog.show();

            client = new RestFullClient(Constants.BASE_URL+Constants.ATTENDANCE_RELATIVE_URI);

            client.AddParam("associate_id", Constants.ASSOCIATE_ID);
            System.out.println("associate_id: "+Constants.ASSOCIATE_ID);

            client.AddParam("tl_id", Constants.TL_ID);
            System.out.println("tl_id: "+Constants.TL_ID);

            client.AddParam("outlet_id", Constants.OUTLET_ID);
            System.out.println("outlet_id: "+Constants.OUTLET_ID);

            client.AddParam("attendance_type", Constants.ATTENDANCE_TYPE);
            System.out.println("attendance_type: "+Constants.ATTENDANCE_TYPE);

            client.AddParam("attendance_image", Constants.ATTENDANCE_IMAGE);
            System.out.println("attendance_image: "+Constants.ATTENDANCE_IMAGE);

            client.AddParam("latitude", Constants.CURRENT_LAT);
            System.out.println("latitude: "+Constants.CURRENT_LAT);

            client.AddParam("longitude", Constants.CURRENT_LONG);
            System.out.println("longitude: "+Constants.CURRENT_LONG);

            client.AddParam("distance", Constants.DISTANCE);
            System.out.println("distance: "+Constants.DISTANCE);

            client.AddParam("attendance_date", Constants.ATTENDANCE_DATE);
            System.out.println("attendance_date: "+Constants.ATTENDANCE_DATE);

            client.AddParam("reason", Constants.REASON);
            System.out.println("reason: "+Constants.REASON);

            client.AddParam("remarks", Constants.REMARKS);
            System.out.println("remarks: "+Constants.REMARKS);

            client.AddParam("leave_type", Constants.LEAVE_TYPE);
            System.out.println("leave_type: "+Constants.LEAVE_TYPE);

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

      /*  }//if(Constants.GEOFENCE_ENABLED)
        if(!Constants.GEOFENCE_ENABLED){
             *//*if(!Constants.ASSOCIATE_ID.equalsIgnoreCase("")&&!Constants.TL_ID.equalsIgnoreCase("")&&!Constants.OUTLET_ID.equalsIgnoreCase("")
                     &&!Constants.ATTENDANCE_TYPE.equalsIgnoreCase("")&&!Constants.ATTENDANCE_IMAGE.equalsIgnoreCase("")){*//*
            if(!Constants.ASSOCIATE_ID.equalsIgnoreCase("")||!Constants.TL_ID.equalsIgnoreCase("")||!Constants.OUTLET_ID.equalsIgnoreCase("")
                    ||!Constants.ATTENDANCE_TYPE.equalsIgnoreCase("")||!Constants.ATTENDANCE_IMAGE.equalsIgnoreCase("")){

                 System.out.println("Constants.ASSOCIATE_ID: " + Constants.ASSOCIATE_ID);
                 System.out.println("Constants.TL_ID: " + Constants.TL_ID);
                 System.out.println("Constants.OUTLET_ID: " + Constants.OUTLET_ID);
                 System.out.println("Constants.ATTENDANCE_TYPE: " + Constants.ATTENDANCE_TYPE);
                // System.out.println("Constants.ATTENDANCE_IMAGE: " + Constants.ATTENDANCE_IMAGE);
                 System.out.println("Constants.ATTENDANCE_DATE: " + Constants.ATTENDANCE_DATE);
                 System.out.println("Constants.REASON: " + Constants.REASON);
                 System.out.println("Constants.LEAVE_TYPE: " + Constants.LEAVE_TYPE);
                 System.out.println("Constants.REMARKS: " + Constants.REMARKS);
                 System.out.println("distance: "+Constants.DISTANCE);

                 showRetryDialog();

                 //Uncomment this section if Forced Attendance is required in case Lat & Long don't match.
                 *//*Constants.GEOFENCE_ENABLED = true;
                 System.out.println("Attendance TYPE: " + "FORCEFULLY, Because Lat & Long don't match");
                 giveAttendanceforInOuttime();*//*
             }
            else{
                 if(Constants.ATTENDANCE_TYPE.equalsIgnoreCase("in")){
                     Toast.makeText(getActivity(), "You Have Already Submitted Your 'Intime' For Today. Thank You!", Toast.LENGTH_LONG).show();
                 }
                 if(Constants.ATTENDANCE_TYPE.equalsIgnoreCase("out")){
                     Toast.makeText(getActivity(), "You Have Already Submitted Your 'Outtime' For Today. Thank You!", Toast.LENGTH_LONG).show();
                 }

             }

        }//if(!Constants.GEOFENCE_ENABLED)*/

    }//giveAttendanceforInOuttime()

    private void receiveDataForServerResponse(JSONObject jobj) {

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

                //Success
                if(STATUS.equalsIgnoreCase("true")){
                    //showSuccessDialog();
                    //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_LONG).show();

                    //Constants.GEOFENCE_ENABLED = false; //Used Earlier
                    Constants.ASSOCIATE_ID = "";
                    Constants.TL_ID = "";
                    Constants.OUTLET_ID = "";
                    Constants.ATTENDANCE_TYPE = "";
                    Constants.ATTENDANCE_IMAGE = "";
                    Constants.CURRENT_LAT = "0.0";
                    Constants.CURRENT_LONG = "0.0";
                    //Constants.ATTENDANCE_DATE = "";
                    //Constants.ATTENDANCE_DATE = "yyyy-mm-dd";
                    Constants.ATTENDANCE_DATE = "0000-00-00";
                    Constants.REASON = "";
                    Constants.LEAVE_TYPE = "";
                    Constants.REMARKS = "";
                    Constants.DISTANCE = "0";
                    //Constants.GEOFENCE_ENABLED = false;

                    /*Intent geolocationService = new Intent(getActivity(),
                            GeolocationService.class);
                    getActivity().stopService(geolocationService);*/

                   // new GeolocationService().stopSelf();

                    //Alert Dialog Builder
                    final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
                    aldb.setTitle("Success!");
                    aldb.setMessage(MESSAGE);
                    aldb.setCancelable(false);
                    aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        }
                    });
                    aldb.show();
                }

                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    //showRetryDialog();
                    showFailureDialog();
                }
            }
            //Failed
            if(client.responseCode!=200){

                //Toast.makeText(getActivity(), MESSAGE, Toast.LENGTH_LONG).show();
                //showRetryDialog();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
        aldb.setTitle("Attendance Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                //Added later
                Constants.ASSOCIATE_ID = "";
                Constants.TL_ID = "";
                Constants.OUTLET_ID = "";
                Constants.ATTENDANCE_TYPE = "";
                Constants.ATTENDANCE_IMAGE = "";
                Constants.CURRENT_LAT = "0.0";
                Constants.CURRENT_LONG = "0.0";
                Constants.ATTENDANCE_DATE = "0000-00-00";
                Constants.REASON = "";
                Constants.LEAVE_TYPE = "";
                Constants.REMARKS = "";
                Constants.DISTANCE = "0";
                /////////////////

                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        aldb.show();

    }

    //Show Retry Dialog
    private void showRetryDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(getActivity());
        aldb.setTitle("Location Error!");
        aldb.setMessage("Your current location either does not match with our record or\nYou are not inside the assigned Premises. \nWould you still like to continue submitting your attendance?");
        aldb.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /*Intent geolocationService = new Intent(getActivity(),
                        GeolocationService.class);
                getActivity().stopService(geolocationService);

                Intent intent  = new Intent(getActivity(), GeoFencingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);*/

                if(CommonUtils.isInternelAvailable(getActivity())){
                    Constants.GEOFENCE_ENABLED = true; //Force attendance
                    giveAttendanceforInOuttime();
                }
                else{
                    Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_LONG).show();
                }

            }
        });
        aldb.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /*Intent geolocationService = new Intent(getActivity(),
                        GeolocationService.class);
                getActivity().stopService(geolocationService);*/

                //Added later
                Constants.ASSOCIATE_ID = "";
                Constants.TL_ID = "";
                Constants.OUTLET_ID = "";
                Constants.ATTENDANCE_TYPE = "";
                Constants.ATTENDANCE_IMAGE = "";
                Constants.CURRENT_LAT = "0.0";
                Constants.CURRENT_LONG = "0.0";
                Constants.ATTENDANCE_DATE = "0000-00-00";
                Constants.REASON = "";
                Constants.LEAVE_TYPE = "";
                Constants.REMARKS = "";
                Constants.DISTANCE = "0";
                /////////////////

                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        aldb.setNegativeButton("My Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Added later
                Constants.ASSOCIATE_ID = "";
                Constants.TL_ID = "";
                Constants.OUTLET_ID = "";
                Constants.ATTENDANCE_TYPE = "";
                Constants.ATTENDANCE_IMAGE = "";
                Constants.CURRENT_LAT = "0.0";
                Constants.CURRENT_LONG = "0.0";
                Constants.ATTENDANCE_DATE = "0000-00-00";
                Constants.REASON = "";
                Constants.LEAVE_TYPE = "";
                Constants.REMARKS = "";
                Constants.DISTANCE = "0";
                /////////////////
            }
        });
        aldb.show();

    }


    protected void displayGeofences() {
        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance().getSimpleGeofences();

        for(Map.Entry<String, SimpleGeofence>item : geofences.entrySet()){
            SimpleGeofence sg = item.getValue();

            CircleOptions circleOptions1 = new CircleOptions()
                    .center(new LatLng(sg.getLatitude(), sg.getLongitude()))
                    .radius(sg.getRadius()).strokeColor(Color.BLACK)
                    .strokeWidth(3).fillColor(0x500000ff);
            map.addCircle(circleOptions1);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("done");
                if (resultCode == 1) {
                    Double latitude = bundle.getDouble("latitude");
                    Double longitude = bundle.getDouble("longitude");
                    System.out.println("Current Lat: "+latitude);
                    System.out.println("Current long: "+longitude);
                    try{
                        if(latitude!=null&&longitude!=null){
                            updateMarker(latitude, longitude);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    protected void createMarker(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        //myPositionMarker = map.addMarker(new MarkerOptions().position(latLng).title("Lat:"+String.valueOf(latitude)+",Long:"+String.valueOf(longitude))); //Does Not Work
        myPositionMarker = map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    protected void updateMarker(Double latitude, Double longitude) {
        if (myPositionMarker == null) {
            createMarker(latitude, longitude);
        }

        LatLng latLng = new LatLng(latitude, longitude);
        myPositionMarker.setPosition(latLng);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver,
                new IntentFilter("com.experis.attendancetracking.geolocation.service.casio.demo.common"));
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