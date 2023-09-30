
package com.water.eldowas.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.water.eldowas.R;
import com.water.eldowas.model.NotificationModel;
import com.water.eldowas.ui.activity.MainActivity;
import com.water.eldowas.ui.activity.MpesaActivity;
import com.water.eldowas.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.water.eldowas.util.AppConstants.PUSH_NOTIFICATION;

/**
 * Created  on 6/30/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String ADMIN_CHANNEL_ID = "com.myduka.eldowas";
    private DatabaseReference databaseReference ;
    private FirebaseAuth firebaseAuth;
    private String userid;
    public static final  String NOTIFICATION = "notifications";
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    private NotificationUtils notificationUtils;
    private String accountIndex;
    private String body;
    private String title;
    private String message;
    private String imageUrl;
    private String timestamp;
    private Map<String, String> data;
    private RemoteMessage remoteMessages;
    private NotificationManager notificationManager;
    private String messages;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        databaseReference = FirebaseDatabase.getInstance().getReference ();
        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth .getCurrentUser().getUid();
        data = remoteMessage.getData();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        //you can get your text message here.
        String text= data.get("text");

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {

            Map<String, Object> userData = new HashMap<String, Object>();
            HashMap<String, Object> dateLastChangedObj = new HashMap<>();
            dateLastChangedObj.put("date", ServerValue.TIMESTAMP);

           // imageUrl =  remoteMessage.getData().get("image");


            body =  remoteMessage.getData().get("message");
            title = body.substring(0,body.indexOf('#') );
            message = body.substring(body.indexOf('#') + 1,body.length() );
            messages = body.substring(body.indexOf('#') + 1,body.length() );

            accountIndex =  databaseReference.child(NOTIFICATION).child(userid).push().getKey();
            NotificationModel notificationModel= new NotificationModel(title, message ,"", dateLastChangedObj, accountIndex);

            databaseReference.child(NOTIFICATION).child(userid).child(accountIndex).setValue(notificationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }
            });

            handleNotification(title);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            Intent resultIntent = new Intent(getApplicationContext(), MpesaActivity.class);
            resultIntent.putExtra("message", message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.putExtra("MAIN", "MAIN");
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"id_product")
                            .setSmallIcon(R.drawable.eldowas_final) //your app icon
                            .setBadgeIconType(R.drawable.eldowas_final) //your app icon
                            .setChannelId(ADMIN_CHANNEL_ID)
                            .setContentTitle(title)
                            .setAutoCancel(true).setContentIntent(pendingIntent)
                            .setNumber(1)
                            .setColor(255)
                            .setContentText(messages)
                            .setWhen(System.currentTimeMillis());
                    notificationManager.notify(1, notificationBuilder.build());
                }



                showNotificationMessage(getApplicationContext(), title, messages, data.get("timestamp"), resultIntent);
            } else {
                // image is present, show notification with image
                Log.e(TAG, "imageUrl: " + imageUrl);
               showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }

        }
    }

    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            //imageUrl = data.getString("image");
            timestamp = data.getString("timestamp");

            JSONObject payload = data.getJSONObject("payload");




            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MpesaActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                   showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {

        } catch (Exception e) {

        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}