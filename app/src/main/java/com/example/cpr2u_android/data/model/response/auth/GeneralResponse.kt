package com.example.cpr2u_android.data.model.response.auth


import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)