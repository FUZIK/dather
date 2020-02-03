package com.example.dather.fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.dather.activity.HostActivity
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
    lateinit var mPresenter: GMapFragmentPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mPresenter = GMapFragmentPresenter(this)

        getMapAsync {
            googleMap = it

            it.setOnMapClickListener {latLng ->
                mPresenter.onClickOnMap(latLng)
            }
//            it.setOnMarkerClickListener {marker ->
//                return@setOnMarkerClickListener mPresenter.onClickOnMarker(marker)
//            }

            //TODO: otherwise on rotate s./etc marker not updated
            //TODO: похорошему собрать логику на Bundle, возможно
            val marker = addMarker(
                LatLng(HostActivity.latitude, HostActivity.longitude),
                STRING_IN_PROGRESS,
                STRING_IN_PROGRESS
            )
            addWeatherMarker(marker)

//            val args = arguments
//            if (args != null) {
//                if (args.containsKey(KEY_LATITUDE)
//                    && args.containsKey(KEY_LONGITUDE)) {
//                    val marker = addMarker(
//                        LatLng(args.getDouble(KEY_LATITUDE), args.getDouble(KEY_LONGITUDE)),
//                        STRING_IN_PROGRESS,
//                        STRING_IN_PROGRESS
//                    )
//                    addWeatherMarker(marker)
//                }
//            }
        }
    }

//    override fun onSaveInstanceState(p0: Bundle) {
//        super.onSaveInstanceState(Bundle())
//    }

    fun updateLastPos(position: LatLng) {
        Log.e("LATPOS", "UPDATED: from ${HostActivity.latitude} to ${position.latitude}")
        HostActivity.latitude = position.latitude
        HostActivity.longitude = position.longitude
    }

    fun addMarker(position: LatLng, title: String, description: String): Marker {
        val marker = MarkerOptions()
        marker.position(position)
        marker.title(title)
        marker.snippet(description)
        return googleMap.addMarker(marker)
    }

    fun addWeatherMarker(marker: Marker) {
        HostActivity.getCity(HostActivity.latitude, HostActivity.longitude).observe(this, Observer {
            marker.title = it.name
            marker.snippet = it.weather[0].description
        })
    }

    fun clearMap() {
        googleMap.clear()
    }
}


interface IGMapFragmentPresenter {
    fun onClickOnMap(position: LatLng)
//    fun onClickOnMarker(marker: Marker): Boolean
}


class GMapFragmentPresenter(
    val gMapFragment: GMapFragment

) : IGMapFragmentPresenter {
    override fun onClickOnMap(position: LatLng) {
        gMapFragment.updateLastPos(position)
        gMapFragment.clearMap()
        gMapFragment.addWeatherMarker(
            gMapFragment.addMarker(position, STRING_IN_PROGRESS, STRING_IN_PROGRESS)
        )
    }

//    override fun onClickOnMarker(marker: Marker): Boolean {
//        if (marker.isInfoWindowShown) {
//            marker.hideInfoWindow()
//        } else {
//            marker.showInfoWindow()
//        }
//        return true
//    }
}