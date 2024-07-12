package com.example.domain.usecase

import com.example.domain.ConnectivityRepository
import kotlinx.coroutines.flow.Flow

class ObserveConnectivityUC(private val repository: ConnectivityRepository) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isConnected
    }
}