package com.example.coin_app.view.intro

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import com.example.coin_app.MainActivity
import com.example.coin_app.R
import com.example.coin_app.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private val viewModel: IntroViewModel by viewModels()
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.checkFirstFlag()
        viewModel.first.observe(this, Observer {
            if (it) {
                //처음 접속한 유저가 아니면
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                binding.animationView.visibility = View.INVISIBLE
                binding.fragmentContainerView.visibility = View.VISIBLE

            }
        })
    }
}