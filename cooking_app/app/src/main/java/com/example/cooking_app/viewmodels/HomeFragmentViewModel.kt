package com.example.cooking_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cooking_app.models.RecipeModelWithId

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private var _mutableLoadingState =
        MutableLiveData<Boolean>(false)
    val liveLoadingState : MutableLiveData<Boolean> get() = _mutableLoadingState
    fun setTrue(){
        _mutableLoadingState.value = true
    }
    fun setFalse(){
        _mutableLoadingState.value = false

    }
}