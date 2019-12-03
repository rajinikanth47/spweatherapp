package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class LocationList (
    val location: MutableList<WeatherDisplayItem>
)