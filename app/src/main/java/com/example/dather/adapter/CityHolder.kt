package com.example.dather.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dather.R
import com.example.dather.pojo.City
import com.example.dather.utils.OWMUtils

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
        //TODO: hot: single object (ref)
        textCityWDescription.text = city.weather[0].description
        //TODO: hot: single object (ref)
        if (city.weather[0].owmIconUri != null) {

            Glide.with(itemView).load(
                OWMUtils.getIconFromDescription(
                    //TODO: hot: single object (ref)
                    city.weather[0].owmIconUri
                )
            ).into(imageCityIcon)
        }

    }
}