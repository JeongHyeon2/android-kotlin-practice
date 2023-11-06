package com.example.sogating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sogating_app.R
import com.example.sogating_app.auth.IntroActivity
import com.example.sogating_app.message.MyLikeListActivity
import com.example.sogating_app.message.MyMsgActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val btn = findViewById<Button>(R.id.myPageButton)
        btn.setOnClickListener {
            startActivity(Intent(this,MyPageActivity::class.java))
        }
        val myMsg = findViewById<Button>(R.id.myMsg)
        myMsg.setOnClickListener { startActivity(Intent(this,MyMsgActivity::class.java)) }
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this,IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        findViewById<Button>(R.id.matchingListButton).setOnClickListener {
            startActivity(Intent(this,MyLikeListActivity::class.java))
        }
    }
}