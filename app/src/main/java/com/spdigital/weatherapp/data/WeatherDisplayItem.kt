package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDisplayItem(
    val location:String,
    var locationCondition : CurrentCondition?=null
)