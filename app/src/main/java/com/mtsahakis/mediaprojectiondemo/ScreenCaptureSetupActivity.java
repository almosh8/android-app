package com.mtsahakis.mediaprojectiondemo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.mtsahakis.mediaprojectiondemo.services.ScreenCaptureService;
import com.mtsahakis.mediaprojectiondemo.storagemanagers.ProjectionIntentSaver;


public class ScreenCaptureSetupActivity extends Activity {

    private static final int REQUEST_CODE = 100;
    private static final String SAVED_INTENT_KEY = "Saved intent";

    ProjectionIntentSaver projectionIntentSaver;


    /****************************************** Activity Lifecycle methods ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectionIntentSaver = new ProjectionIntentSaver(this);
        projectionIntentSaver.deleteIntent();

        if (!hasUsageStatsPermission(this)) {
            Log.i("kalzak", "launching permission intent");
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        setupbroadcastReceivers();
        setupScreenCapture();
    }

    private void setupbroadcastReceivers() {
    }

    private boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void setupScreenCapture() {

        if (needProjectionPermission())
            askForProjectionPermission();
        else {
            startScreenCaptureService();
            finish();
        }
    }

    private boolean needProjectionPermission() {
        return !projectionIntentSaver.intentIsStored();
    }

    private void askForProjectionPermission() {
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("kalzak", "onActivityResult permission granted");
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                projectionIntentSaver.saveIntent(data);
                startScreenCaptureService();
                finish();
            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/


    private void startScreenCaptureService() {
        //Intent projectionIntent = (Intent) savedProjectionIntent.clone();
        //saveIntent(projectionIntent);
        ProjectionIntentSaver projectionIntentSaver = new ProjectionIntentSaver(this);
        Intent projectionIntent = projectionIntentSaver.getIntent();
        //startService(ScreenCaptureService.getStopIntent(this));
        startService(ScreenCaptureService.getStartIntent(this, Activity.RESULT_OK, projectionIntent));
    }


}