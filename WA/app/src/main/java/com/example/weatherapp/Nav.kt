package com.example.weatherapp

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

sealed class Screen(val route: String, val icon: ImageVector, val text: String) {
    class CurrentWeather(context: Context) : Screen(Routes.CURRENT_WEATHER, Icons.Rounded.LocationOn, context.getString(R.string.today))
    class FiveDayWeather(context: Context) : Screen(Routes.FIVE_DAY_WEATHER, Icons.Rounded.DateRange, context.getString(R.string._5_day))
    class SearchWeather(context: Context) : Screen(Routes.SEARCH_WEATHER, Icons.Rounded.Search, context.getString(R.string.search))
}

@Singleton
class ScreenProvider @Inject constructor(@ApplicationContext private val context: Context) {
    val screens = listOf(
        Screen.CurrentWeather(context),
        Screen.FiveDayWeather(context),
        Screen.SearchWeather(context)
    )
}

object Routes {
    const val CURRENT_WEATHER = "current"
    const val FIVE_DAY_WEATHER = "fiveDay"
    const val SEARCH_WEATHER = "search"
}