package com.michaeltroger.datarecording;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class DatarecordingRemotecontrolService extends WearableListenerService {

    private static final String TAG = DatarecordingRemotecontrolService.class.getSimpleName();

    private GoogleApiClient googleApiClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "DatarecordingRemotecontrolService");

        startInForeground();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        return START_NOT_STICKY;
    }

    private void startInForeground() {
        final NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        final Notification notification = notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("remote control")
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(102, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
        );
        chan.setLightColor(Color.BLUE);
        chan.setImportance(NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        final NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        final String data = new String(messageEvent.getData());

        Log.d("received msg path:", path);
        Log.d("received msg:", data);

        switch (data) {
            case "start":
                Log.d(TAG, "start recording");
                break;
            case "stop":
                Log.d(TAG, "stop recording");
                break;
        }
    }
}
