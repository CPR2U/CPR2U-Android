package com.example.cpr2u_android.data.repository.call

import com.example.cpr2u_android.data.datasource.call.CallDataSource
import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.call.ResponseCall
import com.example.cpr2u_android.data.model.response.call.ResponseCallList
import com.example.cpr2u_android.data.model.response.call.ResponseDispatch
import com.example.cpr2u_android.domain.repository.call.CallRepository

class CallRepositoryImpl(private val callDataSource: CallDataSource): CallRepository {
    override suspend fun postCall(data: RequestCall): ResponseCall {
        return callDataSource.postCall(data)
    }

    override suspend fun postCallEnd(callId: Int): GeneralResponse {
        return callDataSource.postCallEnd(callId)
    }

    override suspend fun getCallList(): ResponseCallList {
        return callDataSource.getCallList()
    }

    override suspend fun postDispatch(callID: Int): ResponseDispatch {
        return callDataSource.postDispatch(callID)
    }
}