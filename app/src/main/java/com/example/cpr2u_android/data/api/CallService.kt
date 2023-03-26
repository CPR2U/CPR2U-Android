package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.request.education.RequestCall
import retrofit2.http.Body
import retrofit2.http.POST

interface CallService {

    @POST("/call")
    suspend fun postCall(
        @Body body: RequestCall,
    ): String
}
