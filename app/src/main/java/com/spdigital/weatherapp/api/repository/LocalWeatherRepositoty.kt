package com.spdigital.weatherapp.api.repository

import android.util.Log
import com.spdigital.weatherapp.api.LocalWeatherApiService

class LocalWeatherRepository: LocalWeatherApiService() {

    fun getLocalWeatherInfo(location: String) : Any{
        var response:String
        try {
            response = serviceApiCall(location).toString()
        } catch (e: Exception) {
            Log.v(TAG,e.message)
            return e
        }
        return response
    }

    companion object{
        private val TAG = LocalWeatherRepository::class.java.simpleName
    }
}