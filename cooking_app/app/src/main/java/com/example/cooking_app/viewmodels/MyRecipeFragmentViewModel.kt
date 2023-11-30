package com.example.cooking_app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.database.MyRecipeDB
import com.example.cooking_app.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRecipeFragmentViewModel(application: Application) : AndroidViewModel(application){
    private var _mutableRecipeListModel = MutableLiveData<List<RecipeModel>>()
    val liveRecipeListModel : LiveData<List<RecipeModel>> get() =_mutableRecipeListModel
    val context = getApplication<Application>().applicationContext!!
    private val db = MyRecipeDB.getDatabase(context)

    fun addItem(item: RecipeModel) {
        val currentList = _mutableRecipeListModel.value?.toMutableList() ?: mutableListOf()
        currentList.add(item)
        _mutableRecipeListModel.value = currentList

    }
    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        _mutableRecipeListModel.postValue(db.myRecipeDAO().getAllData())
        Log.d("getalldata",db.myRecipeDAO().getAllData().toString())

    }
}