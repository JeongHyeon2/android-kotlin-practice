package com.example.coin_app.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coin_app.dataModel.CurrentPrice
import com.example.coin_app.dataModel.CurrentPriceResult
import com.example.coin_app.network.model.CurrentPriceList
import com.example.coin_app.repository.NetworkRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectViewModel : ViewModel() {
    private val networkRepository = NetworkRepository()
    private lateinit var currentPriceResultList : ArrayList<CurrentPriceResult>

    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult:LiveData<List<CurrentPriceResult>> get() = _currentPriceResult

    fun getCurrentCoinList() = viewModelScope.launch {
        val result =networkRepository.getCurrentCoinList()
        currentPriceResultList = ArrayList()
        for(coin in result.data){
            try{
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data[coin.key])
                val gsonFromJson = gson.fromJson(gsonToJson,CurrentPrice::class.java)

                val currentPriceResult = CurrentPriceResult(coin.key,gsonFromJson)
                currentPriceResultList.add(currentPriceResult)
            }catch (e : Exception){
                Timber.e(e.toString())
            }
        }
        _currentPriceResult.value = currentPriceResultList
    }

}