package com.example.cooking_app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cooking_app.models.RecipeModel
@Dao
interface MyRecipeDAO {
    @Query("SELECT * FROM my_recipe_table")
    fun getAllData() : List<RecipeModel>

    @Query("SELECT * FROM my_recipe_table WHERE id = :id")
    fun getData(id:Int) : RecipeModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: RecipeModel)

    @Update()
    fun update(entity: RecipeModel)

    @Query("DELETE from my_recipe_table WHERE id = :id")
    fun deleteOne(id:Int)
    @Query("DELETE from my_recipe_table")
    fun deleteAll()
}