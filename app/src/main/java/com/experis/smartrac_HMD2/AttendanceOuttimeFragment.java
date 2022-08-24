package com.experis.smartrac_HMD2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;


public class AttendanceOuttimeFragment extends Fragment {

    private String TAG_ATTENDANCE_TYPE = "out";//"Out Time"
    private static final int REQ_IMAGE_CAPTURE1 = 1512;
    private ImageView attendancePageTakePicturePlusIconImageViewID1;
    private ImageView attendancePagePhotoPreviewAreaImageViewID1;
    private ImageView attendancePageSubmitImageViewID1;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private static Uri capturedImageUri = null;
    private static String realpath = null;
    private int CAMERA_REQUEST  = 2222;
    private Bitmap bitmap = null;

    private String encodedImageIntoString = null;
    private RestFullClient client;

    private String remarks = "";
    private EditText attendancePageReasonValueEditTextID;

    public AttendanceOuttimeFragment() {
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
        //Toast.makeText(getActivity(),"Out Time Fragment",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.attendance_outtime_fragment, container, false);

        attendancePageTakePicturePlusIconImageViewID1 = (ImageView)view.findViewById(R.id.attendancePageTakePicturePlusIconImageViewID1);
        attendancePagePhotoPreviewAreaImageViewID1 = (ImageView)view.findViewById(R.id.attendancePagePhotoPreviewAreaImageViewID1);
        attendancePageSubmitImageViewID1 = (ImageView)view.findViewById(R.id.attendancePageSubmitImageViewID1);

        attendancePageReasonValueEditTextID = (EditText)view.findViewById(R.id.attendancePageReasonValueEditTextID1);

        return view;
    }

