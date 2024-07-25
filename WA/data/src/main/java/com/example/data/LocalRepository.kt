package com.example.data

import com.example.domain.LocalRepository
import com.example.domain.WeatherCurrent
import com.example.domain.WeatherDescription
import com.example.domain.WeatherFiveDay
import com.example.domain.convertTo
import com.example.localdb.MainData
import com.example.localdb.SearchDao
import com.example.localdb.UserQuery
import com.example.localdb.Weather
import com.example.localdb.WeatherDao
import com.example.localdb.WeatherItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class LocalRepositoryImpl(private val weatherDAO: WeatherDao, private val searchDao: SearchDao): LocalRepository{
    override suspend fun saveWeather(weather: WeatherCurrent) {
        withContext(Dispatchers.IO) {
            weatherDAO.insertWeather(Weather(weather.dt, weather.city, weather.temp, weather.feelsLike, weather.tempMin, weather.tempMax, weather.humidity, weather.weather.id, weather.iconUrl))
            weatherDAO.insertWeatherDescription(weather.weather.convertTo(com.example.localdb.WeatherDescription::class))
        }
    }

    override fun getLastWeather(): Flow<WeatherCurrent>  = flow {
        weatherDAO.getLast().collect{weather ->
            weatherDAO.getWeatherDescription(weather.weatherDescriptionId).collect{ weatherDescription ->
                emit(WeatherCurrent(weather.dt, weather.city, weather.temp, weather.feels_like, weather.temp_min, weather.temp_max, weather.humidity, WeatherDescription(weatherDescription.id, weatherDescription.main, weatherDescription.description, weatherDescription.icon), weather.iconUrl))
            }
        }
    }

    override fun getTimesList(): Flow<List<Int>> = weatherDAO.getAllDates()

    override fun getWeatherByDate(dt: Int): Flow<WeatherCurrent> = flow {
        weatherDAO.findByDate(dt).collect{weather ->
            weatherDAO.getWeatherDescription(weather.weatherDescriptionId).collect{ weatherDescription ->
                emit(WeatherCurrent(weather.dt, weather.city, weather.temp, weather.feels_like, weather.temp_min, weather.temp_max, weather.humidity, WeatherDescription(weatherDescription.id, weatherDescription.main, weatherDescription.description, weatherDescription.icon), weather.iconUrl))
            }
        }
    }

    override suspend fun saveWeatherFive(weather: WeatherFiveDay) {
        withContext(Dispatchers.IO){
           val weatherItemList: MutableList<WeatherItem>  = mutableListOf()
            val mainDataItems: MutableList<MainData> = mutableListOf()
            val weatherDescriptionItems: MutableList<com.example.localdb.WeatherDescription> = mutableListOf()

            weather.weatherList.forEach{weatherItem->
                weatherItemList.add(WeatherItem(weatherItem.dt, weatherItem.weather.id, weatherItem.dt_txt, weatherItem.iconUrl!!, weather.city))
                mainDataItems.add(weatherItem.main.convertTo(MainData::class, "weatherItemId", weatherItem.dt))
                weatherDescriptionItems.add(weatherItem.weather.convertTo(com.example.localdb.WeatherDescription::class))
            }

            weatherDAO.insertFiveDaysWeatherList(weatherItemList, mainDataItems, weatherDescriptionItems)
        }
    }

    override fun getWeatherFive(): Flow<WeatherFiveDay> = flow {
        val weatherItemList: MutableList<com.example.domain.WeatherItem> = mutableListOf()
        weatherDAO.getFiveDaysWeather().collect{weatherItems ->
           weatherItems.forEach{item ->
               val weatherDescription = weatherDAO.getWeatherDescription(item.weather).first()
               val mainData = weatherDAO.getMainData(item.dt).first()
               weatherItemList.add(com.example.domain.WeatherItem(item.dt, mainData.convertTo(com.example.domain.MainData::class), weatherDescription.convertTo(WeatherDescription::class), item.dt_txt, item.iconUrl))
           }
           if(weatherItemList.isNotEmpty()) emit( WeatherFiveDay(weatherItems.firstOrNull()?.city?:"", weatherItemList))
        }
    }

    override suspend fun saveQuery(q: String) {
        withContext(Dispatchers.IO){
            searchDao.insertQuery(UserQuery(q = q))
        }
    }

    override fun getUserQueries(): Flow<List<String>> = flow {
        searchDao.getQueries().collect{queries->
            emit(queries.map { it.q })
        }
    }


}