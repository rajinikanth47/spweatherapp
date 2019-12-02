package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val ClimateAverages: List<ClimateAverage>,
    val current_condition: List<CurrentCondition>,
    val request: List<Request>,
    val weather: List<Weather>
)