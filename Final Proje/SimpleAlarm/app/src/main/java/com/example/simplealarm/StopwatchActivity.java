package com.example.simplealarm;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StopwatchActivity extends AppCompatActivity {

    private TextView timeDisplay;
    private Button startBtn, stopBtn, resetBtn, continueBtn, backBtn;
    private Handler handler = new Handler();
    private long startTime = 0L, timeInMillis = 0L;
    private boolean isRunning = false;

    private final Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds %= 60;
            minutes %= 60;

            String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timeDisplay.setText(time);

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        timeDisplay = findViewById(R.id.txtChrono);
        startBtn = findViewById(R.id.btnStart);
        stopBtn = findViewById(R.id.btnStop);
        resetBtn = findViewById(R.id.btnReset);
        continueBtn = findViewById(R.id.btnContinue);
        backBtn = findViewById(R.id.btnBack);

        startBtn.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                handler.post(updateTime);
                isRunning = true;
            }
        });

        stopBtn.setOnClickListener(v -> {
            if (isRunning) {
                handler.removeCallbacks(updateTime);
                timeInMillis = System.currentTimeMillis() - startTime; // Şu anki zamanı kaydet
                isRunning = false;
                continueBtn.setEnabled(true); // "Devam Et" butonunu aktif et
            }
        });

        resetBtn.setOnClickListener(v -> {
            handler.removeCallbacks(updateTime);
            timeDisplay.setText("00:00:00");
            timeInMillis = 0L;
            isRunning = false;
            continueBtn.setEnabled(false); // "Devam Et" butonunu devre dışı bırak
        });

        continueBtn.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis() - timeInMillis; // Kaldığı yerden devam etsin
                handler.post(updateTime);
                isRunning = true;
                continueBtn.setEnabled(false); // "Devam Et" butonunu devre dışı bırak
            }
        });

        backBtn.setOnClickListener(v -> finish());
    }
}
