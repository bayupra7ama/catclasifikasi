package com.example.myapplication.remote

import com.example.myapplication.response.ApiPrediksiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.POST

interface ApiService {
    @Multipart
    @POST("predict")
    fun predictImage(
        @Part image: MultipartBody.Part
    ): Call<ApiPrediksiResponse>
}

