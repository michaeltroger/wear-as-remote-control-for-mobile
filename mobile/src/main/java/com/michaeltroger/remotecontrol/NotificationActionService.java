package com.michaeltroger.remotecontrol;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class NotificationActionService extends IntentService {
    private static final String TAG = NotificationActionService.class.getSimpleName();

    public static final String NOTIFICATION_ACTION = "command";
    public static final String NOTIFICATION_STOP_COMMAND = "stop";

    public NotificationActionService(String name) {
        super(name);
    }
    public NotificationActionService() {
        super("NotificationAction");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String command = intent.getStringExtra(NOTIFICATION_ACTION);
        Log.d(TAG, "Received command "+ command +" from notification action");

        switch (command) {
            case NOTIFICATION_STOP_COMMAND:
                Utilities.stopRecording(getApplicationContext());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

}
