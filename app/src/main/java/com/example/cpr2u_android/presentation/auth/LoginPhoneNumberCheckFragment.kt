package com.example.cpr2u_android.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentLoginPhoneNumberCheckBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

class LoginPhoneNumberCheckFragment :
    BaseFragment<FragmentLoginPhoneNumberCheckBinding>(R.layout.fragment_login_phone_number_check) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEvent()
        initSmsCodeEvent()
    }

    private fun initClickEvent() {
        binding.tvConfirm.setOnClickListener {
            val intent =
                Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }

    private fun initSmsCodeEvent() {
        val smsCode: List<EditText> =
            listOf(binding.tvSmsCode1, binding.tvSmsCode2, binding.tvSmsCode3, binding.tvSmsCode4)

        for (i in 0..3) {
            if (i in 0..2) {
                smsCode[i].addTextChangedListener {
                    if (smsCode[i].length() > 0) {
                        smsCode[i + 1].requestFocus()
                    }
                }
            }
            smsCode[i].onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    smsCode[i].text.clear()
                }
            }
        }
    }
}
