package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.WeatherCurrent
import com.example.weatherapp.domain.WeatherFiveday
import com.example.weatherapp.domain.usecase.GetFivedayWeatherUC
import com.example.weatherapp.domain.usecase.GetWeatherTodayUC
import com.example.weatherapp.domain.usecase.ObserveConnectivityUC
import com.example.weatherapp.domain.usecase.SearchWeatherByCityUC
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
class MainViewModel @Inject constructor(
    private val weatherTodayUC: GetWeatherTodayUC,
    private val searchWeatherByCityUC: SearchWeatherByCityUC,
    private val getFivedayWeatherUC: GetFivedayWeatherUC,
    private val checkConnectivityUC: ObserveConnectivityUC,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var locationManager : LocationManager? = null

    private val _connectivityState = MutableStateFlow<Boolean>(false)
    val connectivityState: StateFlow<Boolean> get() = _connectivityState

    //CurrentWeather states
    private val _weatherState = MutableStateFlow<WeatherCurrent?>(null)
    private val _errorState = MutableStateFlow<String?>(null)
    val weatherState: StateFlow<WeatherCurrent?> get() = _weatherState
    val errorState: StateFlow<String?> get() = _errorState

    //FiveDay weather states
    private val _fiveDayState = MutableStateFlow<WeatherFiveday?>(null)
    private val _fiveDayErrorState = MutableStateFlow<String?>(null)
    val fiveDayState: StateFlow<WeatherFiveday?> get() = _fiveDayState
    val fiveDayErrorState: StateFlow<String?> get() = _fiveDayErrorState

    //Search states
    private val _searchedWeatherState = MutableStateFlow<WeatherCurrent?>(null)
    private val _inSearchState = MutableStateFlow<Boolean>(false)
    private val _inError = MutableStateFlow<String?>(null)
    private val _isFirstLaunch = MutableStateFlow<Boolean>(true)
    private val _searchText = MutableStateFlow<String>("")
    val searchedWeatherState: StateFlow<WeatherCurrent?> get() = _searchedWeatherState
    val inSearchState: StateFlow<Boolean> get() = _inSearchState
    val inError: StateFlow<String?> get() = _inError
    val isFirstLaunch: StateFlow<Boolean> get() = _isFirstLaunch
    val searchText: StateFlow<String> get() = _searchText

    init {
        viewModelScope.launch {
            checkConnectivityUC.invoke().collect { connectivity ->
                _connectivityState.value = connectivity
            }
        }
    }

    fun getLocation(onReceived: (Pair<Double, Double>?) -> Unit) {
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
        getLocation { coords ->
            if (coords != null && coords.first != -1.0) viewModelScope.launch {
                weatherTodayUC.invoke(coords.first, coords.second).collect { weather ->
                    _weatherState.value = weather
                }
            } else _errorState.value = "Невозможно получить местоположение"
        }
    }

    fun getWeatherFiveDay(){
        getLocation { coords ->
            if (coords != null && coords.first != -1.0) viewModelScope.launch {
                getFivedayWeatherUC.invoke(coords.first, coords.second).collect { weather ->
                    _fiveDayState.value = weather
                }
            } else _fiveDayErrorState.value = "Невозможно получить местоположение"
        }
    }

    fun searchWeather(){
        _isFirstLaunch.value = false
        viewModelScope.launch {
            searchWeatherByCityUC.invoke(searchText.value).collect { weather ->
                _searchedWeatherState.value = weather
                if(weather==null) _inError.value="По данному запросу ничего не найдено"
                _inSearchState.value = false
            }
        }
    }
    fun  onSearchTextChange (q: String) {
        _searchText.value = q
    }

    fun  onPressSearch (q: String) {
        _searchText.value = q
        _inSearchState.value = true
        searchWeather()
    }

    @SuppressLint("SimpleDateFormat")
    fun reformatStringDate(date: String): String{
        val inputformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val outputformat = SimpleDateFormat("dd.MM HH:mm")
        val parceDate: Date = inputformat.parse(date)
        return outputformat.format(parceDate)
    }

    fun getCurrentFormattedDate(): String {
        val currentDateTime = Date()
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale("ru", "RU"))
        return dateFormat.format(currentDateTime)
    }

    fun getTime(utc: Int): String {
        val timeInMillis = (utc + 10800) * 1000L
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
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