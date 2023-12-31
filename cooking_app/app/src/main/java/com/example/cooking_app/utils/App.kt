package com.example.cooking_app.utils

import android.app.Application
import android.content.Context


class App : Application() {

    init{
        instance = this
    }

    companion object {
        var instance: App? = null
        fun context() : Context {
            return instance!!.applicationContext
        }
    }

}