package snnafi.bangla.dictionary.admin.notification;


import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import snnafi.bangla.dictionary.admin.ui.SplashActivity;
import snnafi.bangla.dictionary.admin.util.Constant;

;

public class MessageReceive extends FirebaseMessagingService {
    private static final String TAG = MessageReceive.class.getName();
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String URL = "url";
    private static final String REMOVE = "remove";
    private static SharedPreferences prefs = null;


    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

        super.onMessageReceived(remoteMessage);

    }


    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {

    }

    private void handleData(Map<String, String> data) {
        Log.d(TAG, "handleData: " + data.values().toString());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String url = data.get(URL);
        String remove = data.get(REMOVE);

        if (Integer.valueOf(remove) == -1) {
            sendNotification(title, message);

        } else if (Integer.valueOf(remove) == prefs.getInt(Constant.INSTANCE.getID(), -1)) {
            prefs.edit().putBoolean(Constant.INSTANCE.getIS_LOGIN(), false).apply();
            prefs.edit().putBoolean(Constant.INSTANCE.getFETCH_USER(), false).apply();
            prefs.edit().putInt(Constant.INSTANCE.getID(), -1).apply();
            prefs.edit().putString(Constant.INSTANCE.getNAME(), "").apply();
            prefs.edit().putString(Constant.INSTANCE.getEMAIL(), "").apply();
            prefs.edit().putInt(Constant.INSTANCE.getROLE(), -1).apply();
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);


        } else if (Integer.valueOf(remove) == 0) {
            prefs.edit().putBoolean(Constant.INSTANCE.getIS_LOGIN(), false).apply();
            prefs.edit().putBoolean(Constant.INSTANCE.getFETCH_USER(), false).apply();
            prefs.edit().putInt(Constant.INSTANCE.getID(), -1).apply();
            prefs.edit().putString(Constant.INSTANCE.getNAME(), "").apply();
            prefs.edit().putString(Constant.INSTANCE.getEMAIL(), "").apply();
            prefs.edit().putInt(Constant.INSTANCE.getROLE(), -1).apply();
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }


    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String title, String message) {
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setIconUrl("");
        notificationVO.setAction("");
        notificationVO.setActionDestination("");
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }
}