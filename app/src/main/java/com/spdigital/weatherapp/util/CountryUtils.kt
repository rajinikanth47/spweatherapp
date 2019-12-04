package com.spdigital.weatherapp.util

import com.spdigital.weatherapp.data.Country
import java.io.BufferedReader
import java.io.InputStream


object CountryUtils {

    var jsonCountriesContent: String? = null
    private var mutableListData = mutableListOf<String>()

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