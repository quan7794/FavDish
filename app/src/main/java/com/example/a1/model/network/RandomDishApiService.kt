package com.example.a1.model.network

import com.example.a1.utils.Constants
import com.example.a1.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RandomDishApiService {

    private fun getRetrofit() = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: RandomDishAPI = getRetrofit().create(RandomDishAPI::class.java)

}