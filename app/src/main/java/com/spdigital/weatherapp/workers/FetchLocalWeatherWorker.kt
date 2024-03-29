package com.spdigital.weatherapp.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.spdigital.weatherapp.api.repository.LocalWeatherRepository
import com.spdigital.weatherapp.data.WeatherDisplayItem
import com.spdigital.weatherapp.data.WeatherInfoApiResp
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.util.JsonUtils
import kotlinx.coroutines.coroutineScope

class FetchLocalWeatherWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result = coroutineScope {
        try {

            inputData.getString(Constants.KEY_INPUT_DATA)?.let { loc ->

                when (val result = LocalWeatherRepository().getLocalWeatherInfo(loc)) {
                    is Exception -> Result.failure()
                    else -> {
                        result as String
                        val weatherApiResponse = JsonUtils.responseParser(
                            WeatherInfoApiResp.serializer(),
                            result
                        ) as? WeatherInfoApiResp
                        weatherApiResponse?.let {
                            if (it.data.error == null) {
                                val weatherDisplayItem =
                                    WeatherDisplayItem(loc, it.data.current_condition?.get(0))
                                val stringFormatItem = JsonUtils.toJson(weatherDisplayItem)
                                val d = workDataOf(Constants.KEY_API_RESPONSE to stringFormatItem)
                                Result.success(d)
                            } else {
                                val error = it.data.error?.get(0)?.msg
                                    ?: "error"
                                val weatherDisplayItem =
                                    WeatherDisplayItem(location = loc, errorResp = error)
                                val stringFormatItem = JsonUtils.toJson(weatherDisplayItem)
                                val d = workDataOf(Constants.KEY_API_RESPONSE to stringFormatItem)
                                Result.success(d)
                            }
                        } ?: run {
                            Result.failure()
                        }
                    }
                }
            } ?: run {
                Result.failure()
            }

        } catch (ex: Exception) {
            Result.failure()
        }
    }

    companion object {
        private val TAG = FetchLocalWeatherWorker::class.java.simpleName
    }

}