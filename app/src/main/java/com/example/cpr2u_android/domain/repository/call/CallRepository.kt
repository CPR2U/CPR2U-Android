package com.example.cpr2u_android.domain.repository.call

import com.example.cpr2u_android.data.model.request.education.RequestCall

interface CallRepository {
    suspend fun postCall(data: RequestCall) : String
}