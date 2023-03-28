package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.api.CallService
import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

class CallRemoteDataSource(private val callService: CallService) : CallDataSource {
    override suspend fun postCall(data: RequestCall): Int {
        return callService.postCall(data)
    }

    override suspend fun postCallEnd(callId: Int): GeneralResponse {
        return callService.postCallEnd(callId)
    }
}
