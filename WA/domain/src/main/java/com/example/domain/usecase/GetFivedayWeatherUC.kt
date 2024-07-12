package com.example.domain.usecase

import com.example.domain.WeatherFiveday
import com.example.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetFivedayWeatherUC(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, long: Double): Flow<WeatherFiveday> {
        return repository.getFiveDay(lat, long).map { response ->
            val weatherFiveday = WeatherFiveday.ModelMapper.from(response)
            weatherFiveday.weatherList.forEach{
                it.iconUrl = "https://openweathermap.org/img/wn/${it.weather.icon}.png"
            }
            weatherFiveday
        }
    }
}