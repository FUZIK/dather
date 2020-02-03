package com.example.dather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dather.R
import com.example.dather.pojo.City

class CitiesAdapter : RecyclerView.Adapter<CityHolder>() {
    var cities: List<City> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_city, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
    }
}