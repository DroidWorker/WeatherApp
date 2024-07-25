package com.example.di

import com.example.domain.ConnectivityRepository
import com.example.domain.LocalRepository
import com.example.domain.WeatherRepository
import com.example.domain.usecase.GetFiveDayWeatherUC
import com.example.domain.usecase.GetLocalWeatherFiveUC
import com.example.domain.usecase.GetLocalWeatherUC
import com.example.domain.usecase.GetWeatherTodayUC
import com.example.domain.usecase.ObserveConnectivityUC
import com.example.domain.usecase.SaveWeatherFiveUC
import com.example.domain.usecase.SaveWeatherUC
import com.example.domain.usecase.SearchWeatherByCityUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetWeatherTodayUC(repository: WeatherRepository): GetWeatherTodayUC {
        return GetWeatherTodayUC(repository)
    }

    @Provides
    fun provideSearchWeatherByCityUC(repository: WeatherRepository, localRepository: LocalRepository): SearchWeatherByCityUC {
        return SearchWeatherByCityUC(repository, localRepository)
    }

    @Provides
    fun provideGetFiveDayWeatherUC(repository: WeatherRepository): GetFiveDayWeatherUC {
        return GetFiveDayWeatherUC(repository)
    }

    @Provides
    fun provideObserveConnectivityUC(repository: ConnectivityRepository): ObserveConnectivityUC {
        return ObserveConnectivityUC(repository)
    }

    @Provides
    fun provideGetLocalWeatherUC(repository: LocalRepository): GetLocalWeatherUC {
        return GetLocalWeatherUC(repository)
    }

    @Provides
    fun provideSaveLocalWeatherUC(repository: LocalRepository): SaveWeatherUC {
        return SaveWeatherUC(repository)
    }

    @Provides
    fun provideGetLocalWeatherFiveUC(repository: LocalRepository): GetLocalWeatherFiveUC {
        return GetLocalWeatherFiveUC(repository)
    }

    @Provides
    fun provideSaveWeatherFiveUC(repository: LocalRepository): SaveWeatherFiveUC {
        return SaveWeatherFiveUC(repository)
    }
}