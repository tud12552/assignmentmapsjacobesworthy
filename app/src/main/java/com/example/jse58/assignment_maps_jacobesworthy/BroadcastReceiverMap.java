package com.example.jse58.assignment_maps_jacobesworthy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.graphics.Color;


public class BroadcastReceiverMap extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

    private NotificationChannel notificationChannel;

    private String CHANNELID = "BROADCAST";
    private String CHANNEL_DESC = "BROADCAST_MAP";
    private int CHANNEL_IMPORT = NotificationManager.IMPORTANCE_HIGH;

    @Override
    public void onReceive(Context context, Intent intent) {

        String tmp = intent.getStringExtra("LATITUDE");
        Double lat = Double.valueOf(tmp);
        tmp = intent.getStringExtra("LONGITUDE");
        Double lon = Double.valueOf(tmp);
        String location = intent.getStringExtra("LOCATION");

        notificationChannel = new NotificationChannel(CHANNELID,CHANNEL_DESC,CHANNEL_IMPORT);
        notificationChannel.setDescription(CHANNEL_DESC);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setShowBadge(true);

        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationBuilder = new Notification.Builder(context, CHANNELID);

        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);

        notificationBuilder.setContentTitle(location);
        notificationBuilder.setContentText("Located at " + lat + " , " + lon);

        notificationManager.createNotificationChannel(notificationChannel);

        notificationManager.notify(1,notificationBuilder.build());
    }
}
