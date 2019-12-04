package com.spdigital.weatherapp.viewmodel

import com.spdigital.weatherapp.data.WeatherDisplayItem
import android.widget.ImageView
import com.spdigital.weatherapp.workers.DownloadImageWorker


class WeatherItemInfoViewModel(
    item: WeatherDisplayItem
) {

    companion object{
        var temperature = "c"
        var temp_f = "\u2109"
        var temp_c = "\u2103"
    }
    private val i = item

    val weatherLocation
        get() = i.location

    val weatherLocationDesc
        get() = i.locationCondition?.weatherDesc?.get(0)?.value?:"Not Available"


    val weatherTemperatureInC
        get() = i.locationCondition?.temp_C

    val weatherTemperatureInF
        get() = i.locationCondition?.temp_F


    val humidity
        get() = "Humidity ${i.locationCondition?.humidity}"

    val observationTime
        get() = i.locationCondition?.observation_time

    fun getTemperature():String{
        return i.locationCondition?.let{
            if(temperature == "c"){
                it.temp_C
            }else{
                it.temp_F
            }
        }?:"NA"

    }

    fun getTemperatureIndicator():String{
        return i.locationCondition?.let{
            if(temperature == "c"){
                temp_c
            }else{
                temp_f
            }
        }?:"NA"
    }


    fun getWeatherIcon(imageView: ImageView) {
        var url = ""
        i.locationCondition?.weatherIconUrl?.let { list ->
            for (item in list) {
                if (item.value != "") {
                    url = item.value
                    break
                }
            }
        }
        if (url != "") {
            DownloadImageWorker(imageView).execute(url)
        }
    }
}