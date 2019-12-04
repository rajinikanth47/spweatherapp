package com.spdigital.weatherapp.util

import com.spdigital.weatherapp.data.LocationList
import com.spdigital.weatherapp.data.WeatherDisplayItem
import java.util.*

object LocationQueue {

    val queue :Deque<WeatherDisplayItem>  = LinkedList()
    var maxSize = 10

    fun isLocationAdded(input:String):Boolean{
        queue.map {
            if(it.location == input)
                return true
        }
        return false
    }

    fun addLocationItems(item: LocationList) {
        item.location.map {
            addLocationToQueue(it.location)
        }
    }

    fun addLocationToQueue(location:String) : Int{

//        if(queue.size == maxSize){
//            queue.removeLast()
//        }
        queue.addFirst(WeatherDisplayItem(location=location))
        return 1
    }


    //Allow first 10 records to save
    fun getLocationListForSavingInPreferences() :MutableList<WeatherDisplayItem>{

        val list = queue.toMutableList()
        return if (list.size > 10) {
            list.take(10) as MutableList<WeatherDisplayItem>
        }else{
            list
        }

    }

    fun getLocationQueueAsList():MutableList<WeatherDisplayItem>{
        return queue.toMutableList()
    }

    fun resetQueue(){
        queue.clear()
    }

    fun popLastLocation(){
       queue.removeFirst()
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