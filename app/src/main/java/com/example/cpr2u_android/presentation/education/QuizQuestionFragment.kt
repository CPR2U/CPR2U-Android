package com.example.cpr2u_android.presentation.education

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentQuizQuestionBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class QuizQuestionFragment :
    BaseFragment<FragmentQuizQuestionBinding>(R.layout.fragment_quiz_question) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(R.id.action_QuizQuestionFragment_to_QuizAnswerFragment)
        }
    }
}
