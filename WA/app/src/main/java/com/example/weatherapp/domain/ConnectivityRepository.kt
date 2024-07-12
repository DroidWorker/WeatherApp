package com.example.weatherapp.domain

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    val isConnected: Flow<Boolean>
}