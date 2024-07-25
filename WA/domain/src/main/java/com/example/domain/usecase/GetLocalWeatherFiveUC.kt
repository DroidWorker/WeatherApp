package com.example.domain.usecase

import com.example.domain.LocalRepository
import com.example.domain.WeatherFiveDay
import kotlinx.coroutines.flow.Flow

class GetLocalWeatherFiveUC(private val repository: LocalRepository) {
    operator fun invoke(): Flow<WeatherFiveDay> = repository.getWeatherFive()
}