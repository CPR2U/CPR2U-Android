package com.example.cpr2u_android.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentLoginPhoneNumberCheckBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

class LoginPhoneNumberCheckFragment :
    BaseFragment<FragmentLoginPhoneNumberCheckBinding>(R.layout.fragment_login_phone_number_check) {
    private val signInViewModel: SignInViewModel by activityViewModels()
    private lateinit var smsCode: List<EditText>
    var smsCodeStr: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smsCode =
            listOf(binding.tvSmsCode1, binding.tvSmsCode2, binding.tvSmsCode3, binding.tvSmsCode4)

        initPhoneNumber()
        initClickEvent()
        initSmsCodeEvent()
    }

    private fun initPhoneNumber() {
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        binding.phoneNumber = phoneNumber
    }

    private fun initClickEvent() {
        binding.tvConfirm.setOnClickListener {
            smsCode.forEach {
                smsCodeStr += it.text.toString()
            }

            if (smsCodeStr.length < 4) {
                Toast.makeText(requireContext(), "코드를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val intent =
                    Intent(requireContext(), SignUpActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
        }
    }

    private fun initSmsCodeEvent() {
        for (i in 0..3) {
            smsCode[i].addTextChangedListener {
                if (i in 0..2) {
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
