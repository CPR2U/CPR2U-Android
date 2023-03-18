package com.example.cpr2u_android.data.service

import com.example.cpr2u_android.data.model.response.ResponsePhoneVerification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Content-Type:application/json")
    @POST("auth/verification")
    fun postVerification(
        @Body body: String,
    ): Call<ResponsePhoneVerification>
}
