package com.example.cooking_app.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.databinding.ActivityJoinBinding
import com.example.cooking_app.viewmodels.JoinViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions



class JoinActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinBinding
    private lateinit var viewModel : JoinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, com.example.cooking_app.R.layout.activity_join)

        viewModel = ViewModelProvider(this)[JoinViewModel::class.java]
        binding.vm = viewModel


    }

}