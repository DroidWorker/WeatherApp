package com.example.domain.usecase

import com.example.domain.BuildConfig
import com.example.domain.WeatherCurrent
import com.example.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetWeatherTodayUC(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, long: Double): Flow<WeatherCurrent> {
        return repository.getWeatherToday(lat, long).map { response ->
            val weatherToday = WeatherCurrent.ModelMapper.from(response)
            weatherToday.iconUrl = "${BuildConfig.BASE_IMG_URL}${weatherToday.weather.icon}@4x.png"
            weatherToday
        }
    }
}