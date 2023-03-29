package com.example.cpr2u_android.presentation.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    var _callId = -1
    private val _callUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val callUIState: StateFlow<UiState<Boolean>> = _callUIState

    private val _callEndUIState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val callEndUIState: StateFlow<UiState<Boolean>> = _callEndUIState

    private val _callEndSuccess = MutableLiveData<Boolean>()
    val callEndSuccess: LiveData<Boolean> = _callEndSuccess

    /*
    "latitude": 37.5440261,
  "longitude": 126.9671087,
  "full_address": "서울특별시 용산구 청파동3가 114-11"
     */
    fun postCall(latitude: Double, longitude: Double, address: String) = viewModelScope.launch {
        kotlin.runCatching {
            _callUIState.emit(UiState.Loading)
            val edit_address = address.substring(5, address.length)
            Timber.d("latitude -> $latitude, Longitude -> $longitude, address -> #$edit_address")
            callRepository.postCall(
                data = RequestCall(
                    latitude = 37.5440261,
                    longitude = 126.9671087,
                    fullAddress = "서울특별시 용산구 청파동3가 114-11",
                ),
            )
        }.onSuccess {
            Timber.d("post-call-success $it")
            _callId = it.data.callId
            _callUIState.emit(UiState.Success(true))
            Timber.d("set call ID -> $_callId")
        }.onFailure {
            _callUIState.emit(UiState.Failure("$it"))
            Timber.d("post-call-fail $it")
        }
    }

    fun postCallEnd(callId: Int) = viewModelScope.launch {
        kotlin.runCatching {
            _callEndUIState.emit(UiState.Loading)
            Timber.d("_callID -> $callId")
            callRepository.postCallEnd(callId)
        }.onSuccess {
            _callEndSuccess.value = true
            _callEndUIState.emit(UiState.Success(true))
            Timber.d("post-call-end-success $it")
        }.onFailure {
            _callEndSuccess.value = false
            _callEndUIState.emit(UiState.Failure("$it"))
            Timber.d("post-call-end-fail $it")
        }
    }

    fun getCallList() = viewModelScope.launch {
        kotlin.runCatching {
            callRepository.getCallList()
        }.onSuccess {
            Timber.d("get-call-list-success -> $it")
            it.data.callList
        }.onFailure {
            Timber.d("get-call-list-fail -> $it")
        }
    }
}
