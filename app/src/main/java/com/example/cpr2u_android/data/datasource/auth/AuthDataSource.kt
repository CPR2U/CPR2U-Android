package com.example.cpr2u_android.data.datasource.auth

import com.example.cpr2u_android.data.model.response.auth.ResponseAutoLogin

interface AuthDataSource {
    suspend fun postAutoLogin(refreshToken: String): ResponseAutoLogin
}
