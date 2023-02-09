package com.example.datastoresample_1

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class App : Application() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val EXAMPLE_COUNTER = intPreferencesKey("example_counter")

    companion object {
        lateinit var instance: App
    }

    //객체 생성
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

    }


    //Context -> 어느 엑티비티에서도 컨텍스트를 받아올 수 있음
    fun context(): Context {
        return instance.applicationContext
    }


    suspend fun saveInt(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context().dataStore.edit { preference ->
            preference[dataStoreKey] = value
        }
    }

    suspend fun readInt(key: String): Int? {
        val dataStoreKey = intPreferencesKey(key)
        val preferences = context().dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun saveString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context().dataStore.edit { preference ->
            preference[dataStoreKey] = value
        }
    }

    suspend fun readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context().dataStore.data.first()
        return preferences[dataStoreKey]
    }
}



