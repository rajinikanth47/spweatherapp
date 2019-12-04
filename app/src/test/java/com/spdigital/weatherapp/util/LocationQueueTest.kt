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
    @Test
    fun getLocationListForSavingInPreferencesTest(){
        val locationList = listOf("1", "2", "3","4","5","6","7","8","9","10","11")
        //val locationList1 = listOf("1", "2", "3")
        locationList.map {
            LocationQueue.addLocationToQueue(it)
        }

        val list =  LocationQueue.getLocationListForSavingInPreferences()
        assert(list[9].location == "2" && list[0].location == "11")

    }
}