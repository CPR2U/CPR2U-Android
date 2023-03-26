package com.example.cpr2u_android.data.model.request.education


import com.google.gson.annotations.SerializedName

data class RequestCall(
    @SerializedName("full_address")
    val fullAddress: String,
    @SerializedName("latitude")
    val latitude: Int,
    @SerializedName("longitude")
    val longitude: Int
)