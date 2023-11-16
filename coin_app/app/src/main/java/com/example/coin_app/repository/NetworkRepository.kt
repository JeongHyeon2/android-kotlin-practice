package com.example.coin_app.repository

import com.example.coin_app.network.Api
import com.example.coin_app.network.RetrofitInstance

class NetworkRepository {
    private val client = RetrofitInstance.getInstance().create(Api::class.java)
    suspend fun getCurrentCoinList() = client.getCurrentCoinList()

    suspend fun getInterestCoinPriceData(coin: String) = client.getRecentCoinPrice(coin)
}