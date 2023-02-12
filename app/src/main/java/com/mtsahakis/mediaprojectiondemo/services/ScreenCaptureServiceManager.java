package com.mtsahakis.mediaprojectiondemo.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mtsahakis.mediaprojectiondemo.ScreenCaptureSetupActivity;
import com.mtsahakis.mediaprojectiondemo.storagemanagers.ProjectionIntentSaver;

public class ScreenCaptureServiceManager {

    static Intent projectionIntent;

    public static void launchScreenCaptureService(Context context) {
        try {
            setProjectionIntent(context);
        } catch (Exception e) {
            Log.i("kalzak", e.toString());
            runNewProjectionSetup(context);
            return;
        }

        Log.i("kalzak", "starting screen capture");
        startScreenCaptureService(context);
    }

    private static void setProjectionIntent(Context context) {
        ProjectionIntentSaver projectionIntentSaver = new ProjectionIntentSaver(context);
        projectionIntent = projectionIntentSaver.getIntent();
        assert projectionIntent != null;
    }

    private static void runNewProjectionSetup(Context context) {
        Intent intent = new Intent(context, ScreenCaptureSetupActivity.class);
        context.startActivity(intent);
    }

    private static void startScreenCaptureService(Context context) {
        Intent projectionIntentCopy = (Intent) projectionIntent.clone();
        context.startService(ScreenCaptureService.getStartIntent(context, Activity.RESULT_OK, projectionIntentCopy));
    }


    public static void disableScreenCaptureService(Context context) {
        context.startService(ScreenCaptureService.getStopIntent(context));
    }
}