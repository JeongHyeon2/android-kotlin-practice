package com.example.yyyyy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class TestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val imageData: ByteArray
)