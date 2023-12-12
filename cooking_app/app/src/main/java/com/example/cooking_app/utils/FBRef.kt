package com.example.cooking_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        private val database = Firebase.database
        val myRecipe = database.reference.child(FBAuth.getUid()).child("my_recipe")

    }
}