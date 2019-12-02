package com.spdigital.weatherapp.util

import com.spdigital.weatherapp.data.WeatherDisplayItem
import java.util.*

object LocationQueue {

    val queue :Deque<WeatherDisplayItem>  = LinkedList()
    var maxSize = 10

    fun addLocationToQueue(location:String) : Int{

        queue.map {
            if(it.location == location)
                return -1
        }
        if(queue.size == maxSize){
            queue.removeLast()
        }
        queue.addFirst(WeatherDisplayItem(location=location))
        return 1
    }

    fun getLocationQueueAsList():MutableList<WeatherDisplayItem>{
        return queue.toMutableList()
    }

    fun resetQueue(){
        queue.clear()
    }

    fun updateDataResult(weatherItem: WeatherDisplayItem?) {

        weatherItem?.let{newItem ->
            queue.map {
                if(newItem.location == it.location){
                    it.locationCondition = newItem.locationCondition
                }
            }
        }

    }

}