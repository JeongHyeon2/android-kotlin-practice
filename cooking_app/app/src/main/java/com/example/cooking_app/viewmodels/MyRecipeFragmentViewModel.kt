package com.example.cooking_app.viewmodels

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
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
    private fun deleteItem(position:Int) = viewModelScope.launch (Dispatchers.IO){
        db.myRecipeDAO().deleteOne(_mutableRecipeListModel.value!![position].id)
        getData()
    }
    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        _mutableRecipeListModel.postValue(db.myRecipeDAO().getAllData())

    }
    fun deleteDialog(position: Int,thisContext : Context){
        val builder = AlertDialog.Builder(thisContext)
        builder.setTitle("삭제 확인")
            .setMessage("정말 "+_mutableRecipeListModel.value!![position].title+"을/를 삭제하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    deleteItem(position)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.show()
    }
}