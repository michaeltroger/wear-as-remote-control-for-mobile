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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String command = new String(messageEvent.getData());

        switch (command) {
            case "start":
                Utilities.startRecording(getApplicationContext());
                break;
            case "stop":
                Utilities.stopRecording(getApplicationContext());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
