package com.example.cooking_app.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.example.cooking_app.R
import com.example.cooking_app.databinding.ActivityDialogBinding

class DialogActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dialog)
        binding.dialogButtonSave.setOnClickListener {

        }
    }
}