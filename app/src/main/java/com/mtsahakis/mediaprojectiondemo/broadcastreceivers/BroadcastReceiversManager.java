package com.mtsahakis.mediaprojectiondemo.broadcastreceivers;

import android.content.Context;
import android.content.IntentFilter;

public class BroadcastReceiversManager {

    public static void registerProjectionBroadcastReceivers(Context context) {
        registerDeviceUnlockedBroadcastReceiver(context);
        registerDeviceLockedBroadcastReceiver(context);

    }

    private static void registerDeviceUnlockedBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");

        DeviceUnlockedBroadcastReceiver deviceUnlockedBroadcastReceiver = new DeviceUnlockedBroadcastReceiver();
        context.registerReceiver(deviceUnlockedBroadcastReceiver, intentFilter);
    }

    private static void registerDeviceLockedBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");

        DeviceLockedBroadcastReceiver deviceLockedBroadcastReceiver = new DeviceLockedBroadcastReceiver();
        context.registerReceiver(deviceLockedBroadcastReceiver, intentFilter);
    }
}
