package com.ga.cloudfeed.networking;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.ga.cloudfeed.R;
import com.ga.cloudfeed.view.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseService extends FirebaseMessagingService {

    public static final int CLOUD_FEED_NOTIFICATION = 55569;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            String url = data.get("url");
            NotificationJob.runJobImmediately(url);

            sendNotification(data.get("title"), this);
        }


    }

    public static void sendNotification(String title, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,
                MainActivity.CLOUD_FEED_CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications_2x)
                .setContentTitle(title)
                .setContentText("Update from " + title)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(CLOUD_FEED_NOTIFICATION, mBuilder.build());
    }

}
