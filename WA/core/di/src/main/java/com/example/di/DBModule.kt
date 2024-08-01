package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.localdb.AppDatabase
import com.example.localdb.SearchDao
import com.example.localdb.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DBModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideWeatherDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    fun provideSearchDao(database: AppDatabase): SearchDao {
        return database.searchDao()
    }
}