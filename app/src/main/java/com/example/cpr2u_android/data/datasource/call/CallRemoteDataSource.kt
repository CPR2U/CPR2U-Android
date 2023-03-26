package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.api.CallService
import com.example.cpr2u_android.data.model.request.education.RequestCall

class CallRemoteDataSource(private val callService: CallService) : CallDataSource {
    override suspend fun postCall(data: RequestCall): String {
        return callService.postCall(data)
    }
}
