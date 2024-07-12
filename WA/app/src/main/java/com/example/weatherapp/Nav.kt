package com.example.weatherapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val text: String) {
    object CurrentWeather : Screen("current", Icons.Rounded.LocationOn, "сегодня")
    object FiveDayWeather : Screen("fiveday", Icons.Rounded.DateRange, "5 дней")
    object SearchWeather : Screen("search", Icons.Rounded.Search, "поиск")
}