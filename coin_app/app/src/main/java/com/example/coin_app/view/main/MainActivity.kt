package com.example.coin_app.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.coin_app.R
import com.example.coin_app.databinding.ActivityMainBinding
import com.example.coin_app.view.setting.SettingActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d("onCreate")
        binding.setting.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navController)

    }
}