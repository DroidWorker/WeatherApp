package com.example.domain.usecase

import com.example.domain.LocalRepository
import com.example.domain.WeatherFiveDay

class SaveWeatherFiveUC(private val repository: LocalRepository) {
    suspend operator fun invoke(weatherFiveDay: WeatherFiveDay) {
        repository.saveWeatherFive(weatherFiveDay)
    }
}