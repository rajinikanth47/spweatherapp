package com.spdigital.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.spdigital.weatherapp.databinding.ActivityMainBinding
import com.spdigital.weatherapp.util.LocationQueue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        btnSearch.setOnClickListener{
            val searchLocation = etSearchLocation.text.toString()
            LocationQueue.addLocationToQueue(searchLocation)

            binding.hasSavedLocations = false

            println("Location List ${LocationQueue.getLocationQueueAsList()}")
        }

    }
}
