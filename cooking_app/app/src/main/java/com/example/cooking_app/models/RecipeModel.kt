package com.example.cooking_app.models


import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="my_recipe_table")
data class RecipeModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "image")
    var image: Bitmap? = null,
)

