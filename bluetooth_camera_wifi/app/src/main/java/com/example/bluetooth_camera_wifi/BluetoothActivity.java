package com.example.bluetooth_camera_wifi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Button btnOpen = findViewById(R.id.btnEnableBluetooth);
        Button btnClose = findViewById(R.id.btnDisableBluetooth);
        Button btnList = findViewById(R.id.btnListDevices);
        Button btnVisible = findViewById(R.id.btnMakeVisible);
        Button btnBack = findViewById(R.id.btnBackToMainBluetooth);

        listView = findViewById(R.id.listViewBluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Bluetooth Aç
        btnOpen.setOnClickListener(v -> {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
                Toast.makeText(this, "Bluetooth açılıyor...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth zaten açık.", Toast.LENGTH_SHORT).show();
            }
        });

        // Bluetooth Kapat
        btnClose.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                Toast.makeText(this, "Bluetooth kapatıldı.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth zaten kapalı.", Toast.LENGTH_SHORT).show();
            }
        });

        // Listele
        btnList.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                ArrayList<String> devices = new ArrayList<>();

                for (BluetoothDevice device : pairedDevices) {
                    devices.add(device.getName() + " - " + device.getAddress());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devices);
                listView.setAdapter(adapter);

                Toast.makeText(this, "Cihazlar listelendi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lütfen önce Bluetooth'u açın.", Toast.LENGTH_SHORT).show();
            }
        });

        // Görünür ol
        btnVisible.setOnClickListener(v -> {
            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                startActivity(discoverableIntent);
            }
        });

        // Ana sayfaya dön
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(BluetoothActivity.this, MainActivity.class));
            finish();
        });
    }
}
