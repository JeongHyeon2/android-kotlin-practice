package com.example.cooking_app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.models.RecipeIngredientForDB
import com.example.cooking_app.room.MyDatabase
import com.example.cooking_app.utils.App
import com.example.cooking_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientFragmentViewModel : ViewModel() {
    private var _mutableIngredients =
        MutableLiveData<MutableList<RecipeIngredientForDB>>(mutableListOf())
    val ingredients: MutableLiveData<MutableList<RecipeIngredientForDB>> get() = _mutableIngredients
    private val _loadingState = MutableLiveData<Boolean>(false)
    private val _isCountState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    val isCountState: LiveData<Boolean> get() = _isCountState
    fun setIsCountStateO(){
        _isCountState.value = ! _isCountState.value!!
    }
    fun setIsCountStateTrue(){
        _isCountState.value = true
    }
    fun setIsCountStateFalse(){
        _isCountState.value = false
    }
    fun getData() {
        _loadingState.value = true
        FBRef.myIngredients.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModelScope.launch(Dispatchers.IO) {

                    val list = mutableListOf<RecipeIngredientForDB>()
                    for (postSnapshot in dataSnapshot.children) {
                        val value = postSnapshot.getValue(RecipeIngredientForDB::class.java)
                        list.add(value!!)
                    }
                    list.reverse()
                    withContext(Dispatchers.Main) {
                        _mutableIngredients.value = list
                        _loadingState.value = false
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 읽기가 취소될 때 처리
                println("Read failed: " + databaseError.toException())
            }

        })
    }

    fun add(item: RecipeIngredient) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = FBRef.myIngredients.push().key
            FBRef.myIngredients.child(id!!).setValue(getRecipeIngredientForDB(id, item))
            withContext(Dispatchers.Main) {
                val newList = _mutableIngredients.value!!
                newList.add(getRecipeIngredientForDB(id, item))
                _mutableIngredients.value = newList
            }
        }


    }

    fun delete(position: Int) = viewModelScope.launch(Dispatchers.IO) {
        FBRef.myIngredients.child(_mutableIngredients.value!![position].id).removeValue()
        withContext(Dispatchers.Main) {
            val newList = _mutableIngredients.value!!
            newList.removeAt(position)
            _mutableIngredients.value = newList
        }
    }


    fun edit(position: Int, item: RecipeIngredient) {

        val newList = _mutableIngredients.value!!
        newList[position] = getRecipeIngredientForDB(newList[position].id, item)
        _mutableIngredients.value = newList

        viewModelScope.launch(Dispatchers.IO) {
            FBRef.myIngredients.child(_mutableIngredients.value!![position].id)
                .setValue(getRecipeIngredientForDB(newList[position].id, item))

        }
    }

    private fun getRecipeIngredientForDB(
        id: String,
        item: RecipeIngredient
    ): RecipeIngredientForDB {
        return RecipeIngredientForDB(id, item.name, item.cost, item.amountOfPurchase, item.calorie)
    }
}