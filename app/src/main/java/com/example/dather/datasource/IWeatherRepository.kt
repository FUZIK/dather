package com.example.dather.datasource

import androidx.lifecycle.LiveData
import com.example.dather.City

const val DEFAULT_LIMIT = 20
const val DEFAULT_LANGUAGE = "ru"

interface IWeatherRepository {
    fun getCity(latitude: Double,
                longitude: Double,
                lang: String = DEFAULT_LANGUAGE
    ): LiveData<City>

    fun getCitiesAround(latitude: Double,
                        longitude: Double,
                        limit: Int = DEFAULT_LIMIT,
                        lang: String = DEFAULT_LANGUAGE
    ): LiveData<List<City>>
}