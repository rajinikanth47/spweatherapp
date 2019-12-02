package com.spdigital.weatherapp.viewmodel

class WeatherItemInfoViewModel(item:String) {

    private val location = item

    val getLocation
        get() = location
}