package com.example.cpr2u_android.data.datasource.auth

import com.example.cpr2u_android.data.api.AuthService
import com.example.cpr2u_android.data.model.response.auth.ResponseAutoLogin

class AuthRemoteDataSource(private val authService: AuthService): AuthDataSource {
    override suspend fun postAutoLogin(refreshToken: String): ResponseAutoLogin {
        return authService.postAutoLogin(refreshToken)
    }

}