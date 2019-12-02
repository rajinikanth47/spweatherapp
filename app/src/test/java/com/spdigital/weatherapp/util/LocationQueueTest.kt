package com.spdigital.weatherapp.util

import org.junit.Test

class LocationQueueTest {

    @Test
    fun addLocationTest(){
        val resultCode = LocationQueue.addLocationToQueue("Singapore")
        assert(resultCode == 1)
    }

    @Test
    fun addDuplicateLocationTest(){
        val locationList = listOf("Singapore", "Singapore")

        for ((index, value) in locationList.withIndex()) {
            val resultCode = LocationQueue.addLocationToQueue(value)
            if(index == locationList.size-1){
                assert(resultCode == -1)
            }
        }
    }

    @Test
    fun resentLocationAsFirstTest(){
        val locationList = listOf("Singapore", "India","Indonesia")
        locationList.map {
            LocationQueue.addLocationToQueue(it)
        }
        val list = LocationQueue.getLocationQueueAsList()
        assert(list[0] == "Indonesia")
    }

    @Test
    fun maxSizePopFirstInsertedItemTest(){
        LocationQueue.maxSize = 2
        LocationQueue.resetQueue()
        val locationList = listOf("Singapore", "India","Indonesia")
        locationList.map {
            LocationQueue.addLocationToQueue(it)
        }
        val list = LocationQueue.getLocationQueueAsList()
        println(list)
        val removedItem = locationList[0]
        assert(!list.contains(removedItem))

    }
}