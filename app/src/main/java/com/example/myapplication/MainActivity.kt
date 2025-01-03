package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi tombol
        val btnCekRasKucing: Button = findViewById(R.id.btnCekRasKucing)
        val btnPanduanPenggunaan: Button = findViewById(R.id.btnPanduanPenggunaan)
        val btnKeluar: Button = findViewById(R.id.btnKeluar)

        // Aksi tombol "Cek Ras Kucing"
        btnCekRasKucing.setOnClickListener {
            val intent = Intent(this, CheckBreedActivity::class.java)
            startActivity(intent)
        }

        // Aksi tombol "Panduan Penggunaan"
        btnPanduanPenggunaan.setOnClickListener {
            val intent = Intent(this, PanduanActivity::class.java)
            startActivity(intent)
        }
        // Aksi tombol "Keluar"
        btnKeluar.setOnClickListener {
            finish()
        }
    }
}
