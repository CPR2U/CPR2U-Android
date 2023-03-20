package com.example.cpr2u_android.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.cpr2u_android.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplash()

        setContentView(R.layout.activity_splash)
    }

    private fun initSplash() {
        Handler(
            Looper.getMainLooper(),
        ).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
    
    companion object {
        const val SPLASH_VIEW_TIME = 1200L
    }
}
