package com.example.cpr2u_android.presentation.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpr2u_android.domain.repository.education.EducationRepository
import com.example.cpr2u_android.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class EducationViewModel(private val educationRepository: EducationRepository) : ViewModel() {
    private val _testUIState = MutableStateFlow<UiState<Boolean>> (UiState.Loading)
    val testUIState: StateFlow<UiState<Boolean>> = _testUIState

    fun postLectureId() = viewModelScope.launch {
        kotlin.runCatching {
            _testUIState.emit(UiState.Loading)
            educationRepository.postLectureId(0)
        }.onSuccess {
            _testUIState.emit(UiState.Success(true))
            Timber.d("post-lecture-id-success $it")
        }.onFailure {
            _testUIState.emit(UiState.Failure("$it"))
            Timber.d("post-lecture-id-fail $it")
        }
    }
}
