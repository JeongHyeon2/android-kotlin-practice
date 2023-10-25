package com.example.solo_life_app.util

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuth {
    companion object{
        private lateinit var auth: FirebaseAuth
        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

    }
}