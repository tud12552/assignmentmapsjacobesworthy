package com.example.jse58.assignment_maps_jacobesworthy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.graphics.Color;


public class BroadcastReceiverMap extends BroadcastReceiver {

    private int i = 0;

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

    private NotificationChannel notificationChannel;

    private String CHANNELID = "BROADCAST";
    private String CHANNEL_DESC = "BROADCAST_MAP";
    private int CHANNEL_IMPORT = NotificationManager.IMPORTANCE_HIGH;

    @Override
    public void onReceive(Context context, Intent intent) {
        i++;

        Location bCastLoc = (Location)intent.getSerializableExtra("CUSTOM_LOCATION");

        notificationChannel = new NotificationChannel(CHANNELID,CHANNEL_DESC,CHANNEL_IMPORT);
        notificationChannel.setDescription(CHANNEL_DESC);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setShowBadge(true);

        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationBuilder = new Notification.Builder(context, CHANNELID);

        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);

        notificationBuilder.setContentTitle(bCastLoc.getLocation());
        notificationBuilder.setContentText("Located at " + bCastLoc.getLatitude() + " , " + bCastLoc.getLongitude());

        notificationManager.createNotificationChannel(notificationChannel);

        notificationManager.notify(i,notificationBuilder.build());
    }
}
