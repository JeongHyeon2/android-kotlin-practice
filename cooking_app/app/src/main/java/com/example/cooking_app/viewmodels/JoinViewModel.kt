package com.example.cooking_app.viewmodels

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.utils.App
import com.example.cooking_app.views.IntroActivity
import com.example.cooking_app.views.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinViewModel : ViewModel() {
    var email = MutableLiveData("")
    var pwd = MutableLiveData("")
    var pwdCheck = MutableLiveData("")
    private var auth: FirebaseAuth = Firebase.auth
    private val context = App.context()

    private fun checkValidator(): Boolean {

        if (email.value == "" || pwd.value == "" || pwdCheck.value == "") {
            Toast.makeText(context, "값을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.value!!.contains(" ") || pwd.value!!.contains(" ") || pwdCheck.value!!.contains(" ")) {
            Toast.makeText(context, "값에는 공백을 포함할 수 없습니다", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd.value != pwdCheck.value) {
            Toast.makeText(context, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.value!!.matches("^[A-Za-z0-9](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex())) {
            Toast.makeText(context, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pwd.value!!.length < 6) {
            Toast.makeText(context, "비밀번호는 6자리 이상이어야 합니다", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun onClickJoinBtn() {
        if (checkValidator()) {
            auth.createUserWithEmailAndPassword(email.value.toString(), pwd.value.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(context, "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }
    }
}