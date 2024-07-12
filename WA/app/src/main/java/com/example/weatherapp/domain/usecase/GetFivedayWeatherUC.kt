package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.WeatherFiveday
import com.example.weatherapp.domain.WeatherRepository
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