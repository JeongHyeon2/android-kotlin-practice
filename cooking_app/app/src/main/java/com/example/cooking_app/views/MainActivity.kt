package com.example.cooking_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cooking_app.R
import com.example.cooking_app.databinding.ActivityMainBinding
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MyRecipeFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navController)
    }
    override fun onStart() {
        super.onStart()
        viewModel.getData()
    }

}