package com.example.solo_life_app.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.solo_life_app.R

import com.example.solo_life_app.databinding.ActivityBoardWriteBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)
        binding.btnWrite.setOnClickListener {
            val title = binding.etTitle.text.toString()
             val content = binding.etContent.text.toString()
            val uid = FirebaseAuth.getUid()

            FirebaseRef.boardRef.push().setValue(BoardModel(title,content,uid,FirebaseAuth.getTime()))
            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_SHORT).show()
            finish()


        }



    }
}