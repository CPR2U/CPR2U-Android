package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.call.ResponseCall
import com.example.cpr2u_android.data.model.response.call.ResponseCallList

interface CallDataSource {
    suspend fun postCall(data: RequestCall): ResponseCall
    suspend fun postCallEnd(callId: Int): GeneralResponse
    suspend fun getCallList(): ResponseCallList
}