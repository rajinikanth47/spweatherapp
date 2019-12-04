package com.spdigital.weatherapp.util

import com.spdigital.weatherapp.data.Country
import com.spdigital.weatherapp.data.LocationList
import com.spdigital.weatherapp.data.WeatherDisplayItem
import org.junit.Test

class JsonUtilsTest {

    @Test
    fun responseParserTest(){
        val obj = JsonUtils.responseParser(Country.serializer(),sampleJson) as Country
        assert(obj.list.isNotEmpty())
    }


    @Test
    fun toJsonLocationListTest(){

        val item1 = WeatherDisplayItem(location = "Singapore")
        val item2 = WeatherDisplayItem(location = "India")

        var list = mutableListOf<WeatherDisplayItem>()
        list.add(item1)
        list.add(item2)

        val locationList = LocationList(list)
        val s = JsonUtils.toJson(locationList)

        assert(s.isNotBlank())

    }



    @Test
    fun toJsonWeatherItemListTest(){
        val weatherDisplayItem =
            WeatherDisplayItem(location = "Singaore",errorResp = "Test")
        val s = JsonUtils.toJson(weatherDisplayItem)
        assert(s.isNotBlank())

    }


    val sampleJson= "{\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "      \"country\": \"Afghanistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"country\": \"Albania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"country\": \"Algeria\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"


}