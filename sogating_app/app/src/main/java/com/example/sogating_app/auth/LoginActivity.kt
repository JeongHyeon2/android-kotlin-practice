package com.example.sogating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.sogating_app.MainActivity
import com.example.sogating_app.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email_area)
            val pwd = findViewById<TextInputEditText>(R.id.pwd_area)
            // Initialize Firebase Auth
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "로그인 성공",
                            Toast.LENGTH_SHORT,
                        ).show()
                        startActivity(Intent(this,MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            baseContext,
                            "로그인 실패",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}