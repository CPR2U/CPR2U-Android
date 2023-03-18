package com.example.cpr2u_android.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cpr2u_android.R
import com.example.cpr2u_android.data.model.response.ResponsePhoneVerification
import com.example.cpr2u_android.data.service.ServiceCreator
import com.example.cpr2u_android.databinding.FragmentLoginPhoneNumberBinding
import com.example.cpr2u_android.presentation.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPhoneNumberFragment :
    BaseFragment<FragmentLoginPhoneNumberBinding>(R.layout.fragment_login_phone_number) {
    private val signInViewModel: SignInViewModel by activityViewModels()
    private var bundle = Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickEvent()
        initNetwork()
        observePhoneNumber()
    }

    private fun initNetwork() {
        val phoneNumber = binding.etNumber.text.toString()
        val call: Call<ResponsePhoneVerification> =
            ServiceCreator.authService.postVerification(phoneNumber)

        call.enqueue(object : Callback<ResponsePhoneVerification> {
            override fun onResponse(
                call: Call<ResponsePhoneVerification>,
                response: Response<ResponsePhoneVerification>,
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val validationCode = data?.validationCode
                }
            }

            override fun onFailure(call: Call<ResponsePhoneVerification>, t: Throwable) {
                Log.e("Network", "error: $t")
            }
        })
    }

    private fun observePhoneNumber() {
        binding.etNumber.addTextChangedListener {
            binding.tvSend.isSelected = !it.isNullOrEmpty()
        }
    }

    private fun initClickEvent() {
        binding.tvSend.setOnClickListener {
            if (binding.tvSend.isSelected) {
                bundle = Bundle().apply {
                    putString("phoneNumber", binding.etNumber.text.toString())
                }
                findNavController().navigate(
                    R.id.action_loginPhoneNumberFragment_to_loginPhoneNumberCheckFragment,
                    bundle,
                )
            }
        }
    }
}
