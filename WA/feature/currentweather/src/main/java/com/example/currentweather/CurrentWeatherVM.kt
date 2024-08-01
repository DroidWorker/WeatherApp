package com.example.currentweather

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetLocalWeatherUC
import com.example.domain.usecase.GetWeatherTodayUC
import com.example.domain.usecase.ObserveConnectivityUC
import com.example.domain.usecase.SaveWeatherUC
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherVM @Inject constructor(
    private val weatherTodayUC: GetWeatherTodayUC,
    private val checkConnectivityUC: ObserveConnectivityUC,
    private val saveWeatherUC: SaveWeatherUC,
    private val getLocalWeatherUC: GetLocalWeatherUC,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var locationManager: LocationManager? = null

    val connectivityState = MutableStateFlow(false)

    //CurrentWeather states
    private val _weatherState = MutableStateFlow<com.example.domain.WeatherCurrent?>(null)
    private val _errorState = MutableStateFlow<String?>(null)
    val weatherState: StateFlow<com.example.domain.WeatherCurrent?> get() = _weatherState
    val errorState: StateFlow<String?> get() = _errorState

    private val _timesState = MutableStateFlow<List<Int>>(emptyList())
    val timesState: StateFlow<List<Int>> get() = _timesState

    init {
        viewModelScope.launch {
            checkConnectivityUC.invoke().collect { connectivity ->
                connectivityState.value = connectivity
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

    fun getWeatherToday() {
        if (!connectivityState.value) {
            loadSavedData()
        } else {
            getLocation { coords ->
                if (coords != null && coords.first != -1.0) viewModelScope.launch {
                    weatherTodayUC.invoke(coords.first, coords.second).collect { weather ->
                        _weatherState.value = weather
                        saveWeatherUC.invoke(weather)
                    }
                } else _errorState.value = context.getString(R.string.cant_get_location)
            }
        }
    }

    private fun loadSavedData() {
        viewModelScope.launch {
            getLocalWeatherUC.invoke().collect { weather ->
                _weatherState.value = weather
            }
        }
        viewModelScope.launch {
            getLocalWeatherUC.getTimes().collect { times ->
                _timesState.value = times.reversed()
            }
        }
    }

    fun loadSavedWeatherByDate(dt: Int) {
        viewModelScope.launch {
            getLocalWeatherUC.getWeatherByDate(dt).collect { weather ->
                _weatherState.value = weather
            }
        }
    }

    fun getCurrentFormattedDate(): String {
        val currentDateTime = Date()
        val dateFormat =
            SimpleDateFormat(context.getString(R.string.eeee_d_mmmm), Locale("ru", "RU"))
        return dateFormat.format(currentDateTime)
    }

    fun getTime(utc: Int): String {
        val timeInMillis = (utc + 10800) * 1000L
        val dateFormat = SimpleDateFormat(context.getString(R.string.hh_mm), Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Устанавливаем UTC временную зону

        return dateFormat.format(Date(timeInMillis))
    }

    fun getDate(utc: Int): String {
        val timeInMillis = (utc + 10800) * 1000L
        val dateFormat =
            SimpleDateFormat(context.getString(R.string.eeee_d_mmmmhh_mm), Locale("ru", "RU"))
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Устанавливаем UTC временную зону

        return dateFormat.format(Date(timeInMillis))
    }

    fun getBgColor(code: String): Color {
        return when (code) {
            "01d" -> Color(252, 162, 85, 255)
            "02d" -> Color.Magenta
            "03d" -> Color.Cyan
            "04d" -> Color(41, 123, 177, 255)
            "09d" -> Color.Blue
            "10d" -> Color(0, 81, 134, 255)
            "11d" -> Color(0, 29, 48, 255)
            "13d" -> Color(133, 200, 245, 255)
            "50d" -> Color(255, 254, 157, 255)
            else -> Color.Gray
        }
    }
}