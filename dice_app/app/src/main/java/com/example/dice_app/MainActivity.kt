package com.example.dice_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dice_app.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.diceStartBtn.setOnClickListener {
            Toast.makeText(this, "Roll!!", Toast.LENGTH_SHORT).show()
            val number1 = Random.nextInt(1, 6)
            val number2 = Random.nextInt(1, 6)
            when (number1) {
                1 -> binding.dice1.setImageResource(R.drawable.dice_1)
                2 -> binding.dice1.setImageResource(R.drawable.dice_2)
                3 -> binding.dice1.setImageResource(R.drawable.dice_3)
                4 -> binding.dice1.setImageResource(R.drawable.dice_4)
                5 -> binding.dice1.setImageResource(R.drawable.dice_5)
                6 -> binding.dice1.setImageResource(R.drawable.dice_6)
            }
            when (number2) {
                1 -> binding.dice2.setImageResource(R.drawable.dice_1)
                2 -> binding.dice2.setImageResource(R.drawable.dice_2)
                3 -> binding.dice2.setImageResource(R.drawable.dice_3)
                4 -> binding.dice2.setImageResource(R.drawable.dice_4)
                5 -> binding.dice2.setImageResource(R.drawable.dice_5)
                6 -> binding.dice2.setImageResource(R.drawable.dice_6)
            }
        }

    }
}