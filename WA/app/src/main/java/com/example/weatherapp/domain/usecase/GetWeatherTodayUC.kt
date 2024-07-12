package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.WeatherCurrent
import com.example.weatherapp.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetWeatherTodayUC(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, long: Double): Flow<WeatherCurrent> {
        return repository.getWeatherToday(lat, long).map { response ->
            println("respoooonse ${response.name}")
            val weatherToday = WeatherCurrent.ModelMapper.from(response)
            weatherToday.iconUrl = "https://openweathermap.org/img/wn/${weatherToday.weather.icon}@4x.png"
            weatherToday
        }
    }
}