package com.example.medilogassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "healthcare.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE vitals (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, type TEXT, value TEXT, notes TEXT)");
        db.execSQL("CREATE TABLE prescriptions (id INTEGER PRIMARY KEY AUTOINCREMENT, med_name TEXT, dosage TEXT, hour INTEGER, minute INTEGER)");
        db.execSQL("CREATE TABLE alerts (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, timestamp TEXT, dismissed INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS vitals");
        db.execSQL("DROP TABLE IF EXISTS prescriptions");
        db.execSQL("DROP TABLE IF EXISTS alerts");
        onCreate(db);
    }
}
