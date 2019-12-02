package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ClimateAverage(
    val month: List<Month>
)