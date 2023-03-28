package com.example.cpr2u_android.presentation.education

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.DialogQuizBinding
import com.example.cpr2u_android.databinding.FragmentPosePractice3Binding
import com.example.cpr2u_android.presentation.base.BaseFragment
import com.example.cpr2u_android.util.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PosePractice3Fragment :
    BaseFragment<FragmentPosePractice3Binding>(R.layout.fragment_pose_practice_3) {
    private val educationViewModel: EducationViewModel by viewModel()
    private lateinit var callback: OnBackPressedCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val score = 80

        binding.btnQuit.setOnClickListener {
            if (score >= 80) {
                // 성공
                val dialog = Dialog(requireContext())
                val binding = DataBindingUtil.inflate<DialogQuizBinding>(
                    LayoutInflater.from(requireContext()),
                    R.layout.dialog_quiz,
                    null,
                    false,
                )
                binding.ivHeart.setImageResource(R.drawable.ic_certificate_big)
                binding.ivHeart.visibility = View.VISIBLE
                binding.tvTitle.text = "Congratulation!"
                binding.tvSubtitle.text = "You have got CPR Angel Certificate!"

                binding.buttonFinish.setOnClickListener {
                    educationViewModel.postExercisesProgress()
                    Timber.d("들어오니....")
                    educationViewModel.exercisesProgressUIState.flowWithLifecycle(lifecycle)
                        .onEach {
                            when (it) {
                                is UiState.Success -> {
                                    Timber.d("success")
                                    dialog.dismiss()
                                    activity?.finish()
                                }
                                else -> {}
                            }
                        }.launchIn(lifecycleScope)
                }
                dialog.setContentView(binding.root)
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                )
                dialog.show()
            } else { // 실패
                activity?.finish()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}
