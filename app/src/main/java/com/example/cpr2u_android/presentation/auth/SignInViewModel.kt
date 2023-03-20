package com.example.cpr2u_android.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpr2u_android.domain.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private var _phoneNumber: String = ""
    val phoneNumber: String
        get() = _phoneNumber

    private val _validationCode = MutableLiveData<String>("")
    var validationCode: LiveData<String> = _validationCode

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    fun postVerification(phoneNumber: String) = viewModelScope.launch {
        kotlin.runCatching {
            authRepository.postVerification(phoneNumber)
        }.onSuccess {
            Timber.d("validation code success -> ${it.data.validationCode}")
            _validationCode.value = it.data.validationCode
        }.onFailure {
            Timber.e("validation code fail $it")
        }
    }

    fun getValidationCode(): String? {
        return _validationCode.value
    }
}
