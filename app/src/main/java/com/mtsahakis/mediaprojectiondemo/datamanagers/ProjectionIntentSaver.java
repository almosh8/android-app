package com.mtsahakis.mediaprojectiondemo.datamanagers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.util.Base64;

public class ProjectionIntentSaver extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "intents.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "intents";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INTENT = "intent";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_INTENT + " TEXT" + ")";

    public ProjectionIntentSaver(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }






    public void saveIntent(Intent intent) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put(COLUMN_INTENT, intentToString(intent));

        db.delete(TABLE_NAME, null, null);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    private String intentToString(Intent intent) {
        Parcel parcel = Parcel.obtain();
        intent.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        byte[] mar = parcel.marshall();
        String string = Base64.encodeToString(parcel.marshall(), Base64.DEFAULT);

        parcel.recycle();
        return string;
    }

    public Intent getIntent() {
        Intent intent = null;
        String selectQuery = "SELECT " + COLUMN_INTENT + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            String intentString = cursor.getString(0);
            intent = stringToIntent(intentString);
        }
        cursor.close();
        db.close();

        return intent;
    }

    private Intent stringToIntent(String string) {
        byte[] data = Base64.decode(string, Base64.DEFAULT);
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(data, 0, string.length());
        parcel.setDataPosition(0);
        Intent intent = Intent.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return intent;
    }

}