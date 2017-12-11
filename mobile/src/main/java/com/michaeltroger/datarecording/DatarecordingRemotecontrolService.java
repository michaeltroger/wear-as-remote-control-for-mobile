package com.michaeltroger.datarecording;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DatarecordingRemotecontrolService extends WearableListenerService {

    private static final String TAG = DatarecordingRemotecontrolService.class.getSimpleName();

    private static final String START_COMMAND = "start";
    private static final String STOP_COMMAND = "stop";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String command = new String(messageEvent.getData());

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
