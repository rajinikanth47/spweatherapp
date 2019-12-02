package com.spdigital.weatherapp.viewmodel

import com.spdigital.weatherapp.data.WeatherDisplayItem
import android.widget.ImageView
import com.spdigital.weatherapp.workers.DownloadImageWorker


class WeatherItemInfoViewModel(
    item: WeatherDisplayItem
) {
    private val i = item

    fun getLocation(): String {
        return i.locationCondition?.temp_C ?: "NA"
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