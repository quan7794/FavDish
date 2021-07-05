package com.example.a1.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a1.model.entities.FavDish

class DetailViewModel : ViewModel() {

    private val _dish = MutableLiveData<FavDish>()
    val dish: LiveData<FavDish>
        get() = _dish

    fun setDetailDish(dish: FavDish) {
        _dish.postValue(dish)
    }

}