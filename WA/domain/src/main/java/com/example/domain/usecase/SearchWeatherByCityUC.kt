package com.example.domain.usecase

import com.example.domain.WeatherCurrent
import com.example.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchWeatherByCityUC(private val repository: WeatherRepository) {
    operator fun invoke(city: String): Flow<WeatherCurrent?> {
        return repository.searchWeather(city).map { response ->
            val weatherToday = response?.let { WeatherCurrent.ModelMapper.from(it) }
            if(weatherToday!=null)weatherToday.iconUrl = "https://openweathermap.org/img/wn/${weatherToday.weather.icon}@2x.png"
            weatherToday
        }
    }
}