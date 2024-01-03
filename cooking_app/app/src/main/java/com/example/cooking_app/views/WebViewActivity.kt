package com.example.cooking_app.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.cooking_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()


        webView.loadUrl("https://www.dietshin.com/calorie/calorie_main.asp")
        findViewById<FloatingActionButton>(R.id.fab_button).setOnClickListener {
            val idx = extractIdxFromUrl(webView.url.toString())
            if (idx != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("link", webView.url)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }else{
                Toast.makeText(this,"재료를 선택해주세요",Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun extractIdxFromUrl(url: String): Int? {
        val idxRegex = Regex("""idx=(\d+)""")
        val matchResult = idxRegex.find(url)
        return matchResult?.groupValues?.getOrNull(1)?.toIntOrNull()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}