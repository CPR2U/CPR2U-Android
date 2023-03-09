package com.example.cpr2u_android.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.ActivitySignUpBinding
import com.example.cpr2u_android.presentation.MainActivity
import com.example.cpr2u_android.presentation.base.BaseActivity
import java.util.regex.Pattern

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTextChangeEvent()

        binding.tvConfirm.setOnClickListener {
            if (binding.isError == false) {
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            } else if (binding.etNickname.text.isEmpty()) {
                binding.isError = true
                binding.tvErrorMessage.text = getString(R.string.signup_set_nickname)
            }
        }
    }

    private fun initTextChangeEvent() {
        val ps =
            Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")

        with(binding) {
            etNickname.addTextChangedListener {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        isError = false
                        if (!ps.matcher(it).matches()) {
                            isError = true
                            tvErrorMessage.text = getString(R.string.signup_no_special_characters)
                        } else if (etNickname.text.length > 10) {
                            etNickname.setText(it.toString().subSequence(0, 10))
                            etNickname.setSelection(10)
                        }
                    } else {
                        isError = true
                        tvErrorMessage.text = getString(R.string.signup_set_nickname)
                    }
                }
            }
        }
    }
}
