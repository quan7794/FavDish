package com.example.a1.utils

interface SelectedItem {
    fun onSelected(item: String, selection: String)
}

object Constants {
    const val DISH_TYPE = "DishType"
    const val DISH_CATEGORY = "DishCategory"
    const val DISH_COOKING_TIME = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL ="Local"
    const val DISH_IMAGE_SOURCE_REMOTE ="Online"

    const val DISH_UPDATE = "updateDish"
    const val API_ENDPOINT = "recipes/random"

    const val API_KEY = "apiKey"
    const val LIMIT_LICENSE = "limitLicense"
    const val TAGS = "tags"
    const val NUMBER = "number"

    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY_VALUE = "ad1713e8c06b4d5b8f8ce6cb4b26d27c"
    const val NUMBER_VALUE = 1

    fun getDishTypes() :ArrayList<String> {
        val dishTypes = ArrayList<String>()

        dishTypes.add("breakfast")
        dishTypes.add("lunch")
        dishTypes.add("snacks")
        dishTypes.add("dinner")
        dishTypes.add("breakfast")
        dishTypes.add("lunch")
        dishTypes.add("snacks")
        dishTypes.add("dinner")
        dishTypes.add("salad")
        dishTypes.add("side dish")
        dishTypes.add("dessert")
        dishTypes.add("other")
        return  dishTypes
    }

    fun getDishCategories() :ArrayList<String> {
        val dishCategories = ArrayList<String>()

        dishCategories.add("Pizza")
        dishCategories.add("BQQ")
        dishCategories.add("Bakery")
        dishCategories.add("Burger")
        dishCategories.add("Cafe")
        dishCategories.add("Chicken")
        dishCategories.add("Drinks")
        dishCategories.add("Hot Dogs")
        dishCategories.add("Juices")
        dishCategories.add("Sandwich")
        dishCategories.add("Tea & Coffee")
        dishCategories.add("Wraps")
        dishCategories.add("Other")
        return  dishCategories
    }

    fun getDishCookTime() :ArrayList<String> {
        val dishCookTimes = ArrayList<String>()

        dishCookTimes.add("10")
        dishCookTimes.add("15")
        dishCookTimes.add("20")
        dishCookTimes.add("30")
        dishCookTimes.add("45")
        dishCookTimes.add("50")
        dishCookTimes.add("60")
        dishCookTimes.add("90")
        dishCookTimes.add("120")
        dishCookTimes.add("150")
        dishCookTimes.add("180")
        return  dishCookTimes
    }
}