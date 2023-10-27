package com.example.solo_life_app.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.solo_life_app.R
import com.example.solo_life_app.databinding.ActivityBoardEditBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {
    private lateinit var key: String
    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var writerUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)


        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
        binding.btnWrite.setOnClickListener {
            editBoardData(key)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data!!.data)
        }
    }

    private fun getBoardData(key: String) {
        FirebaseRef.boardRef.child(key).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    binding.etTitle.setText(dataModel?.title)
                    binding.contents.setText(dataModel?.content)
                    writerUid = dataModel?.uid.toString()
                } catch (e: Exception) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.jpg")

        val image = binding.imageArea
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this).load(task.result).into(image)
            }
        })
    }

    private fun editBoardData(key: String) {
        FirebaseRef.boardRef.child(key).setValue(
            BoardModel(
                binding.etTitle.text.toString(),
                binding.contents.text.toString(),
                writerUid,
                FirebaseAuth.getTime(),
            )
        )
        Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show()
        finish()

    }
}