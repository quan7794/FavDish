package com.example.a1.viewmodel

import androidx.lifecycle.*
import com.example.a1.model.entities.FavDish
import com.example.a1.model.repository.FavDishRepository
import com.example.a1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RandomDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    fun getRandomDishes() = liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getRandomDish()))
        } catch (ex: Exception) {
            emit(Resource.error(null, ex.message?:"Error occurred!!!!!!"))
        }
    }

    fun addRandomDishToFavorite(dish: FavDish?) {
        viewModelScope.launch {
            if (dish != null) {
                repository.insertFavDishData(dish)
            }
        }

    }

}

class RandomDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RandomDishViewModel::class.java))
            return RandomDishViewModel(repository) as T
        throw IllegalArgumentException("Unknown this class!")
    }

}