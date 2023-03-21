package com.example.cpr2u_android.presentation.education

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentEducationBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

class EducationFragment : BaseFragment<FragmentEducationBinding>(R.layout.fragment_education) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clLecture.setOnClickListener {
            startActivity(Intent(requireContext(), LectureActivity::class.java))
        }
    }
}
