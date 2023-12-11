package com.example.cooking_app.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.models.RecipeListModel
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRecipeFragmentViewModel(application: Application) : AndroidViewModel(application){
    private var _mutableRecipeListModel = MutableLiveData<RecipeListModel>(
        RecipeListModel(
            mutableListOf()
        )
    )
    val liveRecipeListModel : LiveData<RecipeListModel> get() =_mutableRecipeListModel

    private fun deleteItem(position:Int) = viewModelScope.launch (Dispatchers.IO){

    }
    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        val database = Firebase.database.reference.child(FBAuth.getUid()).child("my_recipe")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val new = mutableListOf<RecipeModel>()

                for(dataModel in dataSnapshot.children){
                    val data =  dataModel.getValue(RecipeModel::class.java)
                    new.add(data!!)
                }
                new.reverse()
                _mutableRecipeListModel.value = RecipeListModel(new)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        database.addValueEventListener(postListener)
      //  _mutableRecipeListModel.postValue(db.myRecipeDAO().getAllData())
    }
    fun deleteDialog(position: Int,thisContext : Context){
//        val builder = AlertDialog.Builder(thisContext)
//        builder.setTitle("삭제 확인")
//            .setMessage("정말 "+_mutableRecipeListModel.value!![position].title+"을/를 삭제하시겠습니까?")
//            .setPositiveButton("확인",
//                DialogInterface.OnClickListener { dialog, id ->
//                    deleteItem(position)
//                })
//            .setNegativeButton("취소",
//                DialogInterface.OnClickListener { dialog, id ->
//                })
//        builder.show()
    }
}