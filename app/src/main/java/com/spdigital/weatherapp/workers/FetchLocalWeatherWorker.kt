package com.spdigital.weatherapp.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.spdigital.weatherapp.api.repository.LocalWeatherRepository
import com.spdigital.weatherapp.data.WeatherDisplayItem
import com.spdigital.weatherapp.data.WeatherInfoApiResp
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.util.JsonUtils
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class FetchLocalWeatherWorker(context: Context,
                              workerParams: WorkerParameters) : CoroutineWorker(context,workerParams) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result = coroutineScope {
      try{

          inputData.getString(Constants.KEY_INPUT_DATA)?.let{loc ->

              when (val result = LocalWeatherRepository().getLocalWeatherInfo(loc)) {
                  is Exception ->   Result.failure()
                  else -> {
                      result as String
                      val weatherApiResponse = JsonUtils.responseParser(WeatherInfoApiResp.serializer(),result) as? WeatherInfoApiResp

                      weatherApiResponse?.let{
                          val weatherDisplayItem = WeatherDisplayItem(loc,it.data.current_condition[0])
                          val stringFormatItem = JsonUtils.toJson(weatherDisplayItem)
                          Log.i(TAG,stringFormatItem)
                          val d = workDataOf(Constants.KEY_API_RESPONSE to stringFormatItem)
                          Result.success(d)

                      }?:run{
                          Log.e(TAG, "Location data error!")
                          Result.failure()
                      }

                  }
              }
          }?:run{
              Log.e(TAG, "Location not provided!!")
              Result.failure()
          }

      }catch (ex:Exception){
          Log.e(TAG, "Error fetching data!!", ex)
          Result.failure()
      }
    }

    companion object {
        private val TAG = FetchLocalWeatherWorker::class.java.simpleName
    }

}