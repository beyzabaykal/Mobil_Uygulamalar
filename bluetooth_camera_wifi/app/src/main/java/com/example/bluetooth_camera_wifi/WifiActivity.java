package com.example.bluetooth_camera_wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WifiActivity extends AppCompatActivity {

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Button btnWifiOn = findViewById(R.id.btnWifiOn);
        Button btnWifiOff = findViewById(R.id.btnWifiOff);
        Button btnBack = findViewById(R.id.btnBackToMainWifi);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Wi-Fi Aç
        btnWifiOn.setOnClickListener(v -> {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
                Toast.makeText(this, "Wi-Fi açıldı", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wi-Fi zaten açık", Toast.LENGTH_SHORT).show();
            }
        });

        // Wi-Fi Kapat
        btnWifiOff.setOnClickListener(v -> {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                Toast.makeText(this, "Wi-Fi kapatıldı", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wi-Fi zaten kapalı", Toast.LENGTH_SHORT).show();
            }
        });

        // Ana Sayfaya Dön
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(WifiActivity.this, MainActivity.class));
            finish();
        });
    }
}
