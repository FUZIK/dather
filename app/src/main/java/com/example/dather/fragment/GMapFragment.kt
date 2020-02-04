package com.example.dather.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.dather.datasource.Repository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

const val KEY_LATITUDE = "latitude"
const val KEY_LONGITUDE = "longitude"
const val STRING_IN_PROGRESS = "Loading.."

class GMapFragment : SupportMapFragment() {
    lateinit var googleMap: GoogleMap
    val positionRepo = Repository.getPositionRepo()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getMapAsync {
            googleMap = it

            it.setOnMapClickListener {latLng ->
                positionRepo.setLastPosition(latLng)
            }

            positionRepo.getLastPosition().observe(this, Observer {position ->
                it?.let {
                    googleMap.clear()
                    addWeatherMarker(
                        addMarker(position)
                    )
                }
            })
        }
    }

    fun addMarker(position: LatLng, title: String = STRING_IN_PROGRESS, description: String = STRING_IN_PROGRESS): Marker {
        val marker = MarkerOptions()
        marker.position(position)
        marker.title(title)
        marker.snippet(description)
        return googleMap.addMarker(marker)
    }

    fun addWeatherMarker(marker: Marker) {
        val position = positionRepo.getLastPosition().value
        position?.let {
            Repository.getWeatherRepo().getCity(position.latitude, position.longitude).observe(this, Observer {
                marker.title = it.name
                marker.snippet = it.weather.description
            })
        }

    }
}
