package com.spdigital.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spdigital.weatherapp.R
import com.spdigital.weatherapp.data.WeatherDisplayItem
import com.spdigital.weatherapp.databinding.WeatherinfoListitemBinding
import com.spdigital.weatherapp.viewmodel.WeatherItemInfoViewModel

class WeatherListAdapter :
    ListAdapter<WeatherDisplayItem, WeatherListAdapter.ViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.weatherinfo_listitem, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: WeatherinfoListitemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WeatherDisplayItem) {
            with(binding) {
                // binding.location = data

                println("Location Data:: $data")

                viewModel = WeatherItemInfoViewModel(data)
                viewModel?.getWeatherIcon(binding.weatherImage)
                executePendingBindings()
            }
        }
    }
}

private class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherDisplayItem>() {

    override fun areItemsTheSame(
        oldItem: WeatherDisplayItem,
        newItem: WeatherDisplayItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: WeatherDisplayItem,
        newItem: WeatherDisplayItem
    ): Boolean {
        return oldItem == newItem
    }
}