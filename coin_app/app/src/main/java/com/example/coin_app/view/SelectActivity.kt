package com.example.coin_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coin_app.MainActivity
import com.example.coin_app.R
import com.example.coin_app.databinding.ActivitySelectBinding
import com.example.coin_app.databinding.FragmentIntro1Binding
import com.example.coin_app.view.adapter.SelectRVAdapter

class SelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectBinding
    private val viewModel : SelectViewModel by viewModels()
    private lateinit var selectRVAdapter: SelectRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getCurrentCoinList()
        viewModel.currentPriceResult.observe(this, Observer {
            selectRVAdapter = SelectRVAdapter(this,it)
            binding.coinListRV.adapter = selectRVAdapter
            binding.coinListRV.layoutManager = LinearLayoutManager(this)
        })
        binding.laterTextArea.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            viewModel.setupFirstFlag()
        }
    }
}