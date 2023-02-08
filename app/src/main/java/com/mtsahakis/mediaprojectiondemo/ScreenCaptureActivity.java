package com.mtsahakis.mediaprojectiondemo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.mtsahakis.mediaprojectiondemo.broadcastreceivers.BroadcastReceiversManager;
import com.mtsahakis.mediaprojectiondemo.services.ScreenCaptureService;
import com.mtsahakis.mediaprojectiondemo.storagemanagers.ProjectionIntentSaver;


public class ScreenCaptureActivity extends Activity {

    private static final int REQUEST_CODE = 100;
    private static final String SAVED_INTENT_KEY = "Saved intent";

    /****************************************** Activity Lifecycle methods ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!hasUsageStatsPermission(this)) {
            Log.i("kalzak", "launching permission intent");
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        try {
            startScreenCapture();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        BroadcastReceiversManager.registerProjectionBroadcastReceivers(this);
    }

    private boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void startScreenCapture() {
        Log.i("kalzak", "starting screen capture");
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        startScreenCaptureService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("kalzak", "onActivityResult resultCode = " + resultCode);
        ProjectionIntentSaver projectionIntentSaver = new ProjectionIntentSaver(this);
        projectionIntentSaver.saveIntent(data);
        //savedProjectionIntent = projectionIntentSaver.getIntent();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startScreenCaptureService();
            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/


    private void startScreenCaptureService() {
        //Intent projectionIntent = (Intent) savedProjectionIntent.clone();
        //saveIntent(projectionIntent);
        Intent projectionIntent = getIntent();
        startService(ScreenCaptureService.getStartIntent(this, Activity.RESULT_OK, projectionIntent));
    }



    private Object mgetIntent() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SAVED_INTENT_KEY, null);
        return gson.fromJson(json, Intent.class);
    }

    private void stopProjection() {
        startService(ScreenCaptureService.getStopIntent(this));
    }

}