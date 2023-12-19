package com.example.cooking_app.viewmodels

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRecipeFragmentViewModel(application: Application) : AndroidViewModel(application){
    private var _mutableRecipeListModel = MutableLiveData<MutableList<RecipeModelWithId>>(mutableListOf())
    val liveRecipeListModel : LiveData<MutableList<RecipeModelWithId>> get() =_mutableRecipeListModel

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private fun deleteItem(key:String) = viewModelScope.launch (Dispatchers.IO){
        FBRef.myRecipe.child(key).removeValue()
    }
    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        _loadingState.postValue(true) // 로딩 상태 시작
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val new = mutableListOf<RecipeModelWithId>()
                for(dataModel in dataSnapshot.children){
                    val data =  dataModel.getValue(RecipeModel::class.java)
                    new.add(RecipeModelWithId(dataModel.key.toString(),data!!))
                }
                new.reverse()
                _mutableRecipeListModel.value = new
                _loadingState.postValue(false) // 로딩 상태 종료
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FBRef.myRecipe.addListenerForSingleValueEvent(postListener)
    }
    fun deleteDialog(position : Int,item: RecipeModelWithId,thisContext : Context){
        val builder = AlertDialog.Builder(thisContext)
        builder.setTitle("삭제 확인")
            .setMessage("정말 "+item.model.title+"을/를 삭제하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    deleteItem(item.id)
                    val list = _mutableRecipeListModel.value!!
                    list.removeAt(position)
                    _mutableRecipeListModel.value = list

                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.show()
    }
}