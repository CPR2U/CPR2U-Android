package com.example.cpr2u_android.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentLoginPhoneNumberCheckBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

class LoginPhoneNumberCheckFragment :
    BaseFragment<FragmentLoginPhoneNumberCheckBinding>(R.layout.fragment_login_phone_number_check) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEvent()
    }

    private fun initClickEvent() {
        binding.tvConfirm.setOnClickListener {
            val intent =
                Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}
