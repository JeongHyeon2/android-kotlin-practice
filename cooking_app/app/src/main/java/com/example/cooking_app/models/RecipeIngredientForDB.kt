package com.example.cooking_app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("recipe_ingredient")
data class RecipeIngredientForDB(
    @PrimaryKey()
    var id : String = "",
    var name: String = "",
    var cost: String = "0",
    var amountOfPurchase: String = "0",
    var calorie: String = "0",
    )