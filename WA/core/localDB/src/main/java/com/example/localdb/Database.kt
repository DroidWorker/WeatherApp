package com.example.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Weather::class, WeatherDescription::class, WeatherItem::class, MainData::class, UserQuery::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun searchDao(): SearchDao
}