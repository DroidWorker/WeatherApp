package com.example.localdb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey val dt: Int,
    val city: String,
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val weatherDescriptionId: Int,
    var iconUrl: String?
)

@Entity
data class WeatherDescription(
    @PrimaryKey val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Entity(tableName = "weatheritems")
data class WeatherItem(
    @PrimaryKey val dt: Int,
    val weather: Int,
    val dt_txt: String,
    val iconUrl: String,
    val city: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WeatherItem::class,
            parentColumns = arrayOf("dt"),
            childColumns = arrayOf("weatherItemId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MainData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weatherItemId: Int,
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

@Entity(tableName = "userquery")
data class UserQuery(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val q: String
)