package com.example.data

import com.example.network.retrofit.WeatherApi
import com.example.weatherapp.domain.FivedayResponce
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.WeatherResponce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(var weatherApi: com.example.network.retrofit.WeatherApi) : WeatherRepository {

    override fun getWeatherToday(lat: Double, long: Double): Flow<WeatherResponce> =
        flow { emit(weatherApi.getWeather(lat.toString(), long.toString())) }

    override fun searchWeather(city: String): Flow<WeatherResponce?> = flow {
        try {
            val response = weatherApi.searchWeather(city)
            emit(response)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getFiveDay(lat: Double, long: Double): Flow<FivedayResponce> =
        flow { emit(weatherApi.getFivedayWeather(lat.toString(), long.toString())) }

}