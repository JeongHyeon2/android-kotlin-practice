package my.app.cooking_app.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import my.app.cooking_app.models.RecipeIngredientForDB
import kotlinx.coroutines.flow.Flow

@Dao
interface MyIngredientDao {
    @Query("SELECT * FROM recipe_ingredient")
    fun getAllData() : List<RecipeIngredientForDB>

    @Query("SELECT * FROM recipe_ingredient")
    fun getAllDataWithFlow() : Flow<List<RecipeIngredientForDB>>

    @Query("SELECT * FROM recipe_ingredient WHERE id = :id")
    fun getOneData(id: String): RecipeIngredientForDB


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : RecipeIngredientForDB)

    @Update
    fun update(item: RecipeIngredientForDB){
    }
    @Query("UPDATE recipe_ingredient SET name = :value1, cost = :value2,  amountOfPurchase = :value3 , calorie = :value4 WHERE id = :itemId")
    fun updateById(itemId: Long, value1:String, value2:String, value3:String, value4:String)


    @Query("DELETE FROM recipe_ingredient WHERE id = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(item: RecipeIngredientForDB)

    @Query("DELETE FROM recipe_ingredient")
    fun deleteAll()
}