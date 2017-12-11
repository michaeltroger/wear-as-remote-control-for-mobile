package com.michaeltroger.remotecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(final View view) {
        Utilities.startRecording(this);
    }

    public void stop(final View view) {
        Utilities.stopRecording(this);
    }

}
