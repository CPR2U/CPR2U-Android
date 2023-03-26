package com.example.cpr2u_android.presentation.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpr2u_android.data.model.request.education.RequestCall
import com.example.cpr2u_android.domain.repository.call.CallRepository
import com.example.cpr2u_android.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CallViewModel(private val callRepository: CallRepository) : ViewModel() {

    private var _callId: Int = -1
    private val _callUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val callUIState: StateFlow<UiState<Boolean>> = _callUIState

    private val _callEndUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val callEndUIState: StateFlow<UiState<Boolean>> = _callEndUIState
    fun postCall(latitude: Double, longitude: Double, address: String) = viewModelScope.launch {
        kotlin.runCatching {
            _callUIState.emit(UiState.Loading)
            callRepository.postCall(
                data = RequestCall(
                    latitude = latitude,
                    longitude = longitude,
                    fullAddress = address,
                ),
            )
        }.onSuccess {
            _callUIState.emit(UiState.Success(true))
            Timber.d("post-call-success $it")
            _callId = it
        }.onFailure {
            _callUIState.emit(UiState.Failure("$it"))
            Timber.d("post-call-fail $it")
        }
    }

    fun postCallEnd() = viewModelScope.launch {
        kotlin.runCatching {
            _callEndUIState.emit(UiState.Loading)
            callRepository.postCallEnd(_callId)
        }.onSuccess {
            _callEndUIState.emit(UiState.Success(true))
            Timber.d("post-call-end-success $it")
        }.onFailure {
            _callEndUIState.emit(UiState.Failure("$it"))
            Timber.d("post-call-end-fail $it")
        }
    }
}
