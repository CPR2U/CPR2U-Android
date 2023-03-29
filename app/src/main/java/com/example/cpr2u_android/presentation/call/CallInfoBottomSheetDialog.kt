package com.example.cpr2u_android.presentation.call

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpr2u_android.databinding.BottomSheetMapBinding
import com.example.cpr2u_android.domain.model.CallInfoBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CallInfoBottomSheetDialog(val item: CallInfoBottomSheet) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetMapBinding
    private val callViewModel: CallViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            BottomSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mapInfo = item
            tvDispatch.setOnClickListener {
                // 출동하기 성공시 dismiss
                callViewModel.postDispatch(item.callId)
                callViewModel.dispatchSuccess.observe(viewLifecycleOwner) {
                    if (it) {
                        Timber.d("it , dismiss ->$it")
                        dismiss()
                    } else {
                        Timber.d("it -> $it")
                    }
                }
            }
        }
    }
}
