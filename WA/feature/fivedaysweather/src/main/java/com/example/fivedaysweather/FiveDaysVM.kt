package com.example.fivedaysweather

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.WeatherFiveDay
import com.example.domain.usecase.GetLocalWeatherFiveUC
import com.example.domain.usecase.SaveWeatherFiveUC
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiveDaysVM @Inject constructor(
    private val getFiveDayWeatherUC: com.example.domain.usecase.GetFiveDayWeatherUC,
    private val checkConnectivityUC: com.example.domain.usecase.ObserveConnectivityUC,
    private val getLocalFiveDayWeatherUC: GetLocalWeatherFiveUC,
    private val saveWeatherFiveUC: SaveWeatherFiveUC,
    @ApplicationContext private val context: Context
    ) : ViewModel() {
    private var locationManager : LocationManager? = null

    private val _connectivityState = MutableStateFlow(false)

    //FiveDay weather states
    private val _fiveDayState = MutableStateFlow<WeatherFiveDay?>(null)
    private val _fiveDayErrorState = MutableStateFlow<String?>(null)
    val fiveDayState: StateFlow<WeatherFiveDay?> get() = _fiveDayState
    val fiveDayErrorState: StateFlow<String?> get() = _fiveDayErrorState

    init {
        viewModelScope.launch {
            checkConnectivityUC.invoke().collect { connectivity ->
                _connectivityState.value = connectivity
            }
        }
    }

    private fun getLocation(onReceived: (Pair<Double, Double>?) -> Unit) {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager?

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    println("${location.latitude}   ${location.longitude}")
                    onReceived(location.latitude to location.longitude)
                } else {
                    // Обработка случая, когда местоположение не может быть найдено
                    onReceived(null)
                }
            }
        } else {
            onReceived(null)
        }
    }

    fun getWeatherFiveDay(){
        if(!_connectivityState.value){
            loadSavedData()
        }else {
            getLocation { coords ->
                if (coords != null && coords.first != -1.0) viewModelScope.launch {
                    getFiveDayWeatherUC.invoke(coords.first, coords.second).collect { weather ->
                        _fiveDayState.value = weather
                        saveWeatherFiveUC.invoke(weather)
                    }
                } else _fiveDayErrorState.value = context.getString(R.string.cant_get_location)
            }
        }
    }

    private fun loadSavedData(){
        viewModelScope.launch {
            getLocalFiveDayWeatherUC.invoke().collect{v ->
                _fiveDayState.value = v
            }
        }
    }
}