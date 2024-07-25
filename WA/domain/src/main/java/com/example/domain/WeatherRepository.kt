package com.example.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherToday(lat: Double, long: Double): Flow<WeatherResponse>
    fun searchWeather(city: String): Flow<WeatherResponse?>
    fun getFiveDay(lat: Double, long: Double): Flow<FiveDayResponse>
}