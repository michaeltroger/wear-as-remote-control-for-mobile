package com.michaeltroger.datarecording;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String DATARECORDING_REMOTECONTROL_MESSAGE_PATH = "/datarecording_remotecontrol";
    private static final String DATARECORDING_REMOTECONTROL_CAPABILITY_NAME = "datarecording_remotecontrol";
    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient googleApiClient;
    private String transcriptionNodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAmbientEnabled();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        setupDatarecodingRemotecontrol();
    }

    public void start(@NonNull final View view) {
        sendMessageToMobile("start");
    }

    public void stop(@NonNull final View view) {
        sendMessageToMobile("stop");
    }

    private void sendMessageToMobile(@NonNull final String text) {
        if (transcriptionNodeId == null) {
            Log.e(TAG, "Unable to retrieve node with datarecording remotecontrol capability");
            return;
        }

        Wearable.MessageApi.sendMessage(
                googleApiClient,
                transcriptionNodeId,
                DATARECORDING_REMOTECONTROL_MESSAGE_PATH,
                text.getBytes()
        ).setResultCallback(
                result -> {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG, "failed to send msg");
                    }
                }
        );

    }

    private void setupDatarecodingRemotecontrol() {
        new Thread(() -> {
            CapabilityApi.GetCapabilityResult result =
                    Wearable.CapabilityApi.getCapability(
                            googleApiClient, DATARECORDING_REMOTECONTROL_CAPABILITY_NAME,
                            CapabilityApi.FILTER_REACHABLE).await();

            updateTranscriptionCapability(result.getCapability());
        }).start();

        Wearable.CapabilityApi.addCapabilityListener(
                googleApiClient,
                this::updateTranscriptionCapability,
                DATARECORDING_REMOTECONTROL_CAPABILITY_NAME);
    }

    private void updateTranscriptionCapability(final CapabilityInfo capabilityInfo) {
        final Set<Node> connectedNodes = capabilityInfo.getNodes();

        transcriptionNodeId = pickBestNodeId(connectedNodes);
        Log.d(TAG, "best node:"+transcriptionNodeId);
    }

    private String pickBestNodeId(final Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (final Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Google API Client connected");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Google API Client connection fail");
    }
}
