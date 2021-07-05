package com.example.a1.viewmodel

import androidx.lifecycle.*
import com.example.a1.model.database.FavDishRepository
import java.lang.IllegalArgumentException

class FavoriteDishesViewModel(repository: FavDishRepository) : ViewModel() {

    val favoriteList = repository.allFavoriteDishesList.asLiveData()
}


@Suppress("UNCHECKED_CAST")
class FavoriteDishesViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteDishesViewModel::class.java)) {
            return FavoriteDishesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}