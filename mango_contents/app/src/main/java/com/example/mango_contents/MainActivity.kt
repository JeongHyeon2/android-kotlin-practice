package com.example.mango_contents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        items.add(ContentsModel("https://www.mangoplate.com/restaurants/kduafZiXUT","https://mp-seoul-image-production-s3.mangoplate.com/155404/449685_1597161095276_16783?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80","산울림1992"))

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        val rvAdapter = RVAdapter(baseContext,items)
            recyclerView.adapter =  rvAdapter
        rvAdapter.itemClick  = object  :RVAdapter.ItemClick{
            override fun onClick(viwe: View, position: Int) {
                val intent = Intent(baseContext,ViewActivity::class.java)
                intent.putExtra("url",items[position].url)
                startActivity(intent)

            }

        }
        recyclerView.layoutManager = GridLayoutManager(this,2)

    }
}