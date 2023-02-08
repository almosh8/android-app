package com.mtsahakis.mediaprojectiondemo.storagemanagers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ProjectionIntentSaver {

    private final String PREFS_NAME = "projection intent prefs";
    private final String SAVED_INTENT_KEY = "Saved intent";

    private SharedPreferences sharedPreferences;

    static Intent projectionIntent;

    public ProjectionIntentSaver(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void saveIntent(Object intent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(intent);
        editor.putString(SAVED_INTENT_KEY, json);
        editor.apply();
    }

    public Intent getIntent() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SAVED_INTENT_KEY, null);
        return gson.fromJson(json, Intent.class);
    }

}