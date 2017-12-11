package com.michaeltroger.remotecontrol;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DatarecordingRemotecontrolListenerService extends WearableListenerService {

    private static final String TAG = DatarecordingRemotecontrolListenerService.class.getSimpleName();

    private static final String START_COMMAND = "start";
    private static final String STOP_COMMAND = "stop";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String command = new String(messageEvent.getData());
        Log.d(TAG, "received " + command + " command from wear");

        switch (command) {
            case START_COMMAND:
                Utilities.startRecording(getApplicationContext());
                break;
            case STOP_COMMAND:
                Utilities.stopRecording(getApplicationContext());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
