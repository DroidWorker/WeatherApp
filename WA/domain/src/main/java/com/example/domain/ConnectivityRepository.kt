package com.example.domain

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    val isConnected: Flow<Boolean>
}