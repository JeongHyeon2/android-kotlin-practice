package com.example.cooking_app.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.cooking_app.models.ContentModel
import com.example.cooking_app.models.RecipeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

@Database(entities = [RecipeModel::class], version = 1)
@TypeConverters(Converters::class,ContentModelConverters::class)
abstract class MyRecipeDB  : RoomDatabase(){
    abstract fun myRecipeDAO() : MyRecipeDAO

    companion object {
        @Volatile
        private var INSTANCE: MyRecipeDB? = null
        fun getDatabase(
            context: Context
        ): MyRecipeDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRecipeDB::class.java,
                    "my_recipe_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
class Converters {

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun toByteArray(bitmap : Bitmap?) : ByteArray?{
        if(bitmap==null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray?) : Bitmap? {
        if(bytes==null) return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
class ContentModelConverters {
    @TypeConverter
    fun fromContentList(contentList: List<ContentModel>?): String? {
        if (contentList == null) return null
        val gson = Gson()
        return gson.toJson(contentList)
    }

    @TypeConverter
    fun toContentList(contentListString: String?): List<ContentModel>? {
        if (contentListString == null) return null
        val gson = Gson()
        val type = object : TypeToken<List<ContentModel>>() {}.type
        return gson.fromJson(contentListString, type)
    }
}