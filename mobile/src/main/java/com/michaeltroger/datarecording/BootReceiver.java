package com.michaeltroger.datarecording;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utilities.startRemotecontrolListeningService(context);
    }
}
