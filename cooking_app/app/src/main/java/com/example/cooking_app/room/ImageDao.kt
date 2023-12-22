package com.example.cooking_app.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.cooking_app.models.RecipeModelWithId

@Dao
@TypeConverters(ImageConverter::class)
interface ImageDao {
    @Query("SELECT * FROM image_table WHERE id = :id")
    fun getOneData(id: String): ImageEntity

    @Query("SELECT * FROM image_table")
    fun getAllData() : List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : ImageEntity)

    @Update
    fun update(item: ImageEntity)

    @Query("DELETE FROM image_table WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM image_table")
    fun deleteAll()
}