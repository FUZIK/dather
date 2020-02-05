package com.example.dather.datasource.openweathermap

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dather.City
import com.example.dather.Weather
import com.example.dather.datasource.ICitiesRepository
import com.example.dather.utility.OWMUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val OWM_API_KEY = "0a26e50d0f49b8d1233c981d36589133"
const val OWM_BASE_URL = "http://api.openweathermap.org/"

//TODO: change gson to Jackson for ex

class CitiesRetroRepo : ICitiesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(OWM_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val owmService = retrofit.create(IOWMService::class.java)
    private val lastLoadedCities = MutableLiveData<List<City>>()

    override fun getCity(latitude: Double, longitude: Double, lang: String): LiveData<City> {
        val liveData = MutableLiveData<City>()
        owmService.getCity(latitude, longitude, lang)
            .enqueue(object : Callback<OWMObject.OWMResult> {
                override fun onResponse(
                    call: Call<OWMObject.OWMResult>,
                    response: Response<OWMObject.OWMResult>
                ) {
                    liveData.value = converteResultToCity(response.body() ?: return)
                }

                override fun onFailure(call: Call<OWMObject.OWMResult>, t: Throwable) {
                    Log.e(this.javaClass.name, t.message!!)
                }
            })
        return liveData
    }

    override fun getLastLoadedCities(): LiveData<List<City>> = lastLoadedCities

    override fun getCitiesAround(
        latitude: Double,
        longitude: Double,
        limit: Int,
        lang: String
    ): LiveData<List<City>> {
        val liveData = MutableLiveData<List<City>>()
        owmService.getCitiesAround(latitude, longitude, limit, lang)
            .enqueue(object : Callback<ResultList> {
                override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                    var cities = emptyList<City>()
                    if (response.body() == null) {

                    }
                    response.body()?.let { result ->
                        liveData.value = result.list.map {
                            converteResultToCity(it)
                        }
                        lastLoadedCities.value = liveData.value
                    }
                }

                override fun onFailure(call: Call<ResultList>, t: Throwable) {
                    Log.e(this.javaClass.name, t.message!!)
                }
            })
        return liveData
    }

    private interface IOWMService {
        //TODO: return Cities
        @GET("/data/2.5/find")
        fun getCitiesAround(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double,
            @Query("cnt") limit: Int,
            @Query("lang") lang: String,
            @Query("appid") apiKey: String = OWM_API_KEY
        ): Call<ResultList>

        //TODO: return Weather
        //TODO: getWeatherAtLoc
        @GET("/data/2.5/weather")
        fun getCity(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double,
            @Query("lang") lang: String,
            @Query("appid") apiKey: String = OWM_API_KEY
        ): Call<OWMObject.OWMResult>
    }

    private data class ResultList(
        @SerializedName("list")
        @Expose
        val list: List<OWMObject.OWMResult>
    )

    companion object {
        @Volatile
        private var instance: CitiesRetroRepo? = null

        fun instance(): CitiesRetroRepo =
            instance ?: synchronized(this) {
                instance ?: CitiesRetroRepo().also {
                    instance = it
                }
            }

        fun converteResultToCity(result: OWMObject.OWMResult) =
            City(
                result.name,
                result.coord.lat,
                result.coord.lon,
                Weather(
                    result.weather[0].description,
                    OWMUtils.getIconFromDescription(result.weather[0].icon)
                )
            )
    }
}