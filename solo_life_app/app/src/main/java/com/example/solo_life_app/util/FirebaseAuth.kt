package com.example.solo_life_app.util

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FirebaseAuth {
    companion object{
        private lateinit var auth: FirebaseAuth
        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
        fun getTime() : String {
            val curTime = Date().time
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            format.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            return format.format(curTime)
        }


    }
}