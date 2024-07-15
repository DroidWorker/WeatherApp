package com.example.data

import com.example.domain.FivedayResponce
import com.example.domain.WeatherRepository
import com.example.domain.WeatherResponce
import com.example.network.retrofit.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(var weatherApi: WeatherApi) :
    com.example.domain.WeatherRepository {

    override fun getWeatherToday(lat: Double, long: Double): Flow<com.example.domain.WeatherResponce> =
        flow { emit(weatherApi.getWeather(lat.toString(), long.toString())) }

    override fun searchWeather(city: String): Flow<com.example.domain.WeatherResponce?> = flow {
        try {
            val response = weatherApi.searchWeather(city)
            emit(response)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getFiveDay(lat: Double, long: Double): Flow<com.example.domain.FivedayResponce> =
        flow { emit(weatherApi.getFivedayWeather(lat.toString(), long.toString())) }

}