package com.example.cpr2u_android.data.repository.call

import com.example.cpr2u_android.data.datasource.call.CallDataSource
import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.domain.repository.call.CallRepository

class CallRepositoryImpl(private val callDataSource: CallDataSource): CallRepository {
    override suspend fun postCall(data: RequestCall): Int {
        return callDataSource.postCall(data)
    }
}