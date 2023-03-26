package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

interface CallDataSource {
    suspend fun postCall(data: RequestCall): Int
    suspend fun postCallEnd(callId: Int): GeneralResponse
}