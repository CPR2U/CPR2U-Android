package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.call.ResponseCall
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CallService {

    @POST("/call")
    suspend fun postCall(
        @Body body: RequestCall,
    ): ResponseCall

    @POST("/call/end/{call_id}")
    suspend fun postCallEnd(
        @Path("call_id") call_id: Int,
    ): GeneralResponse
}
