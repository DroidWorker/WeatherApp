package com.example.network.retrofit

import com.example.domain.FivedayResponce
import com.example.domain.WeatherResponce
import retrofit2.http.GET
import retrofit2.http.Query
val APIKEY = "08f683afd1f3d620d84624b7b5a988e5"
interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = APIKEY): com.example.domain.WeatherResponce

    @GET("weather")
    suspend fun searchWeather(@Query("q") lon: String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = APIKEY): com.example.domain.WeatherResponce

    @GET("forecast")
    suspend fun getFivedayWeather(@Query("lat") lat: String, @Query("lon") lon:String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = APIKEY): com.example.domain.FivedayResponce

}