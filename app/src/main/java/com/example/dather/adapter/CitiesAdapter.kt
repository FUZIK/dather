package com.example.dather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dather.City
import com.example.dather.R

class CitiesAdapter : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {
    var cities: List<City> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_city, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
    }

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textCityName: TextView = view.findViewById(R.id.text_city_name)
        var textCityWDescription: TextView = view.findViewById(R.id.text_city_weather_description)
        var imageCityIcon: ImageView = view.findViewById(R.id.city_weather_icon)

        fun showDescription(flag: Boolean) {
            textCityWDescription.visibility = if (flag) View.VISIBLE else View.GONE
        }

        fun bind(city: City) {
            textCityName.text = city.name
            textCityWDescription.text = city.weather.description
            Glide.with(itemView).load(
                city.weather.iconUri
            ).into(imageCityIcon)

        }
    }
}