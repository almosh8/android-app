package com.mtsahakis.mediaprojectiondemo.services;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.util.Log;

import com.mtsahakis.mediaprojectiondemo.storagemanagers.ProjectionIntentSaver;

public class ScreenCaptureServiceManager {

    static Intent projectionIntent;

    static void launchScreenCaptureService(Context context) {
        if (projectionIntent == null)
            setProjectionIntent(context);

        Log.i("kalzak", "starting screen capture");
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        //startScreenCaptureService();
    }

    private static void setProjectionIntent(Context context) {
        Intent storedIntent = getProjectionIntentFromStorage(context);
        if (storedIntent == null) {
            projectionIntent = createNewProjectionIntent(context);
            putProjectionIntentIntoStorage(context, projectionIntent);
        } else
            projectionIntent = storedIntent;
    }

    private static Intent createNewProjectionIntent(Context context) {

        return null;
    }

    private static void putProjectionIntentIntoStorage(Context context, Intent projectionIntent) {
    }

    private static Intent getProjectionIntentFromStorage(Context context) {
        if (projectionIntent == null) {
            ProjectionIntentSaver projectionIntentSaver = new ProjectionIntentSaver(context);
            return projectionIntentSaver.getIntent();
        }
        return null;
    }


    private void startScreenCapture() {
        Log.i("kalzak", "starting screen capture");
        //MediaProjectionManager mProjectionManager =
          //      (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        startScreenCaptureService();
    }

    private void startScreenCaptureService() {
        //Intent projectionIntent = (Intent) savedProjectionIntent.clone();
        //saveIntent(projectionIntent);
        //Intent projectionIntent = getIntent();
        //startService(ScreenCaptureService.getStartIntent(this, Activity.RESULT_OK, projectionIntent));
    }
}
