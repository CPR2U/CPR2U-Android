package com.example.cpr2u_android.data.api

import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.education.ResponseQuizzesList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EducationService {
    @POST("lectures/progress/")
    suspend fun postLectureProgress(
        @Query("lectureId") lectureId: Int,
    ): GeneralResponse

    @GET("/education/quizzes")
    suspend fun getQuizzes(): ResponseQuizzesList

    @POST("quizzes/progress")
    suspend fun postQuizProgress(
        @Body score: Int,
    ): GeneralResponse
}
