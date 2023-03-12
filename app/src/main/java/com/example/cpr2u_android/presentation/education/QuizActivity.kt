package com.example.cpr2u_android.presentation.education

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.ActivityQuizBinding
import com.example.cpr2u_android.presentation.base.BaseActivity

class QuizActivity : BaseActivity<ActivityQuizBinding>(R.layout.activity_quiz) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_quiz)
        return navController.navigateUp(appBarConfiguration) ||
            super.onSupportNavigateUp()
    }
}
