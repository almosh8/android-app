package com.mtsahakis.mediaprojectiondemo.datahandlers;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DataHandler {

  Bitmap screenshot;
  String appName;
  LocalDateTime now;

  public DataHandler(Bitmap screenshot, String appName) {
    this.screenshot = screenshot;
    this.appName = appName;
    now = LocalDateTime.now();
  }

  public void handleData() {
    Log.i("kalzak", now.toString() + ' ' + this.appName);
  }
}
