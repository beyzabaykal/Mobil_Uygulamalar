package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {

    EditText etAd, etSoyad, etYas, etSehir;
    Button btnEkle, btnGoster, btnSil, btnGuncelle;
    veritabani db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAd = findViewById(R.id.etAd);
        etSoyad = findViewById(R.id.etSoyad);
        etYas = findViewById(R.id.etYas);
        etSehir = findViewById(R.id.etSehir);

        btnEkle = findViewById(R.id.btnEkle);
        btnGoster = findViewById(R.id.btnGoster);
        btnSil = findViewById(R.id.btnSil);
        btnGuncelle = findViewById(R.id.btnGuncelle);

        db = new veritabani(this);

        btnEkle.setOnClickListener(v -> {
            String ad = etAd.getText().toString();
            String soyad = etSoyad.getText().toString();
            int yas = Integer.parseInt(etYas.getText().toString());
            String sehir = etSehir.getText().toString();

            db.kayitEkle(ad, soyad, yas, sehir);
            Toast.makeText(this, "Kayıt Eklendi", Toast.LENGTH_SHORT).show();
        });

        btnGoster.setOnClickListener(v -> {
            Cursor cursor = db.kayitlariGetir();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Kayıt yok", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder sb = new StringBuilder();
            while (cursor.moveToNext()) {
                sb.append("Ad: ").append(cursor.getString(1))
                        .append("\nSoyad: ").append(cursor.getString(2))
                        .append("\nYaş: ").append(cursor.getInt(3))
                        .append("\nŞehir: ").append(cursor.getString(4)).append("\n\n");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Kayıtlar");
            builder.setMessage(sb.toString());
            builder.setPositiveButton("Tamam", null);
            builder.show();
        });

        btnSil.setOnClickListener(v -> {
            String ad = etAd.getText().toString();
            db.kayitSil(ad);
            Toast.makeText(this, "Kayıt Silindi", Toast.LENGTH_SHORT).show();
        });

        btnGuncelle.setOnClickListener(v -> {
            String ad = etAd.getText().toString();
            String yeniSoyad = etSoyad.getText().toString();
            int yeniYas = Integer.parseInt(etYas.getText().toString());
            String yeniSehir = etSehir.getText().toString();

            db.kayitGuncelle(ad, yeniSoyad, yeniYas, yeniSehir);
            Toast.makeText(this, "Kayıt Güncellendi", Toast.LENGTH_SHORT).show();
        });
    }
}
