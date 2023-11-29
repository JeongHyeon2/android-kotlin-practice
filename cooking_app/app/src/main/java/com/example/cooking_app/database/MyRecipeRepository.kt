package com.example.cooking_app.database

import android.content.Context
import com.example.cooking_app.models.RecipeModel

class Repository(context: Context) {
    val db = MyRecipeDB.getDatabase(context)
    fun insertRecipe(entity:RecipeModel) = db.myRecipeDAO().insert(entity)
    fun getRecipeList() = db.myRecipeDAO().getAllData()

}