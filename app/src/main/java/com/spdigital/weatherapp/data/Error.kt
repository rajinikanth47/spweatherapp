package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val msg: String
)