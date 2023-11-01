package com.example.sogating_app.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sogating_app.MainActivity
import com.example.sogating_app.R
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private var nickname=""
    private var gender = ""
    private var city = ""
    private var age =""
    private var uid=""
    private lateinit var imageView : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        auth = Firebase.auth

         imageView = findViewById(R.id.image_area)
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri->
                imageView.setImageURI(uri)
            }
        )
        imageView.setOnClickListener {
            getAction.launch("image/*")
        }

        findViewById<Button>(R.id.btn_register).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email_area)
            val pwd = findViewById<TextInputEditText>(R.id.pwd_area)


            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.regionArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()
            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()

            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    val user = auth.currentUser
                    uid = user?.uid.toString()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "회원가입 성공",
                            Toast.LENGTH_SHORT,
                        ).show()
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                             val token = task.result
                                val user = UserDataModel(uid,nickname, age, gender, city,token)
                                FBRef.userInfoRef.child(uid).setValue(user)
                                uploadImage(uid)
                            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                        })

                        startActivity(Intent(this,MainActivity::class.java))


                    } else {

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }
    }
    private fun uploadImage(uid:String){
        val storage = Firebase.storage
        val storageRef = storage.reference.child("$uid.png")
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.d("dddd","FAIL")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d("dddd","addOnSuccessListener")

        }
    }
}