package com.spdigital.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.spdigital.weatherapp.adapters.WeatherListAdapter
import com.spdigital.weatherapp.databinding.ActivityMainBinding
import com.spdigital.weatherapp.util.LocationQueue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherListAdapter: WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        btnSearch.setOnClickListener{
            val searchLocation = etSearchLocation.text.toString()
            LocationQueue.addLocationToQueue(searchLocation)

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

    }
}
