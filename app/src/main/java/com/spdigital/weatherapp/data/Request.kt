package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val query: String,
    val type: String
)