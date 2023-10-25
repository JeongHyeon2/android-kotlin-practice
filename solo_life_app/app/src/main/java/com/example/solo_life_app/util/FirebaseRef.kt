package com.example.solo_life_app.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object{
        private val database = Firebase.database
        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")
        val bookMarkRef = database.getReference("bookmark_list")
    }
}