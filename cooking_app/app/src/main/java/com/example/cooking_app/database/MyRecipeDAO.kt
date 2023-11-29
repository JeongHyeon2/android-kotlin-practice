package com.example.cooking_app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cooking_app.models.RecipeModel
@Dao
interface MyRecipeDAO {
    @Query("SELECT * FROM my_recipe_table")
    fun getAllData() : List<RecipeModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: RecipeModel)

    @Query("DELETE from my_recipe_table")
    fun deleteAll()
}