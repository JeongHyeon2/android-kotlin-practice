package com.example.cooking_app.models

data class RecipeModel (
    var title: String ="",
    var recipes : MutableList<String> = mutableListOf(),
    val image : String = "",
)