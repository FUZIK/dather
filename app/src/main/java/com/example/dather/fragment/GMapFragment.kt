package com.example.dather.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.dather.City
import com.example.dather.datasource.Repository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Double.NaN

const val KEY_QUEEN_LATITUDE = "queen_latitude"
const val KEY_QUEEN_LONGITUDE = "queen_longitude"
const val KEY_CAM_LATITUDE = "cam_latitude"
const val KEY_CAM_LONGITUDE = "cam_longitude"
const val KEY_CAM_ZOOM = "zoom_of_camera"
const val KEY_DRAW_LOADED_MARKERS = "draw_loaded_markers"

const val STRING_IN_PROGRESS = "Loading.."
const val MARKER_CITY_COLOR = 222f
const val DEFAULT_CAM_ZOOM = 7f

class GMapFragment : SupportMapFragment() {
    private lateinit var googleMap: GoogleMap
    private val citiesRepo = Repository.getCitiesRepo()
    //
    var queenMLat = NaN
    var queenMLon = NaN
    //

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getMapAsync {
            googleMap = it

            it.setOnMapClickListener { latLng ->
                doClickOnMap(latLng.latitude, latLng.longitude)
            }

            //TODO: double-click to centered queen marker

            val bundle = arguments ?: savedInstanceState
            //TODO: recode to one call .moveCamera
            if (bundle != null) {
                if (bundle.containsKey(KEY_QUEEN_LATITUDE) &&
                    bundle.containsKey(KEY_QUEEN_LONGITUDE)
                ) {
                    //TODO: if connect not found -> City not loaded
                    if (arguments != null) {
                        doClickOnMap(
                            bundle.getDouble(KEY_QUEEN_LATITUDE),
                            bundle.getDouble(KEY_QUEEN_LONGITUDE)
                        )

                    } else {
                        setQueenMarker(
                            bundle.getDouble(KEY_QUEEN_LATITUDE),
                            bundle.getDouble(KEY_QUEEN_LONGITUDE)
                        )
                    }

                }

                if (bundle.containsKey(KEY_CAM_LATITUDE) &&
                    bundle.containsKey(KEY_CAM_LONGITUDE)
                ) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                bundle.getDouble(KEY_CAM_LATITUDE),
                                bundle.getDouble(KEY_CAM_LONGITUDE)
                            )
                        )
                    )
                }

                if (bundle.containsKey(KEY_CAM_ZOOM)) {
                    val currentCamPosition = googleMap.cameraPosition.target
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(currentCamPosition.latitude, currentCamPosition.longitude),
                            bundle.getFloat(KEY_CAM_ZOOM)
                        )
                    )
                }
                if (bundle.getBoolean(KEY_DRAW_LOADED_MARKERS)) {
                    citiesRepo.getLastLoadedCities().value?.let { cities ->
                        drawWeatherMarkers(cities)
                    }
                }
            }
            arguments = null
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        if (!queenMLat.isNaN() && !queenMLon.isNaN()) {
            savedInstanceState.putDouble(KEY_QUEEN_LATITUDE, queenMLat)
            savedInstanceState.putDouble(KEY_QUEEN_LONGITUDE, queenMLon)
            savedInstanceState.putBoolean(KEY_DRAW_LOADED_MARKERS, true)
        }
        savedInstanceState.putDouble(KEY_CAM_LATITUDE, googleMap.cameraPosition.target.latitude)
        savedInstanceState.putDouble(KEY_CAM_LONGITUDE, googleMap.cameraPosition.target.longitude)
        savedInstanceState.putFloat(KEY_CAM_ZOOM, googleMap.cameraPosition.zoom)
        super.onSaveInstanceState(savedInstanceState)
    }

    fun doClickOnMapAsync(latitude: Double, longitude: Double) {
        getMapAsync {
            doClickOnMap(latitude, longitude, true)
        }
    }

    //TODO: unused param `moveCam`
    fun doClickOnMap(latitude: Double, longitude: Double, moveCam: Boolean = false) {
        googleMap.clear()
        setQueenMarker(latitude, longitude)
        loadWeatherMarkersFor(latitude, longitude)
        if (moveCam) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude, longitude),
                    DEFAULT_CAM_ZOOM
                )
            )
        }
    }

    fun addMarkerToMap(
        title: String,
        description: String,
        latitude: Double,
        longitude: Double,
        color: Float? = null
    ): Marker {
        val marker = MarkerOptions()
        color?.let {
            marker.icon(BitmapDescriptorFactory.defaultMarker(color))
        }
        marker.position(LatLng(latitude, longitude))
        marker.title(title)
        marker.snippet(description)
        return googleMap.addMarker(marker)
    }

    fun setQueenMarker(latitude: Double, longitude: Double) {
        queenMLat = latitude
        queenMLon = longitude
        val queenM = addMarkerToMap(STRING_IN_PROGRESS, STRING_IN_PROGRESS, latitude, longitude)
        citiesRepo.getCity(latitude, longitude).observe(this, Observer { city ->
            city?.let {
                queenM.title = "(${city.latitude}, ${city.longitude})"
                queenM.snippet = city.weather.description
            }
        })
    }

    fun loadWeatherMarkersFor(latitude: Double, longitude: Double) {
        citiesRepo.getCitiesAround(latitude, longitude).observe(this, Observer { cities ->
            cities?.let {
                drawWeatherMarkers(cities)
            }
        })
    }

    fun drawWeatherMarkers(cities: List<City>) {
        cities.forEach { city ->
            addMarkerToMap(
                city.name,
                city.weather.description,
                city.latitude,
                city.longitude,
                MARKER_CITY_COLOR
            )
        }
    }
}
