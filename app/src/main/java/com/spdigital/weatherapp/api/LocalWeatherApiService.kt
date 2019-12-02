package com.spdigital.weatherapp.api

import org.json.JSONObject
import java.net.URL

open class LocalWeatherApiService{

    val BASE_API = "http://api.worldweatheronline.com/premium/v1/weather.ashx"
    val KEY = "4e95043ca177417ca0765116192811"
    val NUMBER_OF_DAYS = 2
    val FORMAT = "json"


    /**
     * Perform service call
     * @param url
     * @return new String url with given parameters
     */
    fun serviceApiCall(location: String): JSONObject {
        val preparedString = prepareQuery(location)
        val url = URL(preparedString)
        println("URL :: $url")
        val urlConnection = url.openConnection()
        val lines = urlConnection.getInputStream().use { it.bufferedReader().readText() }
        println("lines :: $lines")
        return JSONObject(lines)
    }

    private fun prepareQuery(loc:String): String{
        return "$BASE_API?key=$KEY&q=$loc&num_of_days=$NUMBER_OF_DAYS&tp=3&format=$FORMAT"
    }

}