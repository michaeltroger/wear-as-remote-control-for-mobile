package com.michaeltroger.datarecording;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_ACTION = "command";
    public static final String NOTIFICATION_STOP_COMMAND = "stop";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String command = intent.getStringExtra(NOTIFICATION_ACTION);

        switch (command) {
            case NOTIFICATION_STOP_COMMAND:
                Utilities.stopRecording(context);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
