package com.example.cooking_app.viewmodels

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cooking_app.utils.App
import com.example.cooking_app.views.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    var email = MutableLiveData("")
    var pwd = MutableLiveData("")
    private var auth: FirebaseAuth = Firebase.auth
    private val context = App.context()
    fun onLoginButtonClick() {
        auth.signInWithEmailAndPassword(email.value!!, pwd.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "이메일 혹은 패스워드가 올바르지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }

    }
}