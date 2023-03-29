package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.api.CallService
import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.call.ResponseCall
import timber.log.Timber

class CallRemoteDataSource(private val callService: CallService) : CallDataSource {
    override suspend fun postCall(data: RequestCall): ResponseCall {
        return callService.postCall(data)
    }

    override suspend fun postCallEnd(callId: Int): GeneralResponse {
        Timber.d("data call Id -> $callId")
        return callService.postCallEnd(callId)
    }
}
