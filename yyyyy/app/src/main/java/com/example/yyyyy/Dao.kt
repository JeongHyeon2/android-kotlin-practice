package com.example.yyyyy

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    suspend fun insertImage(image: TestEntity)

    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImageById(id: Long): TestEntity

}
