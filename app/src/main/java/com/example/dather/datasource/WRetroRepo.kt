package com.example.dather.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dather.pojo.City
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val OWM_API_KEY = "0a26e50d0f49b8d1233c981d36589133"
const val OWM_BASE_URL = "http://api.openweathermap.org/"

interface IOWMService {
    //TODO: put api key to retrofit builder
    @GET("/data/2.5/find?appid=$OWM_API_KEY")
    fun getCitiesAround(@Query("lat") latitude: Double,
                        @Query("lon") longitude: Double,
                        @Query("cnt") limit: Int,
                        @Query("lang") lang: String): Call<RetroListCities>
    //TODO: put api key to retrofit builder
    @GET("/data/2.5/weather?appid=$OWM_API_KEY")
    fun getCity(@Query("lat") latitude: Double,
                @Query("lon") longitude: Double,
                @Query("lang") lang: String): Call<City>
}

// TODO: make this class to private
data class  RetroListCities(
    @SerializedName("list")
    @Expose
    val cities: List<City>
)

class WRetroRepo : IWRepository {
    private var owmService: IOWMService

    init {
        // - debug
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val loggingClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        // - end of debug
        val retrofit = Retrofit.Builder()
            .baseUrl(OWM_BASE_URL)
            //TODO: gson for stupid boys
            .addConverterFactory(GsonConverterFactory.create())
            // debug
            .client(loggingClient)
            .build()
        owmService = retrofit.create(IOWMService::class.java)

    }

    override fun getCity(latitude: Double, longitude: Double, lang: String): LiveData<City> {
        val liveData = MutableLiveData<City>()
        owmService.getCity(latitude, longitude, lang).enqueue(object : Callback<City> {
            override fun onResponse(call: Call<City>, response: Response<City>) {
                 liveData.value = response.body()
            }

            override fun onFailure(call: Call<City>, t: Throwable) {
                Log.e(this.javaClass.name, t.message!!)
            }
        })
        return liveData
    }

    override fun getCitiesAround(latitude: Double, longitude: Double, limit: Int, lang: String): LiveData<List<City>> {
        val liveData = MutableLiveData<List<City>>()
        owmService.getCitiesAround(latitude, longitude, limit, lang).enqueue(object : Callback<RetroListCities> {
            override fun onResponse(call: Call<RetroListCities>, response: Response<RetroListCities>) {
                val body = response.body()
                if (body != null) {
                    liveData.postValue(body.cities)
                }
            }

            override fun onFailure(call: Call<RetroListCities>, t: Throwable) {
                Log.e(this.javaClass.name, t.message!!)
            }
        })
        return liveData
    }

}