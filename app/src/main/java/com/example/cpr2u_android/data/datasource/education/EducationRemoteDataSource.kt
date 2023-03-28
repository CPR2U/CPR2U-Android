package com.example.cpr2u_android.data.datasource.education

import com.example.cpr2u_android.data.api.EducationService
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.data.model.response.education.ResponseQuizzesList
import com.example.cpr2u_android.data.model.response.education.ResponseUserInfo

class EducationRemoteDataSource(private val educationService: EducationService) : EducationDataSource {
    override suspend fun postLectureId(lectureId: Int): GeneralResponse {
        return educationService.postLectureProgress(lectureId)
    }

    override suspend fun getQuizzes(): ResponseQuizzesList {
        return educationService.getQuizzes()
    }

    override suspend fun postQuizProgress(score: Int): GeneralResponse {
        return educationService.postQuizProgress(score)
    }

    override suspend fun postExercisesProgress(score: Int): GeneralResponse {
        return educationService.postExercisesProgress(score)
    }

    override suspend fun getUserInfo(): ResponseUserInfo {
        return educationService.getUserInfo()
    }
}