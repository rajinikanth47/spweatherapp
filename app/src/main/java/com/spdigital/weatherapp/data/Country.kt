package com.spdigital.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val list: List<X>
)
@Serializable
data class X(
    val country: String?=null
)