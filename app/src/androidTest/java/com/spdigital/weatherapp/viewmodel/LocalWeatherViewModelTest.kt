package com.spdigital.weatherapp.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.spdigital.weatherapp.util.ServiceCallback
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocalWeatherViewModelTest {

    private lateinit var viewModel: LocalWeatherViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        viewModel = LocalWeatherViewModel(context)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        viewModel.fetchLocalWeatherData("Singapore",object :ServiceCallback<LocalWeatherViewModel.TriggerItem>{
            override fun onSuccess(response: LocalWeatherViewModel.TriggerItem?) {
                Assert.assertTrue(response != null)
            }

            override fun onError(error: Throwable) {
                Assert.assertFalse(error.message != null)
            }

        })
    }
}