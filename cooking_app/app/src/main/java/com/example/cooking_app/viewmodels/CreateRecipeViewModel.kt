package com.example.cooking_app.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.database.MyRecipeDB
import com.example.cooking_app.models.ContentModel
import com.example.cooking_app.models.RecipeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateRecipeViewModel(context: Context,recipeId: Int) : ViewModel() {
    private var _mutableRecipeListModel = MutableLiveData<RecipeModel>()
    val liveRecipeListModel : LiveData<RecipeModel> get() =_mutableRecipeListModel
    private val db = MyRecipeDB.getDatabase(context)

    init {
        if(recipeId>=0) {
            getData(recipeId)


        }else {
            val model = RecipeModel(0, "", null, listOf(ContentModel(0, "", null)))
            _mutableRecipeListModel.value = model
        }

    }

    fun addItem(item: ContentModel) {
        val old = _mutableRecipeListModel.value!!
        val currentList = _mutableRecipeListModel.value!!.contentList?.toMutableList() ?: mutableListOf()
        item.id = currentList.size
        currentList.add(item)
        val new = RecipeModel(old.id,old.title,old.image,currentList)
        _mutableRecipeListModel.value = new

    }
    fun updateItem(item: ContentModel) {
        val old = _mutableRecipeListModel.value!!
        val currentList = _mutableRecipeListModel.value?.contentList!!.toMutableList() ?: mutableListOf()
        currentList[item.id].image = item.image
        val new = RecipeModel(old.id,old.title,old.image,currentList)
        _mutableRecipeListModel.value = new
    }
    fun editTitle(text: String){
        _mutableRecipeListModel.value!!.title = text
    }
    fun updateText(text: String,position: Int) {
        val currentList = _mutableRecipeListModel.value?.contentList!!.toMutableList() ?: mutableListOf()
        currentList[position].title = text

    }
    private fun getData(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val data = db.myRecipeDAO().getData(id)
        val model = RecipeModel(data.id, data.title, null,data.contentList)
        withContext(Dispatchers.Main) {
            _mutableRecipeListModel.value = model
            Log.d("thishits",liveRecipeListModel.value.toString())
        }
    }
    fun saveData(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        CoroutineScope(Dispatchers.IO).launch {
            if(id==-1) {
                db.myRecipeDAO().insert(liveRecipeListModel.value!!)
            }else{
                db.myRecipeDAO().update(liveRecipeListModel.value!!)
            }
        }
    }
}