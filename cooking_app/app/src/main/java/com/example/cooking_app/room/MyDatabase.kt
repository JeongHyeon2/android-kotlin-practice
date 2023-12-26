package com.example.cooking_app.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.utils.FBRef
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [RecipeModelWithId::class], version = 1)
abstract class MyDatabase() : RoomDatabase() {

    abstract fun myDao(): MyDao


    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(
            context: Context
        ): MyDatabase {
            // Firebase Authentication에서 현재 사용자의 UID를 가져옴
            val currentUserUid = FBAuth.getUid()

            return if (INSTANCE?.currentUserUid == currentUserUid) {
                // 이미 존재하는 인스턴스를 반환
                INSTANCE!!
            } else {
                // 현재 사용자의 UID가 변경되었거나 인스턴스가 없는 경우 새로운 인스턴스를 생성
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        currentUserUid
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                    INSTANCE!!.currentUserUid = currentUserUid

                    instance
                }
            }
        }
    }

    private var currentUserUid: String? = null
}
class GsonConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromRecipeModel(model: RecipeModel?): String? {
        return gson.toJson(model)
    }

    @TypeConverter
    fun toRecipeModel(json: String?): RecipeModel {
        return gson.fromJson(json, RecipeModel::class.java)
    }

}