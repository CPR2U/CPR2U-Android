package com.example.cpr2u_android.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpr2u_android.data.model.request.auth.RequestLogin
import com.example.cpr2u_android.data.sharedpref.CPR2USharedPreference
import com.example.cpr2u_android.domain.repository.auth.AuthRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private var _phoneNumber: String = ""
    val phoneNumber: String
        get() = _phoneNumber

    private val _validationCode = MutableLiveData<String>("")
    var validationCode: LiveData<String> = _validationCode

    private val _isUser = MutableLiveData<Boolean>(false)
    var isUser: LiveData<Boolean> = _isUser

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }

    fun postVerification(phoneNumber: String) = viewModelScope.launch {
        kotlin.runCatching {
            authRepository.postVerification(phoneNumber)
        }.onSuccess {
            Timber.d("validation code success -> ${it.data.validationCode}")
            _validationCode.value = it.data.validationCode
            Timber.d("validation code success set -> ${_validationCode.value}")
        }.onFailure {
            Timber.e("validation code fail $it")
        }
    }

    fun postLogin(loginData: RequestLogin) = viewModelScope.launch {
        kotlin.runCatching {
            authRepository.postLogin(loginData)
        }.onSuccess {
            Timber.d("인증된 사용자. 메인화면으로")
            _isUser.value = true
            CPR2USharedPreference.setAccessToken(it.data.accessToken)
            CPR2USharedPreference.setRefreshToken(it.data.refreshToken)
        }.onFailure {
            Timber.d("인증되지 않은 사용자. 회원가입 필요")
            _isUser.value = false
        }
    }

    fun getValidationCode(): String? {
        return _validationCode.value
    }
}
