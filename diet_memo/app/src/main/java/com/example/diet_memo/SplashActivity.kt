package com.example.diet_memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = Firebase.auth
        try {
            Log.d("SPLASH", auth.currentUser!!.uid)
            Toast.makeText(
                baseContext,
                "이미 비회원 로그인 되어있는 회원입니다",
                Toast.LENGTH_SHORT,
            ).show()
            Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },3000)
        }catch (e : Exception){
            Log.d("SPLASH","회원가입 필요!")
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            baseContext,
                            "비회원 로그인 성공",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Handler().postDelayed({
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        },3000)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "비회원 로그인 실패",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }


    }
}