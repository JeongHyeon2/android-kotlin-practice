package com.example.good_words

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class SentenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentence)
        val listview = findViewById<ListView>(R.id.sentenceListView)

        val sentenceAdapter = ListViewAdapter(sentenceList)
        listview.adapter = sentenceAdapter
    }
}