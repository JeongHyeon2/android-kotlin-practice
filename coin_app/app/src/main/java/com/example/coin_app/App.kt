package com.example.coin_app

import android.app.Application
import android.content.Context
import timber.log.Timber

class App:Application() {
    init {
        instance = this
    }
    // context를 받아오기 위함
    companion object{
        private var instance : App?=null
        fun context(): Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }
}