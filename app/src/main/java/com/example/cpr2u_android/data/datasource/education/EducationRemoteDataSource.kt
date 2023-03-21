package com.example.cpr2u_android.data.datasource.education

import com.example.cpr2u_android.data.api.EducationService
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

class EducationRemoteDataSource(private val educationService: EducationService) : EducationDataSource {
    override suspend fun postLectureId(lectureId: Int): GeneralResponse {
        return educationService.postLectureProgress(lectureId)
    }
}