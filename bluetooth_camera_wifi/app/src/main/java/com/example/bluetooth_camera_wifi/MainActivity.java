package com.example.bluetooth_camera_wifi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Butonları tanımla
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnBluetooth = findViewById(R.id.btnBluetooth);
        Button btnWifi = findViewById(R.id.btnWifi);

        // Lambda ifadeleri ile tıklama olayları
        btnCamera.setOnClickListener(v -> startActivity(new Intent(this, CameraActivity.class)));
        btnBluetooth.setOnClickListener(v -> startActivity(new Intent(this, BluetoothActivity.class)));
        btnWifi.setOnClickListener(v -> startActivity(new Intent(this, WifiActivity.class)));
    }
}
