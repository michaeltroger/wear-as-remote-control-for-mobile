package com.michaeltroger.datarecording;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DatarecordingRemotecontrolService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        final String data = new String(messageEvent.getData());

        Log.d("received msg path:", path);
        Log.d("received msg:", data);
    }
}