    private void initAllViews(){

        //shared preference
        prefs = getActivity().getSharedPreferences(CommonUtils.PREFERENCE_NAME,getActivity().MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //Progress Dialog
        progressDialog = new ProgressDialog(getActivity());

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
        /////////////////
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Take Picture Plus Sign Button Click
        attendancePageTakePicturePlusIconImageViewID1.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*//Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), attendancePageTakePicturePlusIconImageViewID1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.camera_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getActivity(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(R.id.cameraoneID==item.getItemId()){
                            callCamera1();
                        }
                        if(R.id.cameratwoID==item.getItemId()){
                            callCamera2();
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu*/

                callCamera2();

            }
        });

        //Submit Button Click
        attendancePageSubmitImageViewID1.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(getActivity())){

                        validateData();
                    }
                    else{
                        Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }//onActivityCreated(Bundle savedInstanceState)

    //Validate Data locally(Checks whether the image is taken or not)
    private void validateData() {

        if(realpath!=null){
            encodedImageIntoString = CommonUtils.imageToString(realpath);
        }
        if(encodedImageIntoString!=null){
            realpath = null;
            remarks = attendancePageReasonValueEditTextID.getText().toString();
            System.out.println("remarks in OUTTIME: "+remarks);
            sendDataForOuttime();
        }
        else{
            Toast.makeText(getActivity(), "Image is not valid, please try again!", Toast.LENGTH_LONG).show();
        }

    }//validateData

    //For OutTime
    private void sendDataForOuttime(){

        Constants.ASSOCIATE_ID = prefs.getString("USERID","");
        Constants.TL_ID = prefs.getString("TLID","");
        Constants.OUTLET_ID = prefs.getString("USEROUTLETID","");
        Constants.ATTENDANCE_TYPE = TAG_ATTENDANCE_TYPE;
        Constants.ATTENDANCE_IMAGE = encodedImageIntoString;
        //Constants.ATTENDANCE_DATE = "";
        //Constants.ATTENDANCE_DATE = "yyyy-mm-dd";
        Constants.ATTENDANCE_DATE = "0000-00-00";
        Constants.REASON = "";
        Constants.LEAVE_TYPE = "";
        Constants.REMARKS = remarks;

        //Added Later
        encodedImageIntoString = null;
        attendancePagePhotoPreviewAreaImageViewID1.setImageBitmap(null);
        System.out.println("Constants.ATTENDANCE_IMAGE:Outtime "+Constants.ATTENDANCE_IMAGE);
        attendancePageReasonValueEditTextID.setText("");
        ////////////

        Intent i = new Intent(getActivity(),GeoFencingActivity.class);
        //getActivity().finish();
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        /*progressDialog.setTitle("Attendance");
        progressDialog.setMessage("Saving Your Intime... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.ATTENDANCE_RELATIVE_URI);
        client.AddParam("associate_id", prefs.getString("USERID",""));
        client.AddParam("tl_id", prefs.getString("TLID",""));
        client.AddParam("outlet_id", prefs.getString("USEROUTLETID",""));
        client.AddParam("attendance_type", TAG_ATTENDANCE_TYPE);
        client.AddParam("attendance_image", encodedImageIntoString);

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

                //receiveDataForServerResponse(client.jObj);

                //handler.sendEmptyMessage(0);

            }

        }).start();*/

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

      /*  if(resultCode == getActivity().RESULT_OK){

            if (requestCode == CAMERA_REQUEST) {
                try {
                    System.out.println("capturedImageUri: "+capturedImageUri);
                    //if(data!=null){

                        if(capturedImageUri!=null){

                            Log.v("capturedImageUri:",capturedImageUri.toString());
                            System.out.println("data!=null: "+data);
                            //bitmap.recycle(); ////Added Later
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), capturedImageUri);

                            //For MarshMallow(6.0) API 23//
                            if(bitmap==null){
                                Log.v("realpath: ",realpath.toString());
                                File f = new File(realpath);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                try {
                                    //bitmap.recycle(); ////Added Later
                                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            //For MarshMallow(6.0) API 23//

                            if(bitmap!=null) {
                                attendancePagePhotoPreviewAreaImageViewID1.setImageBitmap(bitmap);
                                System.out.println("Bitmap Image: " + bitmap.toString());
                            }

                            //imageSelectionType = "CAMERA";

                        }//if(capturedImageUri!=null)

                    //}//if(data!=null)

                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }
        if(resultCode == getActivity().RESULT_CANCELED){

            if (requestCode == CAMERA_REQUEST) {
                capturedImageUri = null;
                realpath = null;
                System.out.println("capturedImageUri: "+capturedImageUri);
                System.out.println("realpath: "+realpath);
            }

        }*/

        if(resultCode == getActivity().RESULT_OK){

          /*  if (requestCode == CAMERA_REQUEST) {
                try {
                    System.out.println("capturedImageUri: "+capturedImageUri);
                    //if(data!=null){

                        if(capturedImageUri!=null){

                            Log.v("capturedImageUri:",capturedImageUri.toString());
                            System.out.println("data!=null: "+data);
                            //bitmap.recycle(); ////Added Later
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), capturedImageUri);

                            //For MarshMallow(6.0) API 23//
                            if(bitmap==null){
                                Log.v("realpath: ",realpath.toString());
                                File f = new File(realpath);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                try {
                                    //bitmap.recycle(); ////Added Later
                                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            //For MarshMallow(6.0) API 23//

                            if(bitmap!=null) {
                                attendancePagePhotoPreviewAreaImageViewID1.setImageBitmap(bitmap);
                                System.out.println("Bitmap Image: " + bitmap.toString());
                            }

                            //imageSelectionType = "CAMERA";

                        }//if(capturedImageUri!=null)

                    //}//if(data!=null)

                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            attendancePagePhotoPreviewAreaImageViewID1.setImageBitmap(imageBitmap);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encodedImageIntoString = Base64.encodeToString(byteArray, Base64.DEFAULT);


        }
        if(resultCode == getActivity().RESULT_CANCELED){

            if (requestCode == CAMERA_REQUEST) {
                capturedImageUri = null;
                realpath = null;
                System.out.println("capturedImageUri: "+capturedImageUri);
                System.out.println("realpath: "+realpath);
            }

        }

    }

    private void callCamera1(){

        //Camera 1 Logic
        Calendar cal = Calendar.getInstance();
        File file = new File(Environment.getExternalStorageDirectory()+"/Self_Pictures", (cal.getTimeInMillis() + ".jpg"));
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
        i.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(i, CAMERA_REQUEST);
        //End Of Camera 1 Logic
    }

    private void callCamera2(){
       /* Intent intent = new Intent(getActivity(), ImageCapture.class);
        intent.putExtra("ATTENDANCE_TYPE","out");
        getActivity().startActivity(intent);*/
        //getActivity().startActivityForResult(intent,CAMERA_REQUEST1);

        int checkPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
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

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQ_IMAGE_CAPTURE1);
            }
        } else {
            Log.d("PERMISSION", "Permission Denied");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();

        if(ImageCapture.camera2_status){
            ImageCapture.camera2_status = false;

            if(ImageCapture.ATTENDANCE_TYPE.equalsIgnoreCase("out")){

                realpath = ImageCapture.CAMERA2_IMAGEPATH;
                System.out.println("realpath: "+realpath);
                if(realpath!=null){
                    attendancePagePhotoPreviewAreaImageViewID1.setImageURI(Uri.parse(realpath));
                }
                ImageCapture.ATTENDANCE_TYPE = "";
            }

            ImageCapture.CAMERA2_IMAGEPATH = "";

        }//if

    }

    public void onClickPictureBack1() {

        if(ImageCapture.camera2_status){
            ImageCapture.camera2_status = false;

            if(ImageCapture.ATTENDANCE_TYPE.equalsIgnoreCase("out")){

                realpath = ImageCapture.CAMERA2_IMAGEPATH;
                System.out.println("realpath: "+realpath);
                if(realpath!=null){
                    attendancePagePhotoPreviewAreaImageViewID1.setImageURI(Uri.parse(realpath));
                }
                ImageCapture.ATTENDANCE_TYPE = "";
            }

            ImageCapture.CAMERA2_IMAGEPATH = "";

        }//if
    }

}
