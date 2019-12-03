package com.spdigital.weatherapp.util

import android.util.Log
import com.spdigital.weatherapp.data.Country
import java.io.BufferedReader
import java.io.InputStream


object CountryUtils {

    private val TAG = CountryUtils::class.java.simpleName

    var jsonCountriesContent: String? = null


    var mutableListData = mutableListOf<String>()


    fun readCountriesFromStream(stream: InputStream) {
        val reader = BufferedReader(stream.reader())
        val content = StringBuilder()
        reader.use { reader ->
            var line = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
                jsonCountriesContent = line
            }
            jsonCountriesContent = content.toString()
        }
    }

    fun getCountriesObject(): MutableList<String> {

        Log.i(TAG, "jsonCountriesContent. 12132 >>>>>> $jsonCountriesContent")

        return if (mutableListData.isEmpty()) {
            jsonCountriesContent?.let {
                val data =
                    JsonUtils.responseParser(Country.serializer(), it) as? Country

                data?.let{country->
                    country.list.map { x->
                        x.country?.let{c ->
                            mutableListData.add(c)
                        }
                    }
                }
                mutableListData
            } ?: run { mutableListData }
        } else {
            mutableListData
        }
    }

}