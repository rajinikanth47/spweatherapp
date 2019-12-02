package com.spdigital.weatherapp.viewmodel

import com.spdigital.weatherapp.data.WeatherDisplayItem

class WeatherItemInfoViewModel(item:WeatherDisplayItem) {

    private val loc = item

    val getLocation
        get() = loc.locationCondition?.temp_C?:"NA"
}