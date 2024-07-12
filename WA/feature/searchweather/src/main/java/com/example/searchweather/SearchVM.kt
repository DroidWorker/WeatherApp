package com.example.searchweather

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SearchWeatherByCityUC
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
class SearchVM @Inject constructor(
    private val searchWeatherByCityUC: SearchWeatherByCityUC,
    private val checkConnectivityUC: com.example.domain.usecase.ObserveConnectivityUC
): ViewModel() {

    //Search states
    private val _searchedWeatherState = MutableStateFlow<com.example.domain.WeatherCurrent?>(null)
    private val _inSearchState = MutableStateFlow<Boolean>(false)
    private val _inError = MutableStateFlow<String?>(null)
    private val _isFirstLaunch = MutableStateFlow<Boolean>(true)
    private val _searchText = MutableStateFlow<String>("")
    val searchedWeatherState: StateFlow<com.example.domain.WeatherCurrent?> get() = _searchedWeatherState
    val inSearchState: StateFlow<Boolean> get() = _inSearchState
    val inError: StateFlow<String?> get() = _inError
    val isFirstLaunch: StateFlow<Boolean> get() = _isFirstLaunch
    val searchText: StateFlow<String> get() = _searchText

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
    fun getTime(utc: Int): String {
        val timeInMillis = (utc + 10800) * 1000L
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Устанавливаем UTC временную зону

        return dateFormat.format(Date(timeInMillis))
    }

}