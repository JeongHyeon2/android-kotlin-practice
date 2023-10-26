package com.example.solo_life_app.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.solo_life_app.R
import com.example.solo_life_app.databinding.ActivityBoardInsideBinding
import com.example.solo_life_app.util.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
//        val title = intent.getStringExtra("title")
//        val content = intent.getStringExtra("content")
//        val time = intent.getStringExtra("time")
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text=time
        val key = intent.getStringExtra("key")
        getBoardData(key.toString())
        getImageData(key.toString())

    }

    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.jpg")

        val image = binding.getImageArea
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this).load(task.result).into(image)
            }
        })
    }

    private fun getBoardData(key: String) {
        FirebaseRef.boardRef.child(key).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.textArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.title
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}