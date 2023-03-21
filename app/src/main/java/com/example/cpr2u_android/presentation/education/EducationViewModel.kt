package com.example.cpr2u_android.presentation.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpr2u_android.data.model.response.education.QuizzesListData
import com.example.cpr2u_android.domain.repository.education.EducationRepository
import com.example.cpr2u_android.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class EducationViewModel(private val educationRepository: EducationRepository) : ViewModel() {
    private val _testUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val testUIState: StateFlow<UiState<Boolean>> = _testUIState

    private val _quizzesUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val quizzesUIState: StateFlow<UiState<Boolean>> = _quizzesUIState

    private val _quizProgressUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val quizProgressUIState: StateFlow<UiState<Boolean>> = _quizProgressUIState

    private var _quizzesList = listOf<QuizzesListData>()
    val quizzesList = _quizzesList

    var question: String = ""
    var correct: Boolean = false
    var index: Int = 0
    var correctCount: Int = 0

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

    fun getQuizzes() = viewModelScope.launch {
        kotlin.runCatching {
            educationRepository.getQuizzes()
        }.onSuccess {
            Timber.d("get-quizzes-success -> ${it.data}")
            _quizzesList = it.data
            _quizzesUIState.emit(UiState.Success(true))
        }.onFailure {
            _quizzesUIState.emit(UiState.Failure("$it"))
            Timber.d("get-quizzes-fail -> $it")
        }
    }

    fun postQuizProgress() = viewModelScope.launch {
        kotlin.runCatching {
            educationRepository.postQuizProgress(100)
        }.onSuccess {
            Timber.d("post-quiz-progress-success -> $it")
            _quizProgressUIState.emit(UiState.Success(true))
        }.onFailure {
            Timber.d("post-quiz-progress-fail -> $it")
            _quizProgressUIState.emit(UiState.Failure("$it"))
        }
    }

    fun getInitQuizzesList(): List<QuizzesListData> {
        return _quizzesList
    }
}
