package com.example.sogating_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        private val database = Firebase.database
        val userInfoRef = database.getReference("userInfo")
        val userLikeRef = database.getReference("userLike")

    }
}