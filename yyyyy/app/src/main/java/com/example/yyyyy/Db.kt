package com.example.yyyyy

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@Database(entities = [TestEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {
    abstract fun imageDao(): Dao
    companion object {
        @Volatile
        private var INSTANCE: Db? = null
        fun getDatabase(
            context: Context
        ): Db {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Db::class.java,
                    "database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromByteArray(value: ByteArray): String {
        return Base64.getEncoder().encodeToString(value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toByteArray(value: String): ByteArray {
        return Base64.getDecoder().decode(value)
    }
}