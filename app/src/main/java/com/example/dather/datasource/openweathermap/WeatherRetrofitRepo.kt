package com.example.dather.datasource.openweathermap

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dather.City
import com.example.dather.Weather
import com.example.dather.datasource.IWRepository
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

class WRetroRepo : IWRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(OWM_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val owmService = retrofit.create(IOWMService::class.java)

    override fun getCity(latitude: Double, longitude: Double, lang: String): LiveData<City> {
        val liveData = MutableLiveData<City>()
        owmService.getCity(latitude, longitude, lang).enqueue(object : Callback<OWMObject.OWMResult> {
            override fun onResponse(call: Call<OWMObject.OWMResult>, response: Response<OWMObject.OWMResult>) {
                liveData.value = resultToCity(response.body() ?: return)
            }
            override fun onFailure(call: Call<OWMObject.OWMResult>, t: Throwable) {
                Log.e(this.javaClass.name, t.message!!)
            }
        })
        return liveData
    }

    override fun getCitiesAround(latitude: Double, longitude: Double, limit: Int, lang: String): LiveData<List<City>> {
        val liveData = MutableLiveData<List<City>>()
        owmService.getCitiesAround(latitude, longitude, limit, lang).enqueue(object : Callback<ResultList> {
            override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                response.body()?.let { result ->
                    liveData.postValue(result.list.map{ resultToCity(it) })
                }
            }
            override fun onFailure(call: Call<ResultList>, t: Throwable) {
                Log.e(this.javaClass.name, t.message!!)
            }
        })
        return liveData
    }

    companion object {
        fun resultToCity(result: OWMObject.OWMResult) =
            City(
                result.name,
                Weather(
                    result.weather[0].description,
                    result.weather[0].icon
                )
            )
    }

    private interface IOWMService {
        @GET("/data/2.5/find")
        fun getCitiesAround(@Query("lat") latitude: Double,
                            @Query("lon") longitude: Double,
                            @Query("cnt") limit: Int,
                            @Query("lang") lang: String,
                            @Query("appid") apiKey: String = OWM_API_KEY): Call<ResultList>

        @GET("/data/2.5/weather")
        fun getCity(@Query("lat") latitude: Double,
                    @Query("lon") longitude: Double,
                    @Query("lang") lang: String,
                    @Query("appid") apiKey: String = OWM_API_KEY): Call<OWMObject.OWMResult>
    }

    private data class ResultList(
        @SerializedName("list")
        @Expose
        val list: List<OWMObject.OWMResult>
    )
}