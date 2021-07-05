package com.example.a1.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.a1.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("Select * from fav_dishes_table order by id")
    fun getAllDishes(): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishData(dish: FavDish)

    @Query("Select * from fav_dishes_table where favorite_dish = 1")
    fun getFavoriteDishes(): Flow<List<FavDish>>
}