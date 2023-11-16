package com.example.coin_app.network.model

import com.example.coin_app.dataModel.RecentPriceData

data class RecentCoinPriceList (

    val status : String,
    val data : List<RecentPriceData>

)