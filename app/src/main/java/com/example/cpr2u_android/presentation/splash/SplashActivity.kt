package com.example.cpr2u_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cpr2u_android.R
import com.example.cpr2u_android.data.sharedpref.CPR2USharedPreference
import com.example.cpr2u_android.presentation.MainActivity
import com.example.cpr2u_android.presentation.auth.LoginActivity
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplash()

        setContentView(R.layout.activity_splash)
    }

    private fun initSplash() {
        Handler(
            Looper.getMainLooper(),
        ).postDelayed({
            checkDeviceToken()
        }, SPLASH_VIEW_TIME)
    }

    private fun checkDeviceToken() {
        if (CPR2USharedPreference.getDeviceToken() == "") {
            splashViewModel.getDeviceToken()
            splashViewModel.deviceToken.observe(this) {
                Timber.d("device token $it")
                CPR2USharedPreference.setDeviceToken(it)
                navigateToNext()
            }
        } else {
            navigateToNext()
        }
    }

    private fun navigateToNext() {
        Timber.d("isLogin ${CPR2USharedPreference.getIsLogin()}")

        val nextView = if (CPR2USharedPreference.getIsLogin()) { // 자동로그인 여부
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }
        startActivity(Intent(this@SplashActivity, nextView))
        finish()
    }

    companion object {
        const val SPLASH_VIEW_TIME = 1200L
    }
}
