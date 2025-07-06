package com.example.medilogassistant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PrescriptionBookActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView prescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_book);

        dbHelper = new DBHelper(this);
        prescriptionList = findViewById(R.id.prescription_list);

        ArrayList<String> prescriptions = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT med_name, dosage, hour, minute FROM prescriptions ORDER BY id DESC", null);
            if (cursor.getCount() == 0) {
                prescriptions.add("No prescriptions found.");
            } else {
                while (cursor.moveToNext()) {
                    String medName = cursor.getString(0);
                    String dosage = cursor.getString(1);
                    int hour = cursor.getInt(2);
                    int minute = cursor.getInt(3);
                    String time = String.format("%02d:%02d", hour, minute);
                    prescriptions.add("Medicine: " + medName + "\nDosage: " + dosage + "\nTime: " + time);
                }
            }
        } catch (Exception e) {
            prescriptions.add("Error loading prescriptions: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        // Use the custom adapter
        PrescriptionAdapter adapter = new PrescriptionAdapter(this, prescriptions);
        prescriptionList.setAdapter(adapter);
    }
}
