package com.example.cooking_app.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.cooking_app.models.RecipeModelWithId
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(GsonConverter::class)
interface MyDao {

    @Query("SELECT * FROM recipe_model_with_id")
    fun getAllData() : List<RecipeModelWithId>

    @Query("SELECT * FROM recipe_model_with_id")
    fun getAllDataWithFlow() : Flow<List<RecipeModelWithId>>

    @Query("SELECT * FROM recipe_model_with_id WHERE id = :id")
    fun getOneData(id: String): RecipeModelWithId


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : RecipeModelWithId)

    @Update
    fun update(item: RecipeModelWithId)

    @Delete
    fun delete(item: RecipeModelWithId)

    @Query("DELETE FROM recipe_model_with_id")
    fun deleteAll()

}