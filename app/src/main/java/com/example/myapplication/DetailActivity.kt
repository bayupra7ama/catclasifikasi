package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val catName = intent.getStringExtra("CAT_NAME") ?: "Unknown"
        val careInfo = intent.getStringExtra("CARE_INFO") ?: "No information available"
        val accuracy = intent.getStringExtra("ACCURACY") ?: "Akurasi Tidak Masuk"
        val imageUriString = intent.getStringExtra("CAT_IMAGE_URI")

        // Set data ke UI
        binding.catName.text = catName
        binding.careInfo.text = careInfo
        binding.accuracyText.text = accuracy

        // Menampilkan gambar kucing jika ada
        if (!imageUriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(imageUriString)
            binding.catImageView.setImageURI(imageUri)
        }
    }

}
