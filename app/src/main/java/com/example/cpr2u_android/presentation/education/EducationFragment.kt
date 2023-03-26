package com.example.cpr2u_android.presentation.education

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentEducationBinding
import com.example.cpr2u_android.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EducationFragment : BaseFragment<FragmentEducationBinding>(R.layout.fragment_education) {
    private val educationViewModel: EducationViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        educationViewModel.getUserInfo()
        educationViewModel.userInfo.observe(viewLifecycleOwner) {
            if (educationViewModel.userInfo.value?.isLectureCompleted == 2) {
                binding.clLecture.isSelected = true
                binding.tvLectureComplete.text = "Complete"
            } else {
                binding.clLecture.isSelected = false
                binding.tvLectureComplete.text = "Not Completed"
            }

            if (educationViewModel.userInfo.value?.isQuizCompleted == 2) {
                binding.clQuiz.isSelected = true
                binding.tvQuizComplete.text = "Complete"
            } else {
                binding.clQuiz.isSelected = false
                binding.tvQuizComplete.text = "Not Completed"
            }

            if (educationViewModel.userInfo.value?.isPostureCompleted == 2) {
                binding.clPosturePractice.isSelected = true
                binding.tvPosePracticeComplete.text = "Complete"
            } else {
                binding.clPosturePractice.isSelected = false
                binding.tvPosePracticeComplete.text = "Not Completed"
            }

            binding.progressBar.progress = (educationViewModel._userInfo.value?.progressPercent!! * 100).toInt()
            binding.tvNickname.text = educationViewModel._userInfo.value?.nickname

            when (educationViewModel.userInfo.value?.angelStatus) {
                0 -> {
                    binding.acquired = true
                    binding.tvUserAcquired.text =
                        "ACQUIRED (D-${educationViewModel.userInfo.value!!.daysLeftUntilExpiration})"
                }
                1 -> {
                    binding.acquired = false
                    binding.tvUserAcquired.text = "EXPIRED"
                }
                else -> {
                    binding.acquired = false
                    binding.tvUserAcquired.text = "UNACQUIRED"
                }
            }
        }

        binding.clLecture.setOnClickListener {
            startActivity(Intent(requireContext(), LectureActivity::class.java))
        }

        binding.clQuiz.setOnClickListener {
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        educationViewModel.getUserInfo()
    }
}
