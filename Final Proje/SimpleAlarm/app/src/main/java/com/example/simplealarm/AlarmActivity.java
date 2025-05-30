package com.example.simplealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Yalnızca ses dosyanız çalacak
        mediaPlayer = MediaPlayer.create(this, R.raw.wakeup_audio);  // Raw klasöründeki wakeup_audio.mp3 dosyasını kullanıyoruz
        mediaPlayer.setLooping(true);  // Alarm sürekli çalsın diye döngüye alıyoruz
        mediaPlayer.start();  // Alarmı başlatıyoruz

        Button btnStop = findViewById(R.id.btnKapat);
        Button btnSnooze = findViewById(R.id.btnErtele);

        // Alarmı kapat butonu
        btnStop.setOnClickListener(v -> {
            stopAlarm();  // Ses kaynağını durduruyoruz
            finishApp();  // Uygulamayı sonlandırıyoruz
        });

        // Alarmı ertele butonu
        btnSnooze.setOnClickListener(v -> {
            stopAlarm();  // Alarm sesini durduruyoruz
            snoozeAlarm(); // Alarmı 5 dakika erteledik
            finishApp();  // Uygulamayı sonlandırıyoruz
        });
    }

    // Alarm sesini durdurma işlemi
    private void stopAlarm() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();  // Alarm sesini durduruyoruz
            }
            mediaPlayer.release();  // Ses kaynağını serbest bırakıyoruz
            mediaPlayer = null;  // MediaPlayer nesnesini null yapıyoruz
        }
    }

    // Alarmı 5 dakika erteleme işlemi
    private void snoozeAlarm() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);  // 5 dakika ekliyoruz

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 9999, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);  // Alarmı 5 dakika sonra kuruyoruz
    }

    // Uygulamayı sonlandırma (son kullanılanlar listesinden kaldırma)
    private void finishApp() {
        stopAlarm();  // Alarm sesini durduruyoruz
        System.exit(0);  // Uygulamayı sonlandırıyoruz
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Uygulama arka plana alındığında ses kaynağını serbest bırakıyoruz
        stopAlarm();  // Ses kaynağını durduruyoruz
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Uygulama kapandığında medya kaynağını serbest bırakıyoruz
        stopAlarm();  // Son olarak ses kaynağını serbest bırakıyoruz
    }
}
