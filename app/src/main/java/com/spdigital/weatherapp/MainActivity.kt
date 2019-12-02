package com.spdigital.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spdigital.weatherapp.util.LocationQueue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch.setOnClickListener{
            val searchLocation = etSearchLocation.text.toString()
            LocationQueue.addLocationToQueue(searchLocation)

            println("Location List ${LocationQueue.getLocationQueueAsList()}")
        }

    }
}
