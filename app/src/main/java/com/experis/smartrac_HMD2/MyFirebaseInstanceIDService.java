package com.experis.smartrac_HMD2;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    @Override
    public void onTokenRefresh() {

        //Shared preference
        prefs = MyFirebaseInstanceIDService.this.getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.i(TAG, "Refreshed FCM Token: " + refreshedToken);

        if(refreshedToken!=null||!refreshedToken.equalsIgnoreCase("")){
            System.out.println("Inside  if(refreshedToken!=null||!refreshedToken.equalsIgnoreCase()): ");
            prefsEditor.putString("GCMREGISTRATIONID", refreshedToken);
            prefsEditor.commit();
        }

    }

    private void sendRegistrationToServer(String token) {
        //Implement this method to store the token on your server if needed
    }
}