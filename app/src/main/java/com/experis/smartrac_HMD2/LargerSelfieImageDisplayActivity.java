package com.experis.smartrac_HMD2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class LargerSelfieImageDisplayActivity extends AppCompatActivity {

    private ImageView largerSelfieImageViewID;
    private Intent intent;
    private ImageLoader imageLoader;
    private String path = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.larger_selfie_in_approve_attendance);

            largerSelfieImageViewID = (ImageView)findViewById(R.id.largerSelfieImageViewID);
            imageLoader = new ImageLoader(this);

            intent = getIntent();
            path = intent.getStringExtra("IMAGEPATH");
            System.out.println("Image Path In LargerSelfieImageDisplayActivity: "+path);

            if(path.equalsIgnoreCase("")||path.equalsIgnoreCase("null")||path.contains("null")){
                largerSelfieImageViewID.setImageResource(R.drawable.noimage);
            }
            else{
                largerSelfieImageViewID.setImageBitmap(imageLoader.getBitmap(path));
            }

        }//onCreate()

    public void onBackPressed(){
        LargerSelfieImageDisplayActivity.this.finish();
    }

}//Main Class
