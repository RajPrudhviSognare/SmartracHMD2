package com.experis.smartrac_HMD2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private String str = "";
    private String USERID = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Shared preference
        prefs = MyFirebaseMessagingService.this.getSharedPreferences(CommonUtils.PREFERENCE_NAME,MODE_PRIVATE);
        prefsEditor = prefs.edit();

        str = prefs.getString("LOGGEDIN","");
        System.out.println("LOGGEDIN in FCM((MyFirebaseMessagingService): "+ str);
        USERID = prefs.getString("USERID","");
        System.out.println("USERID in FCM((MyFirebaseMessagingService): "+ USERID);

        Log.v(TAG, "Message From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) { //Used for custom data
            Log.v(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                 JSONObject json = new JSONObject(remoteMessage.getData());
                 Log.v(TAG, "GCM/FCM json: " + json);
                 String push_type = "";
                 String push_for = ""; //For Message Push
                 String message = "";
                 String ID = "";
                 JSONArray jsonArray = null;

                if(json.has("push_type")){
                     push_type = json.getString("push_type");
                     Log.v(TAG, "GCM/FCM push_type: "+push_type);
                 }
                if(json.has("push_for")){
                    push_for = json.getString("push_for");
                    Log.v(TAG, "GCM/FCM push_for: "+push_for);
                }
                 if(json.has("message")){
                     message = json.getString("message");
                     Log.v(TAG, "GCM/FCM message: "+message);
                 }
                 if(json.has("receiver_id")){
                     ID = json.getString("receiver_id");
                     Log.v(TAG, "GCM/FCM receiver_id: "+ID);
                }

                //Push to TL
                if(push_type.equalsIgnoreCase("tl")){
                            if(ID.equalsIgnoreCase(USERID)){
                                Constants.FROM_PUSH = true;
                                Constants.PUSH_FOR = push_for;
                                System.out.println("USERID is matched with FCM Receiver_ID for TL: "+USERID);
                                sendNotificationToTL(message);
                            }
                }//if(push_type.equalsIgnoreCase("tl"))

                //Push to Associate
                if(push_type.equalsIgnoreCase("associate")){
                    if(ID.equalsIgnoreCase(USERID)){
                        Constants.FROM_PUSH = true;
                        Constants.PUSH_FOR = push_for;
                        System.out.println("USERID is matched with FCM Receiver_ID for Associate: "+USERID);
                        if(push_for.equalsIgnoreCase("message")){
                            sendNotificationToAssociateForMessage(message);
                        }
                        else{
                            Constants.FROM_PUSH = false;
                            Constants.PUSH_FOR = "";
                            sendNotificationToAssociate(message);
                        }

                    }
                }//if(push_type.equalsIgnoreCase("associate"))

                 /******** For Array type*************/
                 /*if(json.has("receiver_id")){
                     jsonArray = json.getJSONArray("receiver_id");
                     System.out.println("jsonArray: "+jsonArray.toString());
                     System.out.println("Total jsonArray elements: "+jsonArray.length());
                 }*/
                //Push to TL
                /*if(push_type.equalsIgnoreCase("tl")){
                    if(jsonArray.length()!=0){
                        for(int i=0;i<jsonArray.length();i++){
                           String id = jsonArray.get(i).toString();
                            if(id.equalsIgnoreCase(USERID)){
                                System.out.println("USERID is matched with FCM Receiver_ID for TL: "+USERID);
                                sendNotificationToTL(message);
                            }
                        }//for
                    }//if(jsonArray.length()!=0)

                }//if(push_type.equalsIgnoreCase("tl"))*/
                //Push to Associate
                /*if(push_type.equalsIgnoreCase("associate")){
                    if(jsonArray.length()!=0){
                        for(int i=0;i<jsonArray.length();i++){
                            String id = jsonArray.get(i).toString();
                            if(id.equalsIgnoreCase(USERID)){
                                System.out.println("USERID is matched with FCM Receiver_ID for Associate: "+USERID);
                                sendNotificationToAssociate(message);
                            }
                        }//for
                    }//if(jsonArray.length()!=0)

                }//if(push_type.equalsIgnoreCase("associate"))*/
                /******** End Of For Array type*************/



            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /*//Calling this method to generate notification
            sendNotification(remoteMessage.getData().toString());*/

        }//if (remoteMessage.getData().size() > 0)

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


    }

    //This method is for generating a push notification to TL
    private void sendNotificationToTL(String messageBody) {
        PendingIntent pendingIntent;
        if(str.equalsIgnoreCase("yes")){
            Constants.FROM_PUSH = false;
            Constants.PUSH_FOR = "";
            Intent intent = new Intent(this, AttendanceApprovalRevisedActivity1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 909090, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 909090, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.applogo1)
                .setContentTitle("Smartrac")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(989898, notificationBuilder.build());
    }

    //This method is for generating a push notification to Associates for Message
    private void sendNotificationToAssociateForMessage(String messageBody){

        PendingIntent pendingIntent;
        if(str.equalsIgnoreCase("yes")){
            Constants.FROM_PUSH = false;
            Constants.PUSH_FOR = "";
            Intent intent = new Intent(this, MessagesForAssociateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 909091, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 909091, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.applogo1)
                .setContentTitle("Smartrac")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(989891, notificationBuilder.build());
    }

    //This method is for generating a push notification to Associates
    private void sendNotificationToAssociate(String messageBody){
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.applogo1)
                .setContentTitle("Smartrac")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(98989899, notificationBuilder.build());
    }


}
