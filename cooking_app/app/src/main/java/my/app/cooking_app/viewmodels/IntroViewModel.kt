package my.app.cooking_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import my.app.cooking_app.utils.MyDataStore
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {

    private val _first = MutableLiveData<Boolean>()
    val first : LiveData<Boolean> get() = _first
    init {
        checkFirstFlag()
    }
    private fun checkFirstFlag()=viewModelScope.launch {
        val getData = MyDataStore().getFirstData()
        _first.value = getData
    }

}