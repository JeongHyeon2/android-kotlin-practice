package com.example.coin_app.network.model

data class CurrentPriceList (
    val status:String,
    val data:Map<String,Any>
)