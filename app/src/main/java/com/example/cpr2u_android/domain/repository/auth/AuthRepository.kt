package com.example.cpr2u_android.domain.repository.auth

import com.example.cpr2u_android.data.model.response.auth.ResponseAutoLogin

interface AuthRepository {

    suspend fun postAuthLogin(refreshToken: String): ResponseAutoLogin?
}
