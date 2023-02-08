package com.mtsahakis.mediaprojectiondemo;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mtsahakis.mediaprojectiondemo.services.ScreenCaptureService;


public class ScreenCaptureActivity extends Activity {

    private static final int REQUEST_CODE = 100;

    private static Intent savedProjectionIntent = null;

    /****************************************** Activity Lifecycle methods ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start projection
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startScreenCapture();
            }
        });

        // stop projection
        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopProjection();
            }
        });

        if (!hasUsageStatsPermission(this)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

    }

    private boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("kalzak", "onActivityResult resultCode = " + resultCode);
        savedProjectionIntent = data;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startScreenCaptureService();
            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/
    private void startScreenCapture() {
        Log.i("kalzak", "first");
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Log.i("kalzak", "second");
        if(savedProjectionIntent == null)
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        else {
            startScreenCaptureService();
        }
        Log.i("kalzak", "third");
    }

    private void startScreenCaptureService() {
        Intent projectionIntent = (Intent) savedProjectionIntent.clone();
        startService(ScreenCaptureService.getStartIntent(this, Activity.RESULT_OK, projectionIntent));
    }

    private void stopProjection() {
        startService(ScreenCaptureService.getStopIntent(this));
    }

}