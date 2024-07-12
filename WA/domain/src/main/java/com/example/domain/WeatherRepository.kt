package com.example.domain

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherToday(lat: Double, long: Double): Flow<WeatherResponce>
    fun searchWeather(city: String): Flow<WeatherResponce?>
    fun getFiveDay(lat: Double, long: Double): Flow<FivedayResponce>
}