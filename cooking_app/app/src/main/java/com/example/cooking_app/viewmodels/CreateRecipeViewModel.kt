package com.example.cooking_app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.database.MyRecipeDB
import com.example.cooking_app.models.ContentModel
import com.example.cooking_app.models.RecipeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.FieldPosition

class CreateRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var _mutableRecipeListModel = MutableLiveData<List<ContentModel>>()
    val liveRecipeListModel : LiveData<List<ContentModel>> get() =_mutableRecipeListModel
    val context = getApplication<Application>().applicationContext!!
    private val db = MyRecipeDB.getDatabase(context)

    init {
        addItem(ContentModel(0,""))
    }

    fun addItem(item: ContentModel) {
        val currentList = _mutableRecipeListModel.value?.toMutableList() ?: mutableListOf()
        item.id = currentList.size
        currentList.add(item)
        _mutableRecipeListModel.value = currentList

    }
    fun updateItem(item: ContentModel) {
        val currentList = _mutableRecipeListModel.value?.toMutableList() ?: mutableListOf()
        var i=0
        for(data in currentList){
            if(data.id==item.id){
                currentList[i] = item
                _mutableRecipeListModel.value = currentList
                break
            }
            i++
        }
    }
    fun updateText(text: String,position: Int) {
        val currentList = _mutableRecipeListModel.value?.toMutableList() ?: mutableListOf()
        currentList[position].title = text

    }

}