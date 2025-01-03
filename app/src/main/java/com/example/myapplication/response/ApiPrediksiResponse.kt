package com.example.myapplication.response
import com.google.gson.annotations.SerializedName
data class ApiPrediksiResponse(
    @field:SerializedName("confidence")
    val confidence: String? = null,

    @field:SerializedName("class")
    val jsonMemberClass: String? = null,

    @field:SerializedName("care_info")
    val careInfo: String? = null,  // Menambahkan field untuk informasi perawatan

    @field:SerializedName("comment")
    val comment: String? = null
)
