package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.ConnectivityRepository
import kotlinx.coroutines.flow.Flow

class ObserveConnectivityUC(private val repository: ConnectivityRepository) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isConnected
    }
}