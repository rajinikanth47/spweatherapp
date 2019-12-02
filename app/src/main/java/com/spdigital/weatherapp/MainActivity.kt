package com.spdigital.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.spdigital.weatherapp.adapters.WeatherListAdapter
import com.spdigital.weatherapp.databinding.ActivityMainBinding
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.util.LocationQueue
import com.spdigital.weatherapp.viewmodel.LocalWeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var mLocalWeatherViewModel:LocalWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mLocalWeatherViewModel = ViewModelProviders.of(this).get(LocalWeatherViewModel::class.java)


        btnSearch.setOnClickListener{
            val searchLocation = etSearchLocation.text.toString()
            LocationQueue.addLocationToQueue(searchLocation)

            mLocalWeatherViewModel.fetchLocalWeatherData(searchLocation)

            binding.hasSavedLocations = false

            weatherListAdapter = WeatherListAdapter()

            binding.locationList.adapter = weatherListAdapter

            println("Location List ${LocationQueue.getLocationQueueAsList()}")

            val locItemsList = LocationQueue.getLocationQueueAsList()
            if(locItemsList.size>0){
                binding.hasSavedLocations = true
            }
            weatherListAdapter.submitList(locItemsList)

        }

        mLocalWeatherViewModel.liveWorkRequest.observe(this, Observer {
            initWorkerObservers(it.id)
        })

    }

    private fun initWorkerObservers(uuid:UUID){
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uuid)
            .observe(this, Observer { workInfo ->
                // Check if the current work's state is "successfully finished"
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    //displayImage(workInfo.outputData.getString(KEY_IMAGE_URI))
                   println("data >>>>> ${workInfo.outputData.getString(Constants.KEY_API_RESPONSE)}")
                }
            })
    }
}
