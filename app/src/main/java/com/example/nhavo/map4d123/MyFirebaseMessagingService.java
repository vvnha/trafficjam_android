package com.example.nhavo.map4d123;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

import static android.app.Person.*;

/**
 * Created by cafe on 11/08/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANEL_1_ID ="CHANEL1";
    NotificationManager manager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            hienThiThongBao(remoteMessage.getNotification().getBody());
        }
        hienThiThongBao(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
    }

    private void hienThiThongBao(String body, String title) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANEL_1_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel1 = new NotificationChannel(CHANEL_1_ID,"CHANEL 1",NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription(body);
            manager.createNotificationChannel(chanel1);
        }
    }

    @Override
    public void onNewToken(String s) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE TOKEN:",token);
        luuTokenVaoCSDLRieng(token);
    }

    private void luuTokenVaoCSDLRieng(String token) {
        new FireBaseIDTask().execute(token);
    }

    private void hienThiThongBao(String body) {
        hienThiThongBao(body, "google");
    }
}

