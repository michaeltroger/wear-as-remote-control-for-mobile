package com.michaeltroger.datarecording;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class MainActivity extends WearableActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DATARECORDING_REMOTECONTROL_MESSAGE_PATH = "/datarecording_remotecontrol";
    private static final String DATARECORDING_REMOTECONTROL_CAPABILITY_NAME = "datarecording_remotecontrol";

    private static final String START_COMMAND = "start";
    private static final String STOP_COMMAND = "stop";

    private GoogleApiClient googleApiClient;
    private String transcriptionNodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAmbientEnabled();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();

        setupDatarecodingRemotecontrol();
    }

    public void start(@NonNull final View view) {
        sendMessageToMobile(START_COMMAND);
    }

    public void stop(@NonNull final View view) {
        sendMessageToMobile(STOP_COMMAND);
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
                    if (result.getStatus().isSuccess()) {
                        Log.d(TAG, "msg sent to mobile");
                    } else {
                        Log.e(TAG, "failed to send msg to mobile");
                    }
                }
        );

    }

    private void setupDatarecodingRemotecontrol() {
        new Thread(() -> {
            final CapabilityApi.GetCapabilityResult result =
                    Wearable.CapabilityApi.getCapability(
                            googleApiClient,
                            DATARECORDING_REMOTECONTROL_CAPABILITY_NAME,
                            CapabilityApi.FILTER_REACHABLE
                    ).await();

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

}
