package com.example.cooking_app.models

import android.graphics.Bitmap
import androidx.room.Entity


@Entity
data class ContentModel (
    var id : Int,
    var title : String,
    var image: Bitmap? = null,
)