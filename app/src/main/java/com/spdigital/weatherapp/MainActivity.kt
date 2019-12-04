package com.spdigital.weatherapp


import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import com.google.android.material.snackbar.Snackbar
import com.spdigital.weatherapp.adapters.WeatherListAdapter
import com.spdigital.weatherapp.controller.PreferenceCtrl
import com.spdigital.weatherapp.data.LocationList
import com.spdigital.weatherapp.data.WeatherDisplayItem
import com.spdigital.weatherapp.databinding.ActivityMainBinding
import com.spdigital.weatherapp.util.*
import com.spdigital.weatherapp.util.Constants.LOCATION_PREF_ITEM
import com.spdigital.weatherapp.util.Constants.SHARED_PREF_NAME
import com.spdigital.weatherapp.viewmodel.LocalWeatherViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherListAdapter: WeatherListAdapter
    private lateinit var mLocalWeatherViewModel: LocalWeatherViewModel
    private lateinit var mPreferenceCtrl: PreferenceCtrl
    private  var isBulkLoading: Boolean = false

    companion object{
        const val RAW_LOCATIONS_JSON_FILE_NAME = "countries"
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        applyToolbarAndStatusBarSettings()
        setSharePreferencesController()
        prepareWeatherOnlineList()
        loadCountriesData()
        loadPreferencesLocationsData()

        binding.countriesList.onItemClickListener =
            OnItemClickListener { parent, _, position, _ ->
                hideKeyBoard()
                showProgressBar(View.VISIBLE)
                val selected = parent.getItemAtPosition(position) as String
                this.binding.countriesList.clearListSelection()
                this.binding.countriesList.text = null

                if(!LocationQueue.isLocationAdded(selected)) {
                    addLocationToQueue(selected)
                }else{
                    showSnackBar(getText(R.string.country_already_in_list).toString())
                }
            }
    }

    private fun prepareWeatherOnlineList(){
        mLocalWeatherViewModel = ViewModelProviders.of(this).get(LocalWeatherViewModel::class.java)
        weatherListAdapter = WeatherListAdapter()
        binding.locationList.adapter = weatherListAdapter

    }
    private fun applyToolbarAndStatusBarSettings(){
        binding.appToolbar.title = ""
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.appToolbar.navigationIcon = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setSharePreferencesController(){
        val sp = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        mPreferenceCtrl = PreferenceCtrl.getInstance(sp)
    }

    private fun addLocationToQueue(value: String) {
        showProgressBar(View.VISIBLE)
        LocationQueue.addLocationToQueue(value)
        loadSingleLocationData(value)
    }

    private fun loadDataTOListView(weatherItem: WeatherDisplayItem?) {
        weatherItem?.let{
            LocationQueue.updateDataResult(it)
            createOrUpdateListItems()
            addOrUpdatePreferences()
        }
    }

    private fun createOrUpdateListItems() {
        val list = LocationQueue.getLocationQueueAsList()
        binding.hasSavedLocations = true
        weatherListAdapter.submitList(list)
        weatherListAdapter.notifyDataSetChanged()
        binding.locationList.smoothScrollToPosition(0)
    }

    private fun loadCountriesData() {
        try {
            val ins = resources.openRawResource(
                resources.getIdentifier(
                    RAW_LOCATIONS_JSON_FILE_NAME,
                    "raw", packageName
                )
            )
            CountryUtils.readCountriesFromStream(ins)
            loadDataInAutoComplete()
        } catch (ex: Exception) {
           showSnackBar(getText(R.string.error_country_list_fail).toString())
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

    private fun hideKeyBoard() {
        val iManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        iManager?.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showSnackBar(msg:String?=null){
        val showText = msg?.let{
            msg
        }?:run{
             resources.getText(R.string.technical_error).toString()

        }
        Snackbar.make(binding.clRootView, showText, Snackbar.LENGTH_LONG).show()
    }

    private fun showProgressBar(isShow:Int){
        binding.progressBarCyclic.visibility = isShow
    }

    private fun addOrUpdatePreferences(){
        val mList = LocationQueue.getLocationListForSavingInPreferences().asReversed()
        val locationObj = LocationList(mList)
        mPreferenceCtrl.putPreferencesFromObject(locationObj)
    }

    private fun loadPreferencesLocationsData(){
        var locationList = loadPreferenceValues()
        if(locationList.location.size > 0) {
            isBulkLoading = true
            LocationQueue.addLocationItems(locationList)
            loadBulkLocationsData(locationList)
        }else{
            binding.hasSavedLocations = false
            isBulkLoading = false
        }
    }

    private fun loadPreferenceValues():LocationList{
        var item = mPreferenceCtrl.getPreferencesList(LocationList.serializer(),LOCATION_PREF_ITEM)
        return item?.let {
            it as LocationList
        }?: LocationList(
            mutableListOf())
    }

    private fun loadSingleLocationData(location: String){
        mLocalWeatherViewModel.fetchLocalWeatherData(location, serviceCallback())
    }

    private fun loadBulkLocationsData(mList: LocationList){
        showProgressBar(View.VISIBLE)
        val length = mList.location.size
        var isLastItem = false
        for ((index, value) in mList.location.withIndex()) {
            if(index == length-1)
                isLastItem = true

            mLocalWeatherViewModel.fetchLocalWeatherData(value.location, serviceCallback(),isLastItem)
        }
    }

    private fun serviceCallback(): ServiceCallback<LocalWeatherViewModel.TriggerItem> {
        return object : ServiceCallback<LocalWeatherViewModel.TriggerItem> {
            override fun onSuccess(response: LocalWeatherViewModel.TriggerItem?) {
                response?.let {
                    listenWorkerResponse(it)

                }
            }
            override fun onError(error: Throwable) {
                showSnackBar()
            }
        }
    }

    private fun listenWorkerResponse(trigger:LocalWeatherViewModel.TriggerItem){
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(trigger.uuid)
            .observe(this, Observer { workInfo ->
                // Check if the current work's state is "successfully finished"
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val respString = workInfo.outputData.getString(Constants.KEY_API_RESPONSE)
                    respString?.let {
                        val weatherItem = JsonUtils.responseParser(
                            WeatherDisplayItem.serializer(),
                            it
                        ) as? WeatherDisplayItem

                        if(trigger.progressVisibleStatus){
                            showProgressBar(View.GONE)
                        }

                        if(weatherItem?.errorResp != null){
                            LocationQueue.popLastLocation()
                            showSnackBar(getText(R.string.error_location_finding).toString())
                        }else{
                            loadDataTOListView(weatherItem)
                        }
                    }
                }
            })
    }


}
