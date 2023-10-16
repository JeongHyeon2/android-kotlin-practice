package com.example.mango_contents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val items = mutableListOf<ContentsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bookmarkButton = findViewById<TextView>(R.id.tv_bookmark)
        bookmarkButton.setOnClickListener {
            val intent = Intent(this, BookMarkActivity::class.java)
            startActivity(intent)
        }
        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/XRoMziImmYCC",
                "https://mp-seoul-image-production-s3.mangoplate.com/331247/60039_1596540913676_34054?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "뉴욕택시디저트"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/bKBEWmF8MVGb",
                "https://mp-seoul-image-production-s3.mangoplate.com/705256_1562413869818147.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "동경산책"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/pVydRWGId3d8",
                "https://mp-seoul-image-production-s3.mangoplate.com/sources/web/restaurants/398605/1062290_1606269461021?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "피자보이시나"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/mmhYgR5FVnYH",
                "https://mp-seoul-image-production-s3.mangoplate.com/516849_1579437536418491.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "경원치킨"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/XRoMziImmYCC",
                "https://mp-seoul-image-production-s3.mangoplate.com/331247/60039_1596540913676_34054?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "뉴욕택시디저트"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/bKBEWmF8MVGb",
                "https://mp-seoul-image-production-s3.mangoplate.com/705256_1562413869818147.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "동경산책"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/pVydRWGId3d8",
                "https://mp-seoul-image-production-s3.mangoplate.com/sources/web/restaurants/398605/1062290_1606269461021?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "피자보이시나"
            )
        )

        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/mmhYgR5FVnYH",
                "https://mp-seoul-image-production-s3.mangoplate.com/516849_1579437536418491.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "경원치킨"
            )
        )
        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/L-rthOXENI",
                "https://mp-seoul-image-production-s3.mangoplate.com/567429_1613885396044603.jpg?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "라룬비올렛"
            )
        )
        items.add(
            ContentsModel(
                "https://www.mangoplate.com/restaurants/kduafZiXUT",
                "https://mp-seoul-image-production-s3.mangoplate.com/155404/449685_1597161095276_16783?fit=around|512:512&crop=512:512;*,*&output-format=jpg&output-quality=80",
                "산울림1992"
            )
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        val rvAdapter = RVAdapter(baseContext, items)
        recyclerView.adapter = rvAdapter
        rvAdapter.itemClick = object : RVAdapter.ItemClick {
            override fun onClick(viwe: View, position: Int) {
                val intent = Intent(baseContext, ViewActivity::class.java)
                intent.putExtra("url", items[position].url)
                intent.putExtra("title", items[position].titleText)
                intent.putExtra("imageUrl", items[position].imageUrl)

                startActivity(intent)

            }

        }
        recyclerView.layoutManager = GridLayoutManager(this, 2)

    }
}