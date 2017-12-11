package com.michaeltroger.datarecording;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class SensorRecordingService extends Service implements SensorEventListener {
    private static final int NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_TITLE = "Recording data";

    private static final String CHANNEL_ID = "My Service";
    private static final String CHANNEL_NAME = "My Background Service";

    private NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        startInForeground();

        return START_NOT_STICKY;
    }

    private void startInForeground() {
        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        final Notification notification = notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        final NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setImportance(NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        notificationManager.createNotificationChannel(notificationChannel);
        return CHANNEL_ID;
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
