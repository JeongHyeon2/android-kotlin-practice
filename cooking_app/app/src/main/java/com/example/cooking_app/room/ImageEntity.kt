package com.example.cooking_app.room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream

@Entity(tableName = "image_table")
@TypeConverters(ImageConverter::class)
data class ImageEntity (
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id : String = "",
    @ColumnInfo(name = "image")
    var image : Bitmap? = null,
)
class ImageConverter {
    @TypeConverter
    fun fromBitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun fromByteArrayToBitmap(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}