package com.example.coin_app.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coin_app.db.entity.SelectedCoinPriceEntity
import com.example.coin_app.network.model.RecentCoinPriceList
import com.example.coin_app.repository.DBRepository
import com.example.coin_app.repository.NetworkRepository
import timber.log.Timber
import java.util.Calendar
import java.util.Date

class GetCoinPriceRecentContractedWorkManager(val context : Context,workerParameters:WorkerParameters ) :CoroutineWorker(context, workerParameters){
    private val dbRepository = DBRepository()
    private val networkRepository=NetworkRepository()
    override suspend fun doWork(): Result {
        Timber.d("work!!!")
        getAllInterestSelectedCoinData()
        return Result.success()
    }
    suspend fun getAllInterestSelectedCoinData(){
        val selectedList = dbRepository.getAllInterestSelectedCoinData()

        val timeStamp = Calendar.getInstance().time
        for (coinData in selectedList){
           val recentCoinPriceList  =  networkRepository.getInterestCoinPriceData(coinData.coin_name)

            saveSelectedCoinPrice(
                coinData.coin_name,
                recentCoinPriceList,
                timeStamp

            )
        }
    }
    // 3. 관심있는 코인 각각의 가격 변동 정보 DB에 저장
    fun saveSelectedCoinPrice(
        coinName : String,
        recentCoinPriceList: RecentCoinPriceList,
        timeStamp : Date
    ){

        val selectedCoinPriceEntity = SelectedCoinPriceEntity(
            0,
            coinName,
            recentCoinPriceList.data[0].transaction_date,
            recentCoinPriceList.data[0].type,
            recentCoinPriceList.data[0].units_traded,
            recentCoinPriceList.data[0].price,
            recentCoinPriceList.data[0].total,
            timeStamp
        )

        dbRepository.insertCoinPriceData(selectedCoinPriceEntity)

    }
}