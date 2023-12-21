package com.example.cooking_app.utils

import com.google.firebase.auth.FirebaseAuth

class FBAuth {
    companion object {
        private var auth  = FirebaseAuth.getInstance()
        fun getUid() : String {

            return auth.currentUser?.uid.toString()
        }
        fun logout()  {
            auth.signOut()
        }
    }
}