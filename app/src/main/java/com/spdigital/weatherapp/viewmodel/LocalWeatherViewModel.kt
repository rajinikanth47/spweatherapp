package com.spdigital.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.util.ServiceCallback
import com.spdigital.weatherapp.workers.FetchLocalWeatherWorker
import java.util.*


class LocalWeatherViewModel(application: Application) : AndroidViewModel(application){

    data class TriggerItem(val uuid:UUID,val progressVisibleStatus:Boolean)

    fun fetchLocalWeatherData(country: String, callback: ServiceCallback<TriggerItem>,progressStatus:Boolean=true){
        val data = workDataOf(Constants.KEY_INPUT_DATA to country)
        val workRequest = OneTimeWorkRequestBuilder<FetchLocalWeatherWorker>()
            .setInputData(data)
            .build()
            workRequest.let{
                WorkManager.getInstance(getApplication()).enqueue(it)
            }
        callback.onSuccess(TriggerItem(workRequest.id,progressStatus))
    }

}