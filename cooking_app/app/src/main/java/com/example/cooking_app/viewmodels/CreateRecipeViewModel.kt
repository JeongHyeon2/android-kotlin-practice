package com.example.cooking_app.viewmodels

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.model.content.ContentModel
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.utils.FBRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRecipeViewModel() : ViewModel() {
    private var _mutableRecipeListModel = MutableLiveData<RecipeModel>(
        RecipeModel(
            "",
            mutableListOf(""),
            mutableListOf(),
            ""
        )
    )
    val liveRecipeListModel: LiveData<RecipeModel> get() = _mutableRecipeListModel


    fun addItem(item: String) {
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.recipes
        newList.add(item)
        val new = RecipeModel(old.title, newList, old.ingredients, old.image)
        _mutableRecipeListModel.value = new
    }

    fun addIngredient(ingredient: RecipeIngredient) {
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.ingredients
        newList.add(ingredient)
        val new = RecipeModel(old.title, old.recipes, newList, old.image)
        _mutableRecipeListModel.value = new
    }
    fun editIngredient(ingredient: RecipeIngredient,position: Int){
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.ingredients
        newList.set(position,ingredient)
        val new = RecipeModel(old.title, old.recipes, newList, old.image)
        _mutableRecipeListModel.value = new

    }

    fun editTitle(text: String) {
        _mutableRecipeListModel.value!!.title = text
    }

    fun updateText(text: String, position: Int) {
        val currentList = _mutableRecipeListModel.value?.recipes!!
        currentList[position] = text
    }

    fun getData(key: String) = viewModelScope.launch(Dispatchers.IO) {
        FBRef.myRecipe.child(key).get().addOnSuccessListener {
            _mutableRecipeListModel.value = it.getValue(RecipeModel::class.java)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun deleteItem() {
        if (_mutableRecipeListModel.value!!.recipes.size == 1) return
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.recipes
        newList.removeLast()
        val new = RecipeModel(old.title, newList, old.ingredients, old.image)
        _mutableRecipeListModel.value = new
    }

    fun deleteRecipe(index: Int) {
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.ingredients
        newList.removeAt(index)
        val new = RecipeModel(old.title, old.recipes, newList, old.image)
        _mutableRecipeListModel.value = new
    }

    fun getInformation(): String {
        var cost = 0
        var cal = 0
        _mutableRecipeListModel.value!!.ingredients.map {
            try {
                cost += ((it.cost.toDouble() / it.amountOfPurchase.toDouble()) * it.amount.toDouble()).toInt()
                cal += (it.calorie.toDouble() * it.amount.toDouble() / 100).toInt()

            } catch (e: Exception) {
            }
        }
        return "가격: "+cost.toString() + "원 열량: "+cal.toString() +"kcal"

    }
}