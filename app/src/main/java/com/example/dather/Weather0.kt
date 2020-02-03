package com.example.dather

enum class WDirection0 {
    EAST,
    NORTH,
    SOUTH,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST
}

data class Weather0(
    val description: String,
    val windSpeed: Long,
    val windDirect: WDirection0,
    val clouds: Int,
    val pressure: Int,
    val owmIconUri: String? = null
)