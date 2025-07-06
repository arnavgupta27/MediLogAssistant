package com.example.medilogassistant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class VitalsLogActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView vitalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals_log);

        dbHelper = new DBHelper(this);
        vitalsList = findViewById(R.id.vitals_list);

        ArrayList<String> vitals = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, type, value, notes FROM vitals ORDER BY date DESC", null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            String type = cursor.getString(1);
            String value = cursor.getString(2);
            String notes = cursor.getString(3);
            vitals.add(date + " | " + type + ": " + value + (notes.isEmpty() ? "" : " (" + notes + ")"));
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vitals);
        vitalsList.setAdapter(adapter);
    }
}
