package com.example.cooking_app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.model.content.ContentModel
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.utils.FBRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRecipeViewModel() : ViewModel() {
    private var _mutableRecipeListModel = MutableLiveData<RecipeModel>(
        RecipeModel("",
            mutableListOf(""),""
        )
    )
    val liveRecipeListModel : LiveData<RecipeModel> get() =_mutableRecipeListModel

    fun addItem(item: String) {
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.recipes
        newList.add(item)
        val new = RecipeModel(old.title,newList,old.image)
        _mutableRecipeListModel.value = new
    }
    fun editTitle(text: String){
        _mutableRecipeListModel.value!!.title = text
    }
    fun updateText(text: String,position: Int) {
        val currentList = _mutableRecipeListModel.value?.recipes!!
        currentList[position] = text
    }
     fun getData(key: String) = viewModelScope.launch(Dispatchers.IO) {
         FBRef.myRecipe.child(key).get().addOnSuccessListener {
             _mutableRecipeListModel.value = it.getValue(RecipeModel::class.java)
         }.addOnFailureListener{
             Log.e("firebase", "Error getting data", it)
         }
    }
    fun deleteItem(){
        if(_mutableRecipeListModel.value!!.recipes.size==1) return
        val old = _mutableRecipeListModel.value!!
        val newList = _mutableRecipeListModel.value!!.recipes
        newList.removeLast()
        val new = RecipeModel(old.title,newList,old.image)
        _mutableRecipeListModel.value = new
    }
}