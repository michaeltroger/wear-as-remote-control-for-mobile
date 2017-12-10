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
                .build();

        setupDatarecodingRemotecontrol();
    }

    public void click(@NonNull final View view) {
        sendMessageToMobile("hello");
    }

    private void sendMessageToMobile(@NonNull final String text) {
        if (transcriptionNodeId != null) {
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
        } else {
            Log.e(TAG, "Unable to retrieve node with datarecording remotecontrol capability");
        }
    }

    private void setupDatarecodingRemotecontrol() {
        final CapabilityApi.CapabilityListener capabilityListener = this::updateTranscriptionCapability;

        Wearable.CapabilityApi.addCapabilityListener(
                googleApiClient,
                capabilityListener,
                DATARECORDING_REMOTECONTROL_CAPABILITY_NAME);
    }

    private void updateTranscriptionCapability(final CapabilityInfo capabilityInfo) {
        final Set<Node> connectedNodes = capabilityInfo.getNodes();

        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(final Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

}
