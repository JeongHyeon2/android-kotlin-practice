package com.example.sogating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sogating_app.MainActivity
import com.example.sogating_app.R
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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

                        // Token
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    return@OnCompleteListener
                                }
                                val postListener = object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val data =
                                            dataSnapshot.getValue(UserDataModel::class.java)!!
                                        val token = task.result
                                        val userModel = UserDataModel(
                                            data.uid,
                                            data.nickname,
                                            data.age,
                                            data.gender,
                                            data.city,
                                            token
                                        )
                                        FBRef.userInfoRef.child(FBAuthUtil.getUid())
                                            .setValue(userModel)
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Getting Post failed, log a message
                                    }
                                }
                                FBRef.userInfoRef.child(FBAuthUtil.getUid())
                                    .addValueEventListener(postListener)

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            })

                        startActivity(Intent(this, MainActivity::class.java))
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

    private fun getMyData() {


    }
}