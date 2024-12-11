package com.example.pelanggaranlalulintas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HasilFoto extends AppCompatActivity {
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil_foto);

        // Inisialisasi ImageView
        photo = findViewById(R.id.foto);

        // Ambil Uri gambar dari Intent
        Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("imageUri");

        if (imageUri != null) {
            // Tampilkan gambar di ImageView
            photo.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Tombol untuk melaporkan
        Button btnLaporkan = findViewById(R.id.button);
        btnLaporkan.setOnClickListener(v -> {
            // Pindahkan ke halaman FormLapor
            Intent formLaporIntent = new Intent(HasilFoto.this, FormLapor.class);
            startActivity(formLaporIntent);
        });
    }
}
