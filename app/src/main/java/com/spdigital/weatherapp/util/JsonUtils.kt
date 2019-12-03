package com.spdigital.weatherapp.util

import com.spdigital.weatherapp.data.LocationList
import com.spdigital.weatherapp.data.WeatherDisplayItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JSON

object JsonUtils {

    fun responseParser(resClass: KSerializer<*>, result: String?):Any? {
        return  result?.let{
            JSON.parse(resClass, result)
        }
    }

    fun toJson(field: WeatherDisplayItem): String {
        return JSON.stringify(WeatherDisplayItem.serializer(), field)
    }

    fun toJson(field: LocationList): String {
        return JSON.stringify(LocationList.serializer(), field)
    }

}