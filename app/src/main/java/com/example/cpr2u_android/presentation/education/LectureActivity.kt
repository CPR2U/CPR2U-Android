package com.example.cpr2u_android.presentation.education

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.ActivityLectureBinding
import com.example.cpr2u_android.databinding.DialogQuizBinding
import com.example.cpr2u_android.presentation.base.BaseActivity
import timber.log.Timber

class LectureActivity : BaseActivity<ActivityLectureBinding>(R.layout.activity_lecture) {
    private var start: Long = 0
    private var end: Long = 0
    private var sum: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webView.webViewClient = WebViewClient()
        // TODO : 추후 학습 영상 링크 url로 변경 필요
        binding.webView.loadUrl("https://www.naver.com/")
        Timber.d("## on Start -> $start")

        Handler(Looper.getMainLooper()).postDelayed({
            val dialog = Dialog(this)
            val binding = DataBindingUtil.inflate<DialogQuizBinding>(
                LayoutInflater.from(this),
                R.layout.dialog_quiz,
                null,
                false,
            )
            binding.buttonFinish.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            dialog.setContentView(binding.root)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )

            dialog.show()
        }, 5000L)
    }
}
