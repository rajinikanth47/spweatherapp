package com.spdigital.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.workers.FetchLocalWeatherWorker

class LocalWeatherViewModel(application: Application) : AndroidViewModel(application){

    var mContext = application.applicationContext
    val liveWorkRequest  = MutableLiveData<OneTimeWorkRequest>()



    fun fetchLocalWeatherData(country: String,isBulkLoad:Boolean = false,isBulkLastItem:Boolean=false){
        val data = workDataOf(Constants.KEY_INPUT_DATA to country)
        val workRequest = OneTimeWorkRequestBuilder<FetchLocalWeatherWorker>()
            .setInputData(data)
            .build()

        liveWorkRequest.value = workRequest

        workRequest?.let{
            WorkManager.getInstance(mContext).enqueue(it).result
        }
    }




}