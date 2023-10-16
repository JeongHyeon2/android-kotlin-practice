package com.example.mango_contents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        auth = Firebase.auth

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPwd = findViewById<EditText>(R.id.et_email)
        val btnJoin = findViewById<Button>(R.id.btn_join)

        btnJoin.setOnClickListener { auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPwd
            .text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Join", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            } }
    }
}