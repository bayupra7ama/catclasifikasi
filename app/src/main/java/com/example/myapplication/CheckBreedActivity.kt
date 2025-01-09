package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCheckBreedBinding
import com.example.myapplication.remote.RetrofitClient
import com.example.myapplication.response.ApiResponse
import com.example.myapplication.ui.DetailViewPagerActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

@Suppress("DEPRECATION")
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
                toggleUI(false) // Nonaktifkan tombol dan tampilkan ProgressBar
                uploadImageToServer(imageBitmap)
            } else {
                Toast.makeText(this, "Pilih atau ambil gambar terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                pickImageRequest -> {
                    val selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                    Toast.makeText(this, "Gambar berhasil diambil dari galeri!", Toast.LENGTH_SHORT).show()
                }
                cameraRequest -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    binding.imageView.setImageBitmap(photo)
                    Toast.makeText(this, "Foto berhasil diambil!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToServer(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        val file = File(cacheDir, "cat_image.jpg").apply {
            writeBytes(imageBytes)
        }

        val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val apiService = RetrofitClient.apiService
        val call = apiService.predictImage(imagePart)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                toggleUI(true) // Aktifkan kembali tombol dan sembunyikan ProgressBar
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val intent = Intent(this@CheckBreedActivity, DetailViewPagerActivity::class.java).apply {
                            putExtra("CAT_IMAGE_URI", file.absolutePath)
                            putExtra("CARE_DETAILS", apiResponse.careDetails)
                            putExtra("FOOD_DETAILS", apiResponse.makanan)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CheckBreedActivity, "Respons kosong dari server!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        this@CheckBreedActivity,
                        "Gagal mengunggah gambar: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                toggleUI(true) // Aktifkan kembali tombol dan sembunyikan ProgressBar
                Toast.makeText(
                    this@CheckBreedActivity,
                    "Gagal mengunggah gambar: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UploadImageError", "Error saat mengunggah gambar", t)
            }
        })
    }

    private fun toggleUI(enable: Boolean) {
        binding.progressBar.visibility = if (enable) View.GONE else View.VISIBLE
        binding.btnChooseImage.isEnabled = enable
        binding.btnTakePhoto.isEnabled = enable
        binding.btnPostPhoto.isEnabled = enable
    }
}
