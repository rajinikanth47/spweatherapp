package com.spdigital.weatherapp.util

import org.junit.Test

class LocationQueueTest {

    @Test
    fun addLocationTest() {
        val resultCode = LocationQueue.addLocationToQueue("Singapore")
        assert(resultCode == 1)
    }

    @Test
    fun addDuplicateLocationTest() {
        LocationQueue.addLocationToQueue("Singapore")
        val resultCode2 = LocationQueue.isLocationAdded("Singapore")
        assert(resultCode2)
    }

    @Test
    fun resentLocationAsFirstTest() {
        val locationList = listOf("Singapore", "India", "Indonesia")
        locationList.map {
            LocationQueue.addLocationToQueue(it)
        }
        val list = LocationQueue.getLocationQueueAsList()
        assert(list[0].location == "Indonesia")
    }

    @Test
    fun maxSizePopFirstInsertedItemTest() {
        LocationQueue.maxSize = 2
        LocationQueue.resetQueue()
        val locationList = listOf("Singapore", "India", "Indonesia")
        locationList.map {
            LocationQueue.addLocationToQueue(it)
        }
        val list = LocationQueue.getLocationQueueAsList()
        println(list)

        list.map {
            assert(it.location == locationList[0])
        }

    }
}