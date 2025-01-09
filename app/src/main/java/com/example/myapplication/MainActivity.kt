package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binfig: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binfig = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binfig.root)

        // Inisialisasi tombol
        val btnCekRasKucing: Button = findViewById(R.id.btnCekRasKucing)
        val btnPanduanPenggunaan: Button = findViewById(R.id.btnPanduanPenggunaan)
        val btnKeluar: Button = findViewById(R.id.btnKeluar)

        Glide.with(this) // Context
            .load(R.drawable.gambar_kucing) // Gambar yang ingin ditampilkan
            .circleCrop() // Transformasi lingkaran
            .into(binfig.circularImageView) // Circ        // Aksi tombol "Cek Ras Kucing"

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
