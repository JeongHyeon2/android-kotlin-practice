package com.example.coin_app.network

import com.example.coin_app.network.model.CurrentPriceList
import retrofit2.http.GET

interface Api {
    @GET("public/ticker/ALL_KRW")
    suspend fun getCurrentCoinList(): CurrentPriceList
}