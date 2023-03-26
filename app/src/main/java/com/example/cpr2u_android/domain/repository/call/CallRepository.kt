package com.example.cpr2u_android.domain.repository.call

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

interface CallRepository {
    suspend fun postCall(data: RequestCall): Int
    suspend fun postCallEnd(callId: Int): GeneralResponse
}