package com.example.cpr2u_android.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentLoginPhoneNumberBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

class LoginPhoneNumberFragment :
    BaseFragment<FragmentLoginPhoneNumberBinding>(R.layout.fragment_login_phone_number) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickEvent()
    }

    private fun initClickEvent() {
        binding.tvSend.setOnClickListener {
            findNavController().navigate(R.id.action_loginPhoneNumberFragment_to_loginPhoneNumberCheckFragment)
        }
    }
}
