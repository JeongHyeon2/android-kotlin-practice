package com.example.sogating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sogating_app.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        findViewById<Button>(R.id.btn_join)
            .setOnClickListener {
                startActivity(Intent(this, JoinActivity::class.java))
            }
        findViewById<Button>(R.id.btn_login)
            .setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
    }
}