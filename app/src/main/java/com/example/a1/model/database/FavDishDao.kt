package com.example.a1.model.database

import androidx.room.*
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

    @Delete
    suspend fun deleteDish(dish: FavDish)

    @Query("""Select * from fav_dishes_table where
            case :key
                when 'type' then type
                when 'category' then category
            end
            = :value""")
    fun getDishByFilter(key: String, value: String): Flow<List<FavDish>>
}