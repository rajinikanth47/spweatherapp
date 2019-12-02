package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDisplayItem(
    val location:String,
    val locationCondition : CurrentCondition?=null
)