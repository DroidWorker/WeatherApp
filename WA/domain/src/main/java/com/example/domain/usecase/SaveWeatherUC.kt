package com.example.domain.usecase

import com.example.domain.LocalRepository
import com.example.domain.WeatherCurrent

class SaveWeatherUC(private val repository: LocalRepository) {
    suspend operator fun invoke(weather: WeatherCurrent) {
        repository.saveWeather(weather)
    }
}