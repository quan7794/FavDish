package com.example.a1.model.database

import androidx.annotation.WorkerThread
import androidx.room.Update
import com.example.a1.MainApplication
import com.example.a1.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish:FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun updateFavDishData(dish: FavDish) {
        favDishDao.updateFavDishData(dish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishes()
    val allFavoriteDishesList: Flow<List<FavDish>> = favDishDao.getFavoriteDishes()
}