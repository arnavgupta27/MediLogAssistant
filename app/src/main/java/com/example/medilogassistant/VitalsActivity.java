package com.example.medilogassistant;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VitalsActivity extends AppCompatActivity {
    private EditText etType, etValue, etNotes;
    private Button btnSave;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        dbHelper = new DBHelper(this);
        etType = findViewById(R.id.et_type);
        etValue = findViewById(R.id.et_value);
        etNotes = findViewById(R.id.et_notes);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> {
            String type = etType.getText().toString().trim();
            String value = etValue.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            if (type.isEmpty() || value.isEmpty()) {
                Toast.makeText(this, "Please enter both type and value.", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("date", date);
            cv.put("type", type);
            cv.put("value", value);
            cv.put("notes", notes);
            db.insert("vitals", null, cv);
            db.close();

            Toast.makeText(this, "Vital logged!", Toast.LENGTH_SHORT).show();
            etType.setText("");
            etValue.setText("");
            etNotes.setText("");
        });
    }
}
