package com.michaeltroger.datarecording;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

public class Utilities {

    public static void startRecording(@NonNull final Context context) {
        final Intent intent =  new Intent(context, SensorRecordingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopRecording(@NonNull final Context context) {
        final Intent intent =  new Intent(context, SensorRecordingService.class);
        context.stopService(intent);
    }
}
