package my.app.cooking_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

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