package com.example.dather.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dather.City
import com.example.dather.R

class CityHolder(view: View) : RecyclerView.ViewHolder(view) {
    var textCityName: TextView
            = view.findViewById(R.id.text_city_name)
    var textCityWDescription: TextView
            = view.findViewById(R.id.text_city_weather_description)
    var imageCityIcon: ImageView
            = view.findViewById(R.id.city_weather_icon)

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