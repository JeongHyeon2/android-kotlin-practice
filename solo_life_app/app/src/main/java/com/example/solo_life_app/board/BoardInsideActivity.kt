package com.example.solo_life_app.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.solo_life_app.R
import com.example.solo_life_app.databinding.ActivityBoardInsideBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
//        val title = intent.getStringExtra("title")
//        val content = intent.getStringExtra("content")
//        val time = intent.getStringExtra("time")
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text=time
         key = intent.getStringExtra("key").toString()
        getBoardData(key.toString())
        getImageData(key.toString())
        binding.boardSettingIcon.setOnClickListener {
            showDialog()

        }

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

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("게시글 수정/삭제")
        val dialog = mBuilder.show()
        dialog.findViewById<Button>(R.id.btn_edit)?.setOnClickListener {
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }
        dialog.findViewById<Button>(R.id.btn_delete)?.setOnClickListener {
            FirebaseRef.boardRef.child(key).removeValue()
            val storage = Firebase.storage
            // Create a storage reference from our app
            val storageRef = storage.reference

            // Create a reference to "mountains.jpg" (or your image file name)
            val imageRef = storageRef.child("$key.jpg")

            // Delete the image file
            imageRef.delete()

            Toast.makeText(this,"삭제되었습니다",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getBoardData(key: String) {
        FirebaseRef.boardRef.child(key).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    binding.titleArea.text = dataModel?.title
                    binding.textArea.text = dataModel?.content
                    binding.timeArea.text = dataModel?.time
                    val myUid = FirebaseAuth.getUid()
                    val writerUid = dataModel?.uid
                    if(myUid.equals(writerUid)){
                        binding.boardSettingIcon.isVisible = true
                    }

                }catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}