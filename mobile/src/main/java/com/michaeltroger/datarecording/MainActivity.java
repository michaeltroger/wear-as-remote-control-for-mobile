package com.michaeltroger.datarecording;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utilities.startRemotecontrolListeningService(this);
    }

    public void start(final View view) {
        Utilities.startRecording(this);
    }

    public void stop(final View view) {
        Utilities.stopRecording(this);
    }

}
