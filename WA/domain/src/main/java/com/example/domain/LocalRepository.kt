package com.example.domain

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveWeather(weather: WeatherCurrent)

    fun getLastWeather(): Flow<WeatherCurrent>

    fun getTimesList(): Flow<List<Int>>

    fun getWeatherByDate(dt: Int): Flow<WeatherCurrent>

    suspend fun saveWeatherFive(weather: WeatherFiveDay)

    fun getWeatherFive(): Flow<WeatherFiveDay>

    suspend fun saveQuery(q: String)

    fun getUserQueries(): Flow<List<String>>
}