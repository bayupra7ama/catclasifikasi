package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityCheckBreedBinding
import com.example.myapplication.remote.RetrofitClient
import com.example.myapplication.response.ApiPrediksiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class CheckBreedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckBreedBinding
    private val pickImageRequest = 1
    private val cameraRequest = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBreedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequest)
        }

        binding.btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, cameraRequest)
        }

        binding.btnPostPhoto.setOnClickListener {
            val imageBitmap = (binding.imageView.drawable as? BitmapDrawable)?.bitmap
            if (imageBitmap != null) {
                uploadImageToServer(imageBitmap)
            } else {
                Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                pickImageRequest -> {
                    val selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                    Toast.makeText(this, "Gambar berhasil diambil dari galeri!", Toast.LENGTH_SHORT).show()
                }
                cameraRequest-> {
                    val photo = data?.extras?.get("data") as Bitmap
                    binding.imageView.setImageBitmap(photo)
                    Toast.makeText(this, "Foto berhasil diambil!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToServer(bitmap: Bitmap) {
        // Konversi bitmap ke byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Simpan gambar sementara di cache directory
        val file = File(cacheDir, "cat_image.jpg")
        file.writeBytes(imageBytes)

        // Uri gambar untuk Intent
        val uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            file
        )

        // Panggil API Retrofit
        val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
        val apiService = RetrofitClient.apiService
        val call = apiService.predictImage(imagePart)

        call.enqueue(object : Callback<ApiPrediksiResponse> {
            override fun onResponse(call: Call<ApiPrediksiResponse>, response: Response<ApiPrediksiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val confidence = apiResponse.confidence
                        val predictedClass = apiResponse.jsonMemberClass
                        val comment = apiResponse.comment
                        val info = apiResponse.careInfo

                        if (!predictedClass.isNullOrEmpty()) {
                            val intent = Intent(this@CheckBreedActivity, DetailActivity::class.java)

                            // Kirim data ke DetailActivity
                            intent.putExtra("CAT_NAME", predictedClass)
                            intent.putExtra("CARE_INFO", info)
                            intent.putExtra("ACCURACY", confidence)
                            intent.putExtra("Comment", comment)

                            // Kirim Uri gambar
                            intent.putExtra("CAT_IMAGE_URI", uri.toString())

                            startActivity(intent)
                        } else {
                            Toast.makeText(binding.root.context, "Ras Tidak Diketahui, atau gambar anda kurang jelas", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(binding.root.context, "Respons kosong dari server!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(binding.root.context, "Terjadi kesalahan: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiPrediksiResponse>, t: Throwable) {
                Toast.makeText(this@CheckBreedActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}