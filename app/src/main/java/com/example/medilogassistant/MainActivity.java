package com.example.medilogassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.widget.Button;
import android.content.Context;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        findViewById(R.id.btn_vitals).setOnClickListener(v -> startActivity(new Intent(this, VitalsActivity.class)));
        findViewById(R.id.btn_vitals_log).setOnClickListener(v -> startActivity(new Intent(this, VitalsLogActivity.class)));
        findViewById(R.id.btn_prescriptions).setOnClickListener(v -> startActivity(new Intent(this, PrescriptionActivity.class)));
        findViewById(R.id.btn_prescription_book).setOnClickListener(v -> startActivity(new Intent(this, PrescriptionBookActivity.class)));

        Button btnTestNotification = findViewById(R.id.btn_test_notification);
        btnTestNotification.setOnClickListener(v -> {
            // 1. Create the notification channel (safe to call repeatedly)
            String channelId = "med_channel";
            String channelName = "Medication Reminders";
            String channelDesc = "Channel for medication reminder notifications";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription(channelDesc);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }

            // 2. Build and show the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Test Notification")
                    .setContentText("If you see this, notifications are working!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1001, builder.build());
        });

    }
}
