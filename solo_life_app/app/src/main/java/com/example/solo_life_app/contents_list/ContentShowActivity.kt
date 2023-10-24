package com.example.solo_life_app.contents_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.solo_life_app.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val url = intent.getStringExtra("url")
        val webview= findViewById<WebView>(R.id.webview)
        webview.loadUrl(url.toString())

    }
}