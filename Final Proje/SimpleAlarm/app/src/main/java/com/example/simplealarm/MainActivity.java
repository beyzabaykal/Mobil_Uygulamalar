package com.example.simplealarm;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnTimer, btnAlarmList;
    private int jam, menit;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "alarms_prefs";
    private static final String ALARMS_KEY = "alarms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CheckBox'ları tanımla
        CheckBox mon = findViewById(R.id.mon);
        CheckBox tue = findViewById(R.id.tue);
        CheckBox wed = findViewById(R.id.wed);
        CheckBox thu = findViewById(R.id.thu);
        CheckBox fri = findViewById(R.id.fri);
        CheckBox sat = findViewById(R.id.sat);
        CheckBox sun = findViewById(R.id.sun);

        timePicker = findViewById(R.id.timePicker);
        btnTimer = findViewById(R.id.btnTimer);
        btnAlarmList = findViewById(R.id.btnAlarmList);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            jam = hourOfDay;
            menit = minute;
        });

        // Bildirim izni kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        btnTimer.setOnClickListener(v -> {
            ArrayList<Integer> selectedDays = new ArrayList<>();

            Calendar today = Calendar.getInstance();
            int todayDayOfWeek = today.get(Calendar.DAY_OF_WEEK);  // Bugünün haftanın kaçıncı günü olduğunu alır

            if (mon.isChecked()) selectedDays.add(Calendar.MONDAY);
            if (tue.isChecked()) selectedDays.add(Calendar.TUESDAY);
            if (wed.isChecked()) selectedDays.add(Calendar.WEDNESDAY);
            if (thu.isChecked()) selectedDays.add(Calendar.THURSDAY);
            if (fri.isChecked()) selectedDays.add(Calendar.FRIDAY);
            if (sat.isChecked()) selectedDays.add(Calendar.SATURDAY);
            if (sun.isChecked()) selectedDays.add(Calendar.SUNDAY);

            if (selectedDays.isEmpty()) {
                Toast.makeText(MainActivity.this, "Lütfen en az bir gün seçin!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int day : selectedDays) {
                setAlarmForDay(day, todayDayOfWeek);
                saveAlarm(day, jam, menit);
            }

            Toast.makeText(MainActivity.this, "Alarm Ayarlandı: " + jam + ":" + String.format("%02d", menit), Toast.LENGTH_SHORT).show();
            createNotificationChannel(); // Bildirim kanalı
        });

        btnAlarmList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmListActivity.class);
            startActivity(intent);
        });

        Button btnKronometre = findViewById(R.id.btnKronometre);
        btnKronometre.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StopwatchActivity.class);
            startActivity(intent);
        });
    }

    // Bildirim kanalını
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Reminders";
            String description = "HEY, ALARM!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Haftalık alarm (gün seçimine göre)
    private void setAlarmForDay(int dayOfWeek, int todayDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY, jam);
        cal.set(Calendar.MINUTE, menit);
        cal.set(Calendar.SECOND, 0);

        Calendar now = Calendar.getInstance();

        // Eğer bugünün günü seçilen günse ve saat geçmişse -> bir sonraki haftaya kur
        if (dayOfWeek == todayDayOfWeek) {
            Calendar temp = (Calendar) now.clone();
            temp.set(Calendar.HOUR_OF_DAY, jam);
            temp.set(Calendar.MINUTE, menit);
            temp.set(Calendar.SECOND, 0);

            if (temp.before(now)) {
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
        } else if (cal.before(now)) {
            // Bugünden önceki bir gün seçildiyse -> bir sonraki haftaya kur
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Alarmı ayarla
        Intent i = new Intent(MainActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, dayOfWeek, i, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    // Alarmı SharedPreferences'a kayıt et
    private void saveAlarm(int dayOfWeek, int hour, int minute) {
        // Alarm bilgisi formatı: "day-hour-minute", örn: 2-7-30 (Pazartesi 07:30)
        String alarmStr = dayOfWeek + "-" + hour + "-" + minute;

        Set<String> alarmSet = sharedPreferences.getStringSet(ALARMS_KEY, new HashSet<>());
        Set<String> newSet = new HashSet<>(alarmSet);
        newSet.add(alarmStr);

        sharedPreferences.edit().putStringSet(ALARMS_KEY, newSet).apply();
    }
}
