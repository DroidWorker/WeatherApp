package com.example.data

import com.example.domain.FiveDayResponse
import com.example.domain.WeatherRepository
import com.example.domain.WeatherResponse
import com.example.network.retrofit.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(var weatherApi: WeatherApi) :
    WeatherRepository {

    override fun getWeatherToday(lat: Double, long: Double): Flow<WeatherResponse> =
        flow { emit(weatherApi.getWeather(lat.toString(), long.toString())) }

    override fun searchWeather(city: String): Flow<WeatherResponse?> = flow {
        try {
            val response = weatherApi.searchWeather(city)
            emit(response)
        } catch (e: Exception) {
            emit(null)
        }
    }

    override fun getFiveDay(lat: Double, long: Double): Flow<FiveDayResponse> =
        flow { emit(weatherApi.getFiveDayWeather(lat.toString(), long.toString())) }

}