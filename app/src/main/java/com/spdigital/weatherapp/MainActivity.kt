package com.spdigital.weatherapp


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.spdigital.weatherapp.adapters.WeatherListAdapter
import com.spdigital.weatherapp.data.WeatherDisplayItem
import com.spdigital.weatherapp.databinding.ActivityMainBinding
import com.spdigital.weatherapp.util.Constants
import com.spdigital.weatherapp.util.CountryUtils
import com.spdigital.weatherapp.util.JsonUtils
import com.spdigital.weatherapp.util.LocationQueue
import com.spdigital.weatherapp.viewmodel.LocalWeatherViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var mLocalWeatherViewModel: LocalWeatherViewModel

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding.appToolbar.title = ""
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.appToolbar.navigationIcon = null
        mLocalWeatherViewModel = ViewModelProviders.of(this).get(LocalWeatherViewModel::class.java)
        weatherListAdapter = WeatherListAdapter()


        binding.locationList.adapter = weatherListAdapter

        binding.hasSavedLocations = false
        loadCountriesData()
        loadDataInAutoComplete()
        mLocalWeatherViewModel.liveWorkRequest.observe(this, Observer {
            initWorkerObservers(it.id)
        })
        binding.countriesList.onItemClickListener =
            OnItemClickListener { parent, v, position, _ ->
                hideKeyBoard(v)
                val selected = parent.getItemAtPosition(position) as String
                this.binding.countriesList.clearListSelection()
                this.binding.countriesList.text = null
                addLocationToQueue(selected)
            }
    }

    private fun addLocationToQueue(value: String) {
        LocationQueue.addLocationToQueue(value)
        mLocalWeatherViewModel.fetchLocalWeatherData(value)
    }

    private fun initWorkerObservers(uuid: UUID) {
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uuid)
            .observe(this, Observer { workInfo ->
                // Check if the current work's state is "successfully finished"
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val respString = workInfo.outputData.getString(Constants.KEY_API_RESPONSE)
                    respString?.let {
                        val weatherItem = JsonUtils.responseParser(
                            WeatherDisplayItem.serializer(),
                            it
                        ) as? WeatherDisplayItem
                        LocationQueue.updateDataResult(weatherItem)
                        createOrUpdateListItems()
                    }
                }
            })
    }

    private fun createOrUpdateListItems() {
        val list = LocationQueue.getLocationQueueAsList()
        println("Weather Item :: >>>>>>>>>>>>>>> $list")
        binding.hasSavedLocations = true
        weatherListAdapter.submitList(list)
        weatherListAdapter.notifyDataSetChanged()
    }

    private fun loadCountriesData() {
        try {
            val ins = resources.openRawResource(
                resources.getIdentifier(
                    "countries",
                    "raw", packageName
                )
            )
            CountryUtils.readCountriesFromStream(ins)
        } catch (ex: Exception) {
            Log.e("TAG", "$ex")
        }
    }

    private fun loadDataInAutoComplete() {
        val list = CountryUtils.getCountriesObject()
        binding.countriesList.threshold = 1
        binding.countriesList.setAdapter(
            ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_item,
                list
            )
        )

    }

    private fun hideKeyBoard(v: View) {
        val iManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        iManager?.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}
