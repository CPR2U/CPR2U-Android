package com.example.cpr2u_android.data.datasource.education

import com.example.cpr2u_android.data.model.response.auth.GeneralResponse

interface EducationDataSource {
    suspend fun postLectureId(lectureId: Int): GeneralResponse
}
