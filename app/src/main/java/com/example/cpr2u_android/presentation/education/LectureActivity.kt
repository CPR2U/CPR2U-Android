package com.example.cpr2u_android.presentation.education

import android.os.Bundle
import android.webkit.WebViewClient
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.ActivityLectureBinding
import com.example.cpr2u_android.presentation.base.BaseActivity

class LectureActivity : BaseActivity<ActivityLectureBinding>(R.layout.activity_lecture) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webView.webViewClient = WebViewClient()
        // TODO : 추후 학습 영상 링크 url로 변경 필요
        binding.webView.loadUrl("https://www.naver.com/")
    }
}
