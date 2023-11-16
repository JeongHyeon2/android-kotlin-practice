package com.example.coin_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coin_app.db.dao.InterestCoinDAO
import com.example.coin_app.db.dao.SelectedCoinPriceDAO
import com.example.coin_app.db.entity.DateConverters
import com.example.coin_app.db.entity.InterestCoinEntity
import com.example.coin_app.db.entity.SelectedCoinPriceEntity


@Database(entities = [InterestCoinEntity::class,SelectedCoinPriceEntity::class], version = 2,exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class CoinPriceDatabase : RoomDatabase() {

    abstract fun interestCoinDAO() : InterestCoinDAO
    abstract fun selectedCoinDAO() : SelectedCoinPriceDAO

    companion object {

        @Volatile
        private var INSTANCE : CoinPriceDatabase? = null

        fun getDatabase(
            context : Context
        ) : CoinPriceDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoinPriceDatabase::class.java,
                    "coin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }


        }

    }


}