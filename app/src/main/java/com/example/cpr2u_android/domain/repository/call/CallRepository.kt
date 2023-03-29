package com.example.cpr2u_android.domain.repository.call

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.call.ResponseCall

interface CallRepository {
    suspend fun postCall(data: RequestCall): ResponseCall
    suspend fun postCallEnd(callId: Int): GeneralResponse
}