package com.example.cpr2u_android.data.repository.auth

import com.example.cpr2u_android.data.datasource.auth.AuthDataSource
import com.example.cpr2u_android.data.model.response.auth.ResponseAutoLogin
import com.example.cpr2u_android.domain.repository.auth.AuthRepository
import timber.log.Timber

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun postAuthLogin(refreshToken: String): ResponseAutoLogin? {
        var result: ResponseAutoLogin? = null
        runCatching {
            authDataSource.postAutoLogin(refreshToken)
        }.onSuccess {
            Timber.d("RepositoryImpl : post-auto-login-server-success : $it")
            result = it
        }.onFailure {
            Timber.e("RepositoryImpl : post-auto-login-server-fail : $it")
        }
        return result
    }
}
