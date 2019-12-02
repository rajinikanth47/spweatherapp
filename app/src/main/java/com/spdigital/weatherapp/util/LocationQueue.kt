package com.spdigital.weatherapp.util

import java.util.*

object LocationQueue {

    val queue :Deque<String>  = LinkedList()
    var maxSize = 10

    fun addLocationToQueue(location:String) : Int{
        queue.map {
            if(it == location)
                return -1
        }
        if(queue.size == maxSize){
            queue.removeLast()
        }
        queue.addFirst(location)
        return 1
    }

    fun getLocationQueueAsList():MutableList<String>{
        return queue.toMutableList()
    }

    fun resetQueue(){
        queue.clear()
    }

}