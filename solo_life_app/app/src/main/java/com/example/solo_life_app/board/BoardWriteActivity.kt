package com.example.solo_life_app.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.solo_life_app.R

import com.example.solo_life_app.databinding.ActivityBoardWriteBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
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

            val key = FirebaseRef.boardRef.push().key.toString()

            FirebaseRef.boardRef.child(key).setValue(BoardModel(title,content,uid,FirebaseAuth.getTime()))
            if(binding.imageArea.drawable.constantState != ContextCompat.getDrawable(this, R.drawable.plusbtn)?.constantState) {
                imageUpload(key)
            }
            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK && requestCode==100) {
            binding.imageArea.setImageURI(data!!.data)
        }
    }
    private fun imageUpload(key:String){
        val storage = Firebase.storage
        // Create a storage reference from our app
        val storageRef = storage.reference

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("$key.jpg")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}