package com.example.sogating_app.utils

import com.google.firebase.auth.FirebaseAuth

class FBAuthUtil {
    companion object {
        private lateinit var auth : FirebaseAuth
        fun getUid() : String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
    }
}