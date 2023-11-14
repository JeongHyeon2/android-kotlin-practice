package com.example.coin_app.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coin_app.dataModel.CurrentPrice
import com.example.coin_app.dataModel.CurrentPriceResult
import com.example.coin_app.data_store.MyDataStore
import com.example.coin_app.db.entity.InterestCoinEntity
import com.example.coin_app.repository.DBRepository
import com.example.coin_app.repository.NetworkRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SelectViewModel: ViewModel() {
    private val networkRepository = NetworkRepository()
    private lateinit var currentPriceResultList: ArrayList<CurrentPriceResult>
    private val dbRepository = DBRepository()
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    private val _saved = MutableLiveData<String>()
    val save : LiveData<String> get() = _saved
    val currentPriceResult: LiveData<List<CurrentPriceResult>> get() = _currentPriceResult

    fun getCurrentCoinList() = viewModelScope.launch {
        val result = networkRepository.getCurrentCoinList()
        currentPriceResultList = ArrayList()
        for (coin in result.data) {
            try {
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data[coin.key])
                val gsonFromJson = gson.fromJson(gsonToJson, CurrentPrice::class.java)

                val currentPriceResult = CurrentPriceResult(coin.key, gsonFromJson)
                currentPriceResultList.add(currentPriceResult)
            } catch (e: Exception) {
                Timber.e(e.toString())
            }
        }
        _currentPriceResult.value = currentPriceResultList
    }

    fun setupFirstFlag() = viewModelScope.launch {
        MyDataStore().setUpFirstData()
    }

    fun saveSelectedCoinList(selectedCoinList: ArrayList<String>) =
        viewModelScope.launch(Dispatchers.IO) {
            for (coin in currentPriceResultList) {
                val selected = selectedCoinList.contains(coin.coinName)
                val interestCoinEntity = InterestCoinEntity(
                    0,
                    coin.coinName,
                    coin.coinInfo.opening_price,
                    coin.coinInfo.closing_price,
                    coin.coinInfo.min_price,
                    coin.coinInfo.max_price,
                    coin.coinInfo.units_traded,
                    coin.coinInfo.acc_trade_value,
                    coin.coinInfo.prev_closing_price,
                    coin.coinInfo.units_traded_24H,
                    coin.coinInfo.acc_trade_value_24H,
                    coin.coinInfo.fluctate_24H,
                    coin.coinInfo.fluctate_rate_24H,
                    selected
                )
                interestCoinEntity.let {
                    dbRepository.insertInterestCoinData(it)
                }
            }
            // 이 value가 done 이 되어야 Main으로 넘어가짐. 근데 with이거 안해주면 백그라운드 쓰레드에 접근했다고 에러 발생
            withContext(Dispatchers.Main){
                _saved.value = "done"
            }

        }

}