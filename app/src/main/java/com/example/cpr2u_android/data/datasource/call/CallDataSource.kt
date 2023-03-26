package com.example.cpr2u_android.data.datasource.call

import com.example.cpr2u_android.data.model.request.education.RequestCall

interface CallDataSource {
    suspend fun postCall(data: RequestCall): Int
}