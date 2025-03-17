package com.example.plakaoluturma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CityDetailActivity extends AppCompatActivity {

    private TextView txtCityName, txtPlateNumber;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        txtCityName = findViewById(R.id.txtCityName);
        txtPlateNumber = findViewById(R.id.txtPlateNumber);
        btnBack = findViewById(R.id.btnBack);

        // Gelen intent ile verileri al
        Intent intent = getIntent();
        if (intent != null) {
            String cityName = intent.getStringExtra("cityName");
            int plateNumber = intent.getIntExtra("plateNumber", 0);

            txtCityName.setText("Şehir: " + cityName);
            txtPlateNumber.setText("Plaka Kodu: " + plateNumber);
        }

        // Butona tıklanınca ana sayfaya dön
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(CityDetailActivity.this, MainActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });
    }
}