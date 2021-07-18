package com.example.a1

import android.app.Application
import com.example.a1.model.database.FavDishDatabase
import com.example.a1.model.repository.FavDishRepository
import com.example.a1.model.network.RandomDishApiService
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApplication : Application() {
    private val database by lazy {
        FavDishDatabase.getDatabase(this@MainApplication)
    }

    private val apiService by lazy {
        RandomDishApiService().apiService
    }

    val repository by lazy {
        FavDishRepository(database.favDishDao(), apiService)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}