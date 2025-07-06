package com.example.medilogassistant;

import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;


public class PrescriptionActivity extends AppCompatActivity {
    private EditText etMedName, etDosage;
    private TimePicker timePicker;
    private Button btnSave;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        dbHelper = new DBHelper(this);
        etMedName = findViewById(R.id.et_med_name);
        etDosage = findViewById(R.id.et_dosage);
        timePicker = findViewById(R.id.time_picker);
        btnSave = findViewById(R.id.btn_save);

        timePicker.setIs24HourView(true);

        btnSave.setOnClickListener(v -> {
            String medName = etMedName.getText().toString().trim();
            String dosage = etDosage.getText().toString().trim();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (medName.isEmpty() || dosage.isEmpty()) {
                Toast.makeText(this, "Please enter medicine name and dosage.", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("med_name", medName);
            cv.put("dosage", dosage);
            cv.put("hour", hour);
            cv.put("minute", minute);
            long prescId = db.insert("prescriptions", null, cv);
            db.close();

            scheduleReminder((int) prescId, hour, minute, medName);

            Toast.makeText(this, "Prescription saved and reminder set!", Toast.LENGTH_SHORT).show();
            etMedName.setText("");
            etDosage.setText("");
        });
    }


    private void scheduleReminder(int prescId, int hour, int minute, String medName) {
        // Calculate delay until next scheduled time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long triggerTime = calendar.getTimeInMillis();
        if (triggerTime < System.currentTimeMillis()) {
            triggerTime += 24 * 60 * 60 * 1000; // add a day if time has passed
        }
        long delay = triggerTime - System.currentTimeMillis();

        Data data = new Data.Builder()
                .putString("med_name", medName)
                .putInt("presc_id", prescId)
                .build();

        OneTimeWorkRequest notificationWork =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();

        WorkManager.getInstance(this).enqueue(notificationWork);
    }

}
