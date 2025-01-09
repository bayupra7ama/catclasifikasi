package com.example.myapplication.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://evident-conversely-bird.ngrok-free.app"

    // Membuat HttpLoggingInterceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log detail request dan response
    }

    // Membuat OkHttpClient dengan interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Menambahkan interceptor ke OkHttpClient
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // Menggunakan OkHttpClient yang sudah diubah
        .addConverterFactory(GsonConverterFactory.create())  // Menambahkan converter untuk JSON
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
