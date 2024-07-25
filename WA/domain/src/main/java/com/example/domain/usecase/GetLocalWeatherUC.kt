package com.example.domain.usecase

import com.example.domain.LocalRepository
import com.example.domain.WeatherCurrent
import kotlinx.coroutines.flow.Flow

class GetLocalWeatherUC(private val repository: LocalRepository) {
    operator fun invoke(): Flow<WeatherCurrent> = repository.getLastWeather()

    fun getTimes(): Flow<List<Int>> = repository.getTimesList()

    fun getWeatherByDate(dt: Int): Flow<WeatherCurrent> = repository.getWeatherByDate(dt)
}