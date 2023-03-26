package com.example.cpr2u_android.data.datasource.education

import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.education.ResponseQuizzesList

interface EducationDataSource {
    suspend fun postLectureId(lectureId: Int): GeneralResponse
    suspend fun getQuizzes(): ResponseQuizzesList
    suspend fun postQuizProgress(score: Int): GeneralResponse
}
