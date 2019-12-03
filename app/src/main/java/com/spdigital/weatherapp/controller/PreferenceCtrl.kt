package com.spdigital.weatherapp.controller

import android.content.SharedPreferences
import com.spdigital.weatherapp.data.LocationList
import com.spdigital.weatherapp.util.Constants.LOCATION_PREF_ITEM
import com.spdigital.weatherapp.util.JsonUtils
import kotlinx.serialization.KSerializer

class PreferenceCtrl private constructor(private val sp: SharedPreferences) {

    var editor: SharedPreferences.Editor? = null

    init {
        editor = sp.edit()
    }

    fun putPreferencesFromObject(mLocationList: LocationList) {
        val result = JsonUtils.toJson(mLocationList)
        editor?.putString(LOCATION_PREF_ITEM, result)
        editor?.apply()
    }

    fun getPreferencesList(
        serializer: KSerializer<LocationList>,
        locationPrefItem: String
    ): Any? {
        val items = sp.getString(locationPrefItem, null)
        return JsonUtils.responseParser(serializer, items)
    }

    companion object {

        @Volatile
        private var instance: PreferenceCtrl? = null

        fun getInstance(sp: SharedPreferences) =
            instance ?: synchronized(this) {
                instance ?: PreferenceCtrl(sp).also { instance = it }
            }
    }
}