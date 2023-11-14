package com.example.coin_app.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.coin_app.App

class MyDataStore {
    private val context = App.context()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_pref")
    }
    private val mDataStore: DataStore<Preferences> = context.dataStore
    private val FIRST_FLAG = booleanPreferencesKey("FIRST_FLAG")

    suspend fun setUpFirstData() {
        mDataStore.edit { pref -> pref[FIRST_FLAG] = true }
    }

    suspend fun getFirstData(): Boolean {
        var currentValue = false
        mDataStore.edit { pref ->
            currentValue = pref[FIRST_FLAG] ?: false

        }
        return currentValue
    }
}