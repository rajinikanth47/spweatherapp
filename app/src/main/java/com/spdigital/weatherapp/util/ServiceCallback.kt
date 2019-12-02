package com.spdigital.weatherapp.util

interface ServiceCallback<T> {
    fun onSuccess(response: T?)
    fun onError(error: Throwable)
}