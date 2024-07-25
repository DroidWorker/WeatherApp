package com.example.domain.usecase

import com.example.domain.BuildConfig
import com.example.domain.LocalRepository
import com.example.domain.WeatherCurrent
import com.example.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchWeatherByCityUC(private val repository: WeatherRepository, private val localRepository: LocalRepository) {
    operator fun invoke(city: String): Flow<WeatherCurrent?> {
        return repository.searchWeather(city).map { response ->
            val weatherToday = response?.let { WeatherCurrent.ModelMapper.from(it) }
            if(weatherToday!=null)weatherToday.iconUrl = "${BuildConfig.BASE_IMG_URL}${weatherToday.weather.icon}@2x.png"
            weatherToday
        }
    }

    suspend fun saveQuery(q: String){
        localRepository.saveQuery(q)
    }

    fun getUserQueries(): Flow<List<String>> = localRepository.getUserQueries()
}