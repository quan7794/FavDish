package com.example.a1.model.network

import com.example.a1.model.entities.RandomFavDish
import com.example.a1.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishAPI {

    @GET(Constants.API_ENDPOINT)
    suspend fun getDishes(
        @Query(Constants.API_KEY) apiKey: String, @Query(Constants.NUMBER) numberDish: Int
    ): RandomFavDish
}