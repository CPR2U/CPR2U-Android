package com.example.cpr2u_android.presentation.education

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentQuizAnswerBinding
import com.example.cpr2u_android.presentation.base.BaseFragment

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuizAnswerFragment : BaseFragment<FragmentQuizAnswerBinding>(R.layout.fragment_quiz_answer) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_QuizAnswerFragment_to_QuizQuestionFragment)
        }
    }
}
