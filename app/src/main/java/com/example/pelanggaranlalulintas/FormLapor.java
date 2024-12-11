package com.example.pelanggaranlalulintas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FormLapor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_lapor);

        Button btnLaporkan = findViewById(R.id.button_next);
        EditText editNama = findViewById(R.id.idnama);
        EditText editNomorTelepon = findViewById(R.id.idtelp);
        EditText editAlamat = findViewById(R.id.idalamat);
        EditText editKota = findViewById(R.id.idkota);
        EditText editTanggal = findViewById(R.id.tanggal_pelaporan);
        CheckBox checkBoxKeterangan = findViewById(R.id.keterangan);

        btnLaporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input
                if (editNama.getText().toString().isEmpty()) {
                    editNama.setError("Nama harus diisi");
                    return;
                }
                if (editNomorTelepon.getText().toString().isEmpty()) {
                    editNomorTelepon.setError("Nomor telepon harus diisi");
                    return;
                }
                if (editAlamat.getText().toString().isEmpty()) {
                    editAlamat.setError("Alamat harus diisi");
                    return;
                }
                if (editKota.getText().toString().isEmpty()) {
                    editKota.setError("Kota harus diisi");
                    return;
                }
                if (!checkBoxKeterangan.isChecked()) {
                    Toast.makeText(FormLapor.this, "Harap centang pernyataan di atas", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Jika semua validasi lulus, lanjut ke halaman Berhasil
                Intent intent = new Intent(FormLapor.this, Berhasil.class);
                startActivity(intent);
            }
        });
    }
}
