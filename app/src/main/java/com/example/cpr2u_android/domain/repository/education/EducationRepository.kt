package com.example.cpr2u_android.domain.repository.education

import com.example.cpr2u_android.data.model.request.RequestDispatchReport
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.education.ResponseQuizzesList
import com.example.cpr2u_android.data.model.response.education.ResponseUserInfo

interface EducationRepository {
    suspend fun postLectureId(lectureId: Int): GeneralResponse
    suspend fun getQuizzes(): ResponseQuizzesList
    suspend fun postQuizProgress(score: Int): GeneralResponse
    suspend fun postExercisesProgress(score: Int): GeneralResponse
    suspend fun getUserInfo(): ResponseUserInfo
}