package com.example.weatherapp.data

import android.content.Context
import android.net.ConnectivityManager
import com.example.weatherapp.domain.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ConnectivityRepositoryImpl(context: Context): ConnectivityRepository{
    private  val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private  val _isConnected = MutableStateFlow( false )
    override val isConnected: Flow<Boolean> = _isConnected

    init {
        // Наблюдаем изменения сетевого подключения
        connectivityManager.registerDefaultNetworkCallback( object : ConnectivityManager.NetworkCallback() {
            override  fun  onAvailable (network: android . net . Network ) {
                _isConnected.value = true
            }

            override  fun  onLost (network: android . net . Network ) {
                _isConnected.value = false
            }
        })
    }
}