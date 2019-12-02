package com.spdigital.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spdigital.weatherapp.R
import com.spdigital.weatherapp.databinding.WeatherinfoListitemBinding

class WeatherListAdapter :
    ListAdapter<String, WeatherListAdapter.ViewHolder>(WeatherDiffCallback()) {

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

        fun bind(data: String) {
            with(binding) {
                binding.location = data
                executePendingBindings()
            }
        }
    }
}

private class WeatherDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}