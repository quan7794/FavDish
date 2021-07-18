package com.example.a1.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a1.model.entities.FavDish

@Database(entities = [FavDish::class], version = 2, exportSchema = true)
abstract class FavDishDatabase : RoomDatabase() {
    abstract fun favDishDao() : FavDishDao

    companion object {
        private const val databaseName = "fav_dish_database"

        @Volatile
        private var INSTANCE: FavDishDatabase? = null

        fun getDatabase(context: Context): FavDishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext, FavDishDatabase::class.java, databaseName
                    ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}