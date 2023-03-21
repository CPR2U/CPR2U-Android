package com.example.cpr2u_android.data.repository.education

import com.example.cpr2u_android.data.datasource.education.EducationDataSource
import com.example.cpr2u_android.data.model.response.auth.GeneralResponse
import com.example.cpr2u_android.domain.repository.education.EducationRepository

class EducationRepositoryImpl(private val educationDataSource: EducationDataSource) : EducationRepository {
    override suspend fun postLectureId(lectureId: Int): GeneralResponse {
        return educationDataSource.postLectureId(lectureId)
    }
}