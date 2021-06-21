package com.example.a1

import android.app.Application
import com.example.a1.model.database.FavDishDatabase
import com.example.a1.model.database.FavDishRepository
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApplication : Application() {
    private val database by lazy {
        FavDishDatabase.getDatabase(this@MainApplication)
    }

    val repository by lazy {
        FavDishRepository(database.favDishDao())
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}