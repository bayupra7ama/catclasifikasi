package com.example.myapplication.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://evident-conversely-bird.ngrok-free.app"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())  // Menambahkan converter untuk JSON
        .build()
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
