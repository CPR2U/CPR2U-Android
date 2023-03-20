package com.example.cpr2u_android.data.model.response

import com.google.gson.annotations.SerializedName

data class ResponsePhoneVerification(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
)

data class Data(
    @SerializedName("validation_code")
    val validationCode: String,
)
