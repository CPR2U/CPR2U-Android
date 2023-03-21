package com.example.cpr2u_android.domain.repository.education

import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

interface EducationRepository {
    suspend fun postLectureId(lectureId: Int): GeneralResponse
}