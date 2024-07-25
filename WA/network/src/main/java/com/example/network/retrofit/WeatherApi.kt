package com.example.network.retrofit

import com.example.domain.FiveDayResponse
import com.example.domain.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = BuildConfig.API_KEY): WeatherResponse

    @GET("weather")
    suspend fun searchWeather(@Query("q") lon: String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = BuildConfig.API_KEY): WeatherResponse

    @GET("forecast")
    suspend fun getFiveDayWeather(@Query("lat") lat: String, @Query("lon") lon:String, @Query("units") units: String = "metric", @Query("lang") lang: String = "ru",  @Query("appid") appid: String = BuildConfig.API_KEY): FiveDayResponse

}