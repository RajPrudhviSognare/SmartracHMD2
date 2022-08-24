package com.experis.smartrac_HMD2;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ImageCapture1 extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    //private ZoomControls imageCapture_zoomControlsID;
    private FloatingActionButton fab_Camera;
    private ImageView imageCapturetopbarBackImageID;

    private PowerManager.WakeLock mWakeLock;
    private Camera.Parameters params;
    private Camera mCamera = null;
    File imagePath;
    MediaPlayer mp;

    private boolean hasFlash = false;
    private File finalImageFilePath = null;

    public static boolean camera2_status1 = false;
    public static String CAMERA2_IMAGEPATH1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.imagecapture1);

        mCamera = getCameraInstance();

        if(mCamera==null){
            Toast.makeText(ImageCapture1.this,"No Camera Found", Toast.LENGTH_LONG).show();
        }

        imageCapturetopbarBackImageID = (ImageView)findViewById(R.id.imageCapturetopbarBackImageID1);
        fab_Camera = (FloatingActionButton)findViewById(R.id.fab_Camera1);

        surfaceView = (SurfaceView)findViewById(R.id.imageCapture_surfaceview1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        //Image Capture Button Listener
        fab_Camera.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

		    	/*
		    	params = mCamera.getParameters();
		    	//Added Later
		    	params.setJpegQuality(CameraProfile.getJpegEncodingQualityParameter(0, CameraProfile.QUALITY_HIGH));
		    	params.setAntibanding("auto");

		    	List<Size> sizes = params.getSupportedPictureSizes();
		    	System.out.println("Total Camera Picture Sizes are: "+sizes.size());
		    	System.out.println("Camera Picture Sizes are: "+sizes.toString());
		        Size size = sizes.get(0);

		        for (int i = 0; i <= sizes.size()/3; i++) {
		            if (sizes.get(i).width > size.width)
		                size = sizes.get(i);

		            System.out.println("Picture Size: "+size.width+","+size.height);
		        }

		        System.out.println("Finally Setting Picture Size: "+size.width+","+size.height);
				params.setPreviewSize(size.width, size.height); ////Added Later
		        params.setPictureSize(size.width, size.height);
		        //Finish of Added Later
		        */

                if(mCamera!=null){
		    		/*if(hasFlash){
		    			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    				//mCamera.setParameters(params);
		    		}*/

                    ////mCamera.setParameters(params);

                    playSound();
                    // get an image from the camera
                    try{
                        mCamera.takePicture(null, null, null, mPicture);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(ImageCapture1.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }//if(mCamera!=null)

            }

        });

        //Exit Button Listener
        imageCapturetopbarBackImageID.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ImageCapture1.this.finish();
            }

        });


        //Added Later//////////////////
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            System.out.println("Inside ImageCapture1 Page checkAllPermissions() is called Above Lallipop: ");
            try{
                checkAllPermissions();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //End Of Added Later//////////

    }//onCreate()

    private void checkAllPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ImageCapture1.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImageCapture1.this,
                    android.Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ImageCapture1.this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1991);

                // The callback method gets the result of the request.
            }

        }//if

    }//checkAllPermissions()

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1991: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // tasks you need to do.
                    System.out.println("CAMERA, WRITE_EXTERNAL_STORAGE & READ_EXTERNAL_STORAGE ARE GRANTED!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("CAMERA, WRITE_EXTERNAL_STORAGE & READ_EXTERNAL_STORAGE ARE REJECTED!");
                    ImageCapture1.this.finish();
                }
                return;
            }

        }

    }

    @SuppressWarnings("deprecation")
    public Camera.PictureCallback mPicture = new Camera.PictureCallback(){

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

			/*if(hasFlash){
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(params);
			}*/

            Calendar cal = Calendar.getInstance();

            releaseCamera();

            finalImageFilePath = new File(Environment.getExternalStorageDirectory().toString()+"/Self_Pictures/Picture_"+cal.getTimeInMillis()+".jpg");
            System.out.println("finalImageFilePath: "+finalImageFilePath);

            if(finalImageFilePath == null){
                camera2_status1 = false;
                CAMERA2_IMAGEPATH1 = "";
                return;
            }
            else{
                CAMERA2_IMAGEPATH1 = finalImageFilePath.getAbsolutePath().toString();
                System.out.println("CAMERA2_IMAGEPATH: "+CAMERA2_IMAGEPATH1);
            }

            try {
                FileOutputStream fos = new FileOutputStream(finalImageFilePath);
                fos.write(data);
                fos.close();
                Toast.makeText(getApplicationContext(), "Picture Taken Successfully", Toast.LENGTH_SHORT).show();

                ////showImagePreview(finalImageFilePath.toString());

                //showImagePreview1(finalImageFilePath.toString());

                camera2_status1 = true;
                ImageCapture1.this.finish();
            } catch (FileNotFoundException e) {
                Log.e("ImageCapture", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.e("ImageCapture", "Error accessing file: " + e.getMessage());
            }

        }

    };

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance  //0==Back; 1==Front
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e("Camera Failed to Open: ", e.getMessage());
        }

        return c; // returns null if camera is unavailable
    }

    private void playSound(){

        mp = MediaPlayer.create(ImageCapture1.this, R.raw.light_switch_on);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });

        if(mp!=null){
            mp.start();
        }
    }

    @Override
    public void onPause() {
        releaseCamera();  // release the camera immediately on pause event
        super.onPause();
        finish();
    }

    @SuppressWarnings("deprecation")
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();  // release the camera for other applications
            mCamera = null;
        }
    }

    public void onBackPressed(){
        releaseCamera();
        camera2_status1 = false;
        CAMERA2_IMAGEPATH1 = "";
        ImageCapture1.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

        if(surfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        } catch (Exception e){}

        // start preview with new settings
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.e("ImageCapture1", "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

        if (mCamera != null){
            try {

                //Added Later
			/*params = mCamera.getParameters();
			params.setJpegQuality(CameraProfile.getJpegEncodingQualityParameter(0, CameraProfile.QUALITY_HIGH));
			params.setAntibanding("auto");

			List<Size> sizes = params.getSupportedPictureSizes();
			System.out.println("Total Camera Picture Sizes are: "+sizes.size());
			System.out.println("Camera Picture Sizes are: "+sizes.toString());
			Size size = sizes.get(0);

			for (int i = 0; i <= sizes.size()/3; i++) {
				if (sizes.get(i).width > size.width)
					size = sizes.get(i);

				System.out.println("Picture Size: "+size.width+","+size.height);
			}

			System.out.println("Finally Setting Picture Size: "+size.width+","+size.height);
			params.setPreviewSize(size.width, size.height); ////Added Later
			params.setPictureSize(size.width, size.height);
			mCamera.setParameters(params);*/
                //Finish of Added Later

                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();

            }//try
            catch (IOException e) {
                Log.e("ImageCapture1", "Error setting camera preview: " + e.getMessage());
            }
        }
        else {
            finish();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        releaseCamera();
    }


}//Main Class
