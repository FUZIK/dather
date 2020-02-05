package com.example.dather.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.dather.datasource.Repository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

const val KEY_LATITUDE = "latitude"
const val KEY_LONGITUDE = "longitude"
const val STRING_IN_PROGRESS = "Loading.."
const val MARKER_CITY_COLOR = 111

class GMapFragment : SupportMapFragment() {
    private lateinit var googleMap: GoogleMap
    private val citiesRepo = Repository.getCitiesRepo()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getMapAsync {
            googleMap = it

            it.setOnMapClickListener { latLng ->
                googleMap.clear()
                addWeatherMarkers(latLng.latitude, latLng.longitude)
            }
        }
    }


    fun addMarker(title: String, description: String, latitude: Double, longitude: Double, color: Float? = null): Marker {
        val marker = MarkerOptions()
        color?.let {
            marker.icon(BitmapDescriptorFactory.defaultMarker(color))
        }
        marker.position(LatLng(latitude, longitude))
        marker.title(title)
        marker.snippet(description)
        return googleMap.addMarker(marker)
    }

    fun addWeatherMarkers(latitude: Double, longitude: Double) {
        val mainM = addMarker(STRING_IN_PROGRESS, STRING_IN_PROGRESS, latitude, longitude)
        citiesRepo.getCity(latitude, longitude).observe(this, Observer { city ->
            city?.let {
                mainM.title = "(${city.latitude}, ${city.longitude})"
                mainM.snippet = city.weather.description
            }
        })
        citiesRepo.getCitiesAround(latitude, longitude).observe(this, Observer { cities ->
            cities?.let {
                cities.forEach { city ->
                    addMarker(
                        city.name,
                        city.weather.description,
                        city.latitude,
                        city.longitude,
                        222f
                    )
                }
            }
        })
    }
}
