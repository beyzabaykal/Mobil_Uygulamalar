package com.example.bluetooth_camera_wifi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Kamera izni kontrolü
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        Button btnPhoto = findViewById(R.id.btnTakePhoto);
        Button btnVideo = findViewById(R.id.btnRecordVideo);
        Button btnBack = findViewById(R.id.btnBackToMain);

        btnPhoto.setOnClickListener(v -> {
            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (photoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnVideo.setOnClickListener(v -> {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (videoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE);
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(CameraActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri mediaUri = data.getData();
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Toast.makeText(this, "Fotoğraf çekildi: " + mediaUri, Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Toast.makeText(this, "Video kaydedildi: " + mediaUri, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "İşlem iptal edildi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kamera izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
