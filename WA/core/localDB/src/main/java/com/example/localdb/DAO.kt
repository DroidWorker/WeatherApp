package com.example.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT dt FROM weather")
    fun getAllDates(): Flow<List<Int>>

    @Query("SELECT * FROM weather ORDER BY dt DESC LIMIT 1")
    fun getLast(): Flow<Weather>

    @Query("SELECT * FROM weatherdescription WHERE id = :id LIMIT 1")
    fun getWeatherDescription(id: Int): Flow<WeatherDescription>

    @Query("SELECT * FROM weather WHERE dt = :dt LIMIT 1")
    fun findByDate(dt: Int): Flow<Weather>

    @Query("SELECT * FROM weatheritems")
    fun getFiveDaysWeather(): Flow<List<WeatherItem>>

    @Query("SELECT * FROM MainData where weatherItemId = :id")
    fun getMainData(id: Int): Flow<MainData>

    @Query("DELETE FROM weatheritems")
    fun deleteFiveDaysWeather()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherDescription(weather: WeatherDescription)

    @Insert
    fun insertWeatherItem(weatherItem: WeatherItem)

    @Insert
    fun insertMainData(mainData: MainData)

    @Transaction
    fun insertFiveDaysWeatherList(
        items: List<WeatherItem>,
        mainDataItems: List<MainData>,
        weatherDescriptionItems: List<WeatherDescription>
    ) {
        if (items.size == mainDataItems.size && mainDataItems.size == weatherDescriptionItems.size) {
            deleteFiveDaysWeather()
            for (i in items.indices) {
                insertWeatherItem(items[i])
                insertMainData(mainDataItems[i])
                insertWeatherDescription(weatherDescriptionItems[i])
            }
        }
    }
}

@Dao
interface SearchDao {
    @Query("SELECT * FROM userquery")
    fun getQueries(): Flow<List<UserQuery>>

    @Insert
    fun insertQuery(q: UserQuery)
}