package com.example.di

import android.content.Context
import com.example.data.ConnectivityRepositoryImpl
import com.example.data.LocalRepositoryImpl
import com.example.data.WeatherRepositoryImpl
import com.example.domain.ConnectivityRepository
import com.example.domain.LocalRepository
import com.example.domain.WeatherRepository
import com.example.localdb.SearchDao
import com.example.localdb.WeatherDao
import com.example.network.retrofit.WeatherApi
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
    fun provideRepository(weatherApi: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(weatherApi = weatherApi)
    }

    @Provides
    @Singleton
    fun provideConnectionRepository(@ApplicationContext context: Context): ConnectivityRepository {
        return ConnectivityRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(weatherDao: WeatherDao, searchDao: SearchDao): LocalRepository {
        return LocalRepositoryImpl(weatherDao, searchDao)
    }
}