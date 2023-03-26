package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CallService {

    @POST("/call")
    suspend fun postCall(
        @Body body: RequestCall,
    ): Int

    @POST("/call/end")
    suspend fun postCallEnd(
        @Query("call_id") callId: Int,
    ): GeneralResponse
}
