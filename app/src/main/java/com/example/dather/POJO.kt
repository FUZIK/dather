package com.example.dather

data class Weather(
    val description: String,
    val iconUri: String
)

data class City(
    val name: String,
    val weather: Weather
)
