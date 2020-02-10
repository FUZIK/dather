package com.example.dather.datasource

import com.example.dather.datasource.openweathermap.CitiesRetroRepo

object Repository {
    fun getCitiesRepo() =
        CitiesRetroRepo.instance()
}