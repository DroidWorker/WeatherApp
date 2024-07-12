package com.example.weatherapp.di

import com.example.weatherapp.domain.ConnectivityRepository
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.usecase.GetFivedayWeatherUC
import com.example.weatherapp.domain.usecase.GetWeatherTodayUC
import com.example.weatherapp.domain.usecase.ObserveConnectivityUC
import com.example.weatherapp.domain.usecase.SearchWeatherByCityUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetWeatherTodayUC(repository: WeatherRepository): GetWeatherTodayUC{
        return GetWeatherTodayUC(repository)
    }

    @Provides
    fun provideSearchWeatherByCityUC(repository: WeatherRepository): SearchWeatherByCityUC{
        return SearchWeatherByCityUC(repository)
    }

    @Provides
    fun provideGetFivedayWeatherUC(repository: WeatherRepository): GetFivedayWeatherUC{
        return GetFivedayWeatherUC(repository)
    }

    @Provides
    fun provideObserveConnectivityUC(repository: ConnectivityRepository): ObserveConnectivityUC{
        return ObserveConnectivityUC(repository)
    }
}