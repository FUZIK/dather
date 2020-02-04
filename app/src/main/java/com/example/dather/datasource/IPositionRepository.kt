package com.example.dather.datasource

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng

const val DEFAULT_LATITUDE = 47.2363064
const val DEFAULT_LONGITUDE = 39.7200547

interface IPositionRepository {
    fun getLastPosition(): LiveData<LatLng>
    fun setLastPosition(position: LatLng)
}