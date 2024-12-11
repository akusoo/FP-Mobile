package com.example.pelanggaranlalulintas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mulai_awal);

        Button buttonNext = findViewById(R.id.button_next);
        buttonNext.setOnClickListener(v -> checkCameraPermission());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Kamera tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                handleCapturedImage(data); // Process the captured image
            } else {
                Toast.makeText(this, "Tidak ada data gambar yang diterima", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleCapturedImage(Intent data) {
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

        if (imageBitmap != null) {
            File imageFile = new File(getExternalFilesDir(null), "captured_image.jpg");
            try (FileOutputStream out = new FileOutputStream(imageFile)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Uri imageUri = Uri.fromFile(imageFile);

                // Send image to the server using InferenceLocal
                new Thread(() -> {
                    try {
                        InferenceLocal inferenceLocal = new InferenceLocal();
                        String response = inferenceLocal.sendImageToServer(imageFile);

                        // Update UI on the main thread
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();

                            // Pass the URI to the next activity
                            Intent intent = new Intent(MainActivity.this, HasilFoto.class);
                            intent.putExtra("imageUri", imageUri.toString());
                            startActivity(intent);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error menyimpan gambar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Gagal menangkap gambar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk menggunakan fitur ini", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
