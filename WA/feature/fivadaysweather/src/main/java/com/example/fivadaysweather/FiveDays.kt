package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fivadaysweather.FiveDaysVM
import com.example.widgets.WeatherItem

@Composable
fun FiveDaysScreen(modifier: Modifier, changeBackground: (Color) -> Unit) {
    val vm: FiveDaysVM = hiltViewModel()
    val weatherState by vm.fiveDayState.collectAsState()
    val errorState by vm.fiveDayErrorState.collectAsState()
    LaunchedEffect(Unit) {
        vm.getWeatherFiveDay()
    }

    if(weatherState!=null) Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "В ближайшие 5 дней", fontSize = 30.sp, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        Text(text = weatherState!!.city, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        LazyColumn {
            items(weatherState!!.weatherList.size){item ->
                WeatherItem(weather = weatherState!!.weatherList[item], vm)
            }
        }
    } else if(errorState!=null){
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Text(errorState ?: "")
        }
    } else CircularProgressIndicator()
}