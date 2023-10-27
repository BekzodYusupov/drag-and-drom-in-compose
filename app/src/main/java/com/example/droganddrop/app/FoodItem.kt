package com.example.droganddrop.app

import androidx.annotation.DrawableRes
import com.example.droganddrop.R

//data class FoodItem(val id: Int, val name: String, val price: Double, @DrawableRes val image: Int)



data class Person(val id: Int, val name: String, @DrawableRes val profile: Int)

val persons = listOf(
    Person(1, "Jhone", R.drawable.user_one),
    Person(2, "Eyle", R.drawable.user_two),
    Person(3, "Tommy", R.drawable.user_three),
)