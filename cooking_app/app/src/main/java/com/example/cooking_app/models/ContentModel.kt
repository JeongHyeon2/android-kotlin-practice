package com.example.cooking_app.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.cooking_app.database.ContentModelConverters
import com.example.cooking_app.database.MyBitmapConverter


@Entity
@TypeConverters(MyBitmapConverter::class, ContentModelConverters::class)
data class ContentModel (
    var id : Int,
    var title : String,
    var image: Bitmap? = null,
)