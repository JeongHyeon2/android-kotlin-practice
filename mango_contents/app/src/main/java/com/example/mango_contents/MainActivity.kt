package com.example.mango_contents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val items = mutableListOf<ContentsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for(i in 1..10) {
            items.add(
                ContentsModel(
                    "https://www.mangoplate.com/restaurants/L-rthOXENI",
                    "https://mp-seoul-image-production-s3.mangoplate.com/567429_1613885396044603.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                    "라룬비올렛"
                )
            )
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = RVAdapter(baseContext,items)
        recyclerView.layoutManager = GridLayoutManager(this,2)

    }
}