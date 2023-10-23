package com.example.solo_life_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.solo_life_app.MainActivity
import com.example.solo_life_app.R
import com.example.solo_life_app.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.btnJoin.setOnClickListener {
            var isValidate = true
            val email = binding.etEmail.text.toString()
            val pwd1 = binding.etPwd1.text.toString()
            val pwd2 = binding.etPwd2.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요!", Toast.LENGTH_SHORT).show()
                isValidate = false
            }
            if (pwd1.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
                isValidate = false
            }
            if (pwd2.isEmpty()) {
                Toast.makeText(this, "비밀번호 확인을 입력해주세요!", Toast.LENGTH_SHORT).show()
                isValidate = false
            }
            if (pwd1 != pwd2) {
                Toast.makeText(this, "비밀번호 확인이 올바르지 않습니다!", Toast.LENGTH_SHORT).show()
                isValidate = false
            }
            if (pwd1.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리이상 입력해주세요!", Toast.LENGTH_SHORT).show()
                isValidate = false
            }
            if (isValidate) {
                auth.createUserWithEmailAndPassword(email, pwd1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "회원가입 성공.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intent = Intent(this,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {

                            Toast.makeText(
                                baseContext,
                                "회원가입 실패",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }
            }
        }


    }
}