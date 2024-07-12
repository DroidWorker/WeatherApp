package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.ConnectivityRepositoryImpl
import com.example.weatherapp.data.WeatherRepositoryImpl
import com.example.network.retrofit.WeatherApi
import com.example.weatherapp.domain.ConnectivityRepository
import com.example.weatherapp.domain.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(weatherApi: com.example.network.retrofit.WeatherApi):WeatherRepository {
        return WeatherRepositoryImpl(weatherApi = weatherApi)
    }

    @Provides
    @Singleton
    fun provideConnectionRepository(@ApplicationContext context: Context):ConnectivityRepository {
        return ConnectivityRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): com.example.network.retrofit.WeatherApi {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val weatherApi = retrofit.create(com.example.network.retrofit.WeatherApi::class.java)
        return weatherApi
    }

}