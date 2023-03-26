package com.example.cpr2u_android.presentation.call

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.lifecycle.flowWithLifecycle
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.ActivityCallingBinding
import com.example.cpr2u_android.presentation.base.BaseActivity
import com.example.cpr2u_android.util.UiState
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CallingActivity : BaseActivity<ActivityCallingBinding>(R.layout.activity_calling) {
    private val callViewModel: CallViewModel by viewModel()

    private var timerSec: Int = 0
    private var time: TimerTask? = null
    private var timerText: TextView? = null
    private val handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvSituationEnd.setOnClickListener {
            callViewModel.postCallEnd()
            callViewModel.callEndUIState.flowWithLifecycle(lifecycle).onEach {
                when(it) {
                    is UiState.Success -> {
                        finish()
                    }
                    else -> {}
                }
            }
        }

        timerText = binding.tvMinute
        timerSec = 0
        time = object : TimerTask() {
            override fun run() {
                updateTime()
                if (timerSec >= 300) return
                if (timerSec % 15 == 0) {
                    // TODO : 15초마다 서버 통신
                }
                timerSec++
            }
        }
        val timer = Timer()
        timer.schedule(time, 0, 1000)
    }

    private fun updateTime() {
        val updater = Runnable {
            val minute = if (timerSec / 60 < 1) "00" else "0${(timerSec / 60)}"
            val second =
                if (timerSec % 60 < 10) "0${(timerSec % 60)}" else (timerSec % 60).toString()
            binding.tvMinute.setText("$minute : $second")
        }
        handler.post(updater)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
