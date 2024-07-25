package com.example.domain.usecase

import com.example.domain.BuildConfig
import com.example.domain.WeatherFiveDay
import com.example.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetFiveDayWeatherUC(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, long: Double): Flow<WeatherFiveDay> {
        return repository.getFiveDay(lat, long).map { response ->
            val weatherFiveDay = WeatherFiveDay.ModelMapper.from(response)
            weatherFiveDay.weatherList.forEach{
                it.iconUrl = "${BuildConfig.BASE_IMG_URL}${it.weather.icon}.png"
            }
            weatherFiveDay
        }
    }
}