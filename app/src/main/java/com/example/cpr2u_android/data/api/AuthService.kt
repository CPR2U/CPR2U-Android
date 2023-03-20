package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.response.auth.ResponseAutoLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/auto-login")
    suspend fun postAutoLogin(
        @Body refreshToken: String,
    ): ResponseAutoLogin
}
