package com.example.simplealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class AlarmListActivity extends AppCompatActivity {

    private ListView listView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "alarms_prefs";
    private static final String ALARMS_KEY = "alarms";
    private ArrayList<String> alarmList;
    private Button btnBackToMain;  // Yeni buton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        listView = findViewById(R.id.listViewAlarms);
        btnBackToMain = findViewById(R.id.btnBackToMain);  // Butonu bağla
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadAlarms();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, alarmList);
        listView.setAdapter(adapter);

        // Uzun tıklama ile alarm silme
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedAlarm = alarmList.get(position);
            removeAlarm(selectedAlarm);
            alarmList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(AlarmListActivity.this, "Alarm silindi", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Ana ekrana dön butonu tıklama
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmListActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadAlarms() {
        Set<String> alarmSet = sharedPreferences.getStringSet(ALARMS_KEY, new HashSet<>());
        alarmList = new ArrayList<>();
        for (String alarm : alarmSet) {
            alarmList.add(formatAlarm(alarm));
        }
    }

    private String formatAlarm(String alarmStr) {
        String[] parts = alarmStr.split("-");
        int day = Integer.parseInt(parts[0]);
        int hour = Integer.parseInt(parts[1]);
        int minute = Integer.parseInt(parts[2]);

        String dayName = getDayName(day);
        return dayName + " - " + String.format("%02d:%02d", hour, minute);
    }

    private String getDayName(int day) {
        switch (day) {
            case Calendar.SUNDAY: return "Pazar";
            case Calendar.MONDAY: return "Pazartesi";
            case Calendar.TUESDAY: return "Salı";
            case Calendar.WEDNESDAY: return "Çarşamba";
            case Calendar.THURSDAY: return "Perşembe";
            case Calendar.FRIDAY: return "Cuma";
            case Calendar.SATURDAY: return "Cumartesi";
            default: return "Bilinmiyor";
        }
    }

    private void removeAlarm(String alarmToRemoveFormatted) {
        String[] parts = alarmToRemoveFormatted.split(" - ");
        String dayName = parts[0];
        String time = parts[1];
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        int day = getDayFromName(dayName);
        String alarmStr = day + "-" + hour + "-" + minute;

        Set<String> alarmSet = sharedPreferences.getStringSet(ALARMS_KEY, new HashSet<>());
        Set<String> newSet = new HashSet<>(alarmSet);
        if (newSet.remove(alarmStr)) {
            sharedPreferences.edit().putStringSet(ALARMS_KEY, newSet).apply();
        }

        cancelAlarm(day);
    }

    private int getDayFromName(String dayName) {
        switch (dayName) {
            case "Pazar": return Calendar.SUNDAY;
            case "Pazartesi": return Calendar.MONDAY;
            case "Salı": return Calendar.TUESDAY;
            case "Çarşamba": return Calendar.WEDNESDAY;
            case "Perşembe": return Calendar.THURSDAY;
            case "Cuma": return Calendar.FRIDAY;
            case "Cumartesi": return Calendar.SATURDAY;
            default: return -1;
        }
    }

    private void cancelAlarm(int dayOfWeek) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmListActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmListActivity.this, dayOfWeek, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
