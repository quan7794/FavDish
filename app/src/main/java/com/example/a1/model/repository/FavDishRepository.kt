package com.example.a1.model.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.a1.model.database.FavDishDao
import com.example.a1.model.entities.FavDish
import com.example.a1.model.network.RandomDishAPI
import com.example.a1.utils.Constants
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao, private val networkDishResource: RandomDishAPI) {

    @WorkerThread
    suspend fun getRandomDish() = networkDishResource.getDishes(Constants.API_KEY_VALUE, Constants.NUMBER_VALUE)

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
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