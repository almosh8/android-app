package com.mtsahakis.mediaprojectiondemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


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
                startProjection();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("kalzak", "onActivityResult resultCode = " + resultCode);
        savedProjectionIntent = data;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startProjectionIntent();
            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/
    private void startProjection() {
        Log.i("kalzak", "first");
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Log.i("kalzak", "second");
        if(savedProjectionIntent == null)
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        else {
            startProjectionIntent();
        }
        Log.i("kalzak", "third");
    }

    private void startProjectionIntent() {
        Intent projectionIntent = (Intent) savedProjectionIntent.clone();
        startService(com.mtsahakis.mediaprojectiondemo.ScreenCaptureService.getStartIntent(this, Activity.RESULT_OK, projectionIntent));
    }

    private void stopProjection() {
        startService(com.mtsahakis.mediaprojectiondemo.ScreenCaptureService.getStopIntent(this));
    }

}