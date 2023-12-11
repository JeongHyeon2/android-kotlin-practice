package com.example.cooking_app.models


import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cooking_app.database.ContentModelConverters
import com.example.cooking_app.database.MyBitmapConverter


@Entity(tableName="my_recipe_table")
@TypeConverters(MyBitmapConverter::class)
data class RecipeModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "image")
    var image: Bitmap? = null,
    @ColumnInfo(name = "content_list")
    var contentList : List<ContentModel>
)

