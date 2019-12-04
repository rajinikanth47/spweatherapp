package com.spdigital.weatherapp.util

import org.junit.Test
import java.io.ByteArrayInputStream

class CountryUtilsTest{


    @Test
    fun readCountriesFromStreamTest(){
        val inputStream = ByteArrayInputStream(sampleJson.toByteArray(Charsets.UTF_8))
        CountryUtils.readCountriesFromStream(inputStream)
        assert(!CountryUtils.jsonCountriesContent.isNullOrBlank())
    }

    @Test
    fun getCountriesObjectTest(){
        val inputStream = ByteArrayInputStream(sampleJson.toByteArray(Charsets.UTF_8))
        CountryUtils.readCountriesFromStream(inputStream)
        val obj = CountryUtils.getCountriesObject()
        assert(!obj.isNullOrEmpty())
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