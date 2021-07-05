package com.example.a1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.a1.model.database.FavDishRepository
import com.example.a1.model.entities.FavDish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.concurrent.thread

class FavDishViewModel(private val repository: FavDishRepository): ViewModel() {


    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }

    fun update(dish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(dish)
    }
    val allDishesList = repository.allDishesList.asLiveData()


}



@Suppress("UNCHECKED_CAST")
class FavDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
            return FavDishViewModel(repository) as  T
        }
        throw IllegalArgumentException("Unknown ViewModel.")
    }
}