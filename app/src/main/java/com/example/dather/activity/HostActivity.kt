package com.example.dather.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.dather.City
import com.example.dather.R
import com.example.dather.datasource.openweathermap.WRetroRepo
import com.example.dather.fragment.CitiesFragment
import com.example.dather.fragment.GMapFragment
import com.example.dather.fragment.KEY_LATITUDE
import com.example.dather.fragment.KEY_LONGITUDE
import com.google.android.material.bottomnavigation.BottomNavigationView

const val DEFAULT_LATITUDE = 47.2363064
const val DEFAULT_LONGITUDE = 39.7200547

class HostActivity : AppCompatActivity() {
    val repository = WRetroRepo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        // TODO get current location

        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_item_map -> {
                    val mapFragment = GMapFragment()
                    val args = Bundle()
                    args.putDouble(KEY_LATITUDE, latitude)
                    args.putDouble(KEY_LONGITUDE, longitude)
                    mapFragment.arguments = args
                    replaceOp(findFM(it.itemId) ?: mapFragment)
                }

                R.id.menu_item_city -> {
                    replaceOp(findFM(it.itemId) ?: CitiesFragment())
                }
            }
            true
        }

        if (savedInstanceState == null) {
            //TODO: костыль!?
            navigation.selectedItemId = R.id.menu_item_map
        }

        instance = this
    }

    fun findFM(@IdRes idRes: Int): Fragment? =
        supportFragmentManager.findFragmentById(idRes)

    fun replaceOp(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.host_container, fragment, null)
            .commit()
    }

    //TODO: правильно ли работает ViewModel?
    companion object : IHostActivity {
        private lateinit var instance: HostActivity

        override var latitude: Double = DEFAULT_LATITUDE
        override var longitude: Double = DEFAULT_LONGITUDE

        override fun getCity(latitude: Double, longitude: Double, lang: String): LiveData<City> =
            instance.repository.getCity(latitude, longitude, lang)

        override fun getCitiesAround(
            latitude: Double,
            longitude: Double,
            limit: Int,
            lang: String
        ): LiveData<List<City>> =
            instance.repository.getCitiesAround(latitude, longitude, limit, lang)
    }

}
