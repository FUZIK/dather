package com.example.dather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.dather.datasource.Repository
import com.example.dather.fragment.CitiesFragment
import com.example.dather.fragment.GMapFragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView


const val FRAGMENT_COUNT = 2
const val MAP_FRAGMENT_I = 0
const val CITIES_FRAGMENT_I = 1

class HostActivity : FragmentActivity() {
    private lateinit var viewPager: ViewPager2
    private val citiesRepo = Repository.getCitiesRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        // TODO (copy-paste code) recode next')
        // ~copy-paste code
        ActivityCompat.requestPermissions (
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            44
        )
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
                var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    Log.e("LOCATION_GET", "${location == null}")
                    if (location == null) {
                        val mLocationRequest = LocationRequest()
                        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        mLocationRequest.interval = 0
                        mLocationRequest.fastestInterval = 0
                        mLocationRequest.numUpdates = 1
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest, object : LocationCallback() {
                                override fun onLocationResult(locResult: LocationResult?) {
                                    Log.e("LOCATION_GET", "${locResult == null}")
                                    if (locResult != null) {
                                        val currentLocation = locResult.lastLocation
                                        Log.e("LOCATION_GET", "done ${currentLocation.latitude}")
                                        citiesRepo.getCitiesAround(currentLocation.latitude, currentLocation.longitude)
                                    }
                                }
                            },
                            Looper.myLooper()
                        )
                    } else {
                        citiesRepo.getCitiesAround(location.latitude, location.longitude)
                    }
                }
            }
        }
        // end of ~copy-paste code

        viewPager = findViewById(R.id.host_pager)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = PA(this)

        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener { it ->
            when(it.itemId) {
                R.id.menu_item_map -> {
                    viewPager.setCurrentItem(MAP_FRAGMENT_I, true)
                }

                R.id.menu_item_city -> {
                    viewPager.setCurrentItem(CITIES_FRAGMENT_I, true)
                }
            }
            true
        }

        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.menu_item_map
        }
    }

    private inner class PA(fm: FragmentActivity) : FragmentStateAdapter(fm) {
        override fun getItemCount() = FRAGMENT_COUNT

        override fun createFragment(position: Int): Fragment {
            return if (position == CITIES_FRAGMENT_I) CitiesFragment()
                   else GMapFragment()
        }
    }
}
