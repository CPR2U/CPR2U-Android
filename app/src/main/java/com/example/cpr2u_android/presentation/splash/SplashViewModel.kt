package com.example.cpr2u_android.presentation.splash

import android.content.ContentValues
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel : ViewModel() {

    private val _deviceToken = MutableLiveData<String>()
    var deviceToken: LiveData<String> = _deviceToken

    fun getDeviceToken() {
        viewModelScope.launch {
            kotlin.runCatching {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Timber.tag(ContentValues.TAG)
                                .w(task.exception, "Fetching FCM registration token failed")
                            return@OnCompleteListener
                        }
                        _deviceToken.value = task.result
                    },
                )
            }
        }
    }
}
