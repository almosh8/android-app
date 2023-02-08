package com.mtsahakis.mediaprojectiondemo.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenCaptureServiceExecutor extends Service {
  private KeyguardManager mKeyguardManager;
  private BroadcastReceiver mReceiver;

  @Override
  public void onCreate() {
    super.onCreate();
    mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
          if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            stopSelf();
          }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
          if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
            //startService(new Intent(LockService.this, LockService.class));
          }
        }
      }
    };
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    filter.addAction(Intent.ACTION_SCREEN_ON);
    registerReceiver(mReceiver, filter);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mReceiver);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    // Your service code here
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
