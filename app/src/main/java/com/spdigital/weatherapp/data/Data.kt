package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    var ClimateAverages: List<ClimateAverage>?=null,
    var current_condition: List<CurrentCondition>?=null,
    var request: List<Request>?=null,
    var weather: List<Weather>?=null,
    var error: List<Error>?=null
)