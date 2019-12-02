package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfoApiResp(
    val `data`: Data
)