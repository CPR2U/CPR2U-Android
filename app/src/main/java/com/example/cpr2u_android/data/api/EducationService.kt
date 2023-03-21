package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface EducationService {
    @POST("lectures/progress/")
    suspend fun postLectureProgress(
        @Query("lectureId") lectureId: Int,
    ): GeneralResponse
}
