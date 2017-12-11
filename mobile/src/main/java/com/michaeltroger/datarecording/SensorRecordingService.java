package com.michaeltroger.datarecording;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

    private static final String CHANNEL_ID = "com.michaeltroger.datarecording.DATARECORDING";
    private static final String CHANNEL_NAME = "Data recording";


    private static final String NOTIFICATION_STOP_TITLE = "Stop";

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

        final Intent stopRecordingIntent = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        stopRecordingIntent.putExtra(NotificationActionReceiver.NOTIFICATION_ACTION, NotificationActionReceiver.NOTIFICATION_STOP_COMMAND);
        final PendingIntent stopRecordingPendingIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                stopRecordingIntent,
                0);

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        final Notification notification = notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .addAction(R.drawable.ic_launcher_foreground, NOTIFICATION_STOP_TITLE, stopRecordingPendingIntent)
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
