package com.example.cpr2u_android.presentation.auth

import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {
    private var _phoneNumber: String = ""
    val phoneNumber: String
        get() = _phoneNumber

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber = phoneNumber
    }
}
