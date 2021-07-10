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

    @WorkerThread
    suspend fun deleteDish(dish: FavDish) {
        favDishDao.deleteDish(dish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishes()
    val allFavoriteDishesList: Flow<List<FavDish>> = favDishDao.getFavoriteDishes()
    fun getDishesByFilter(key: String, value: String): Flow<List<FavDish>> = favDishDao.getDishByFilter(key, value)
}