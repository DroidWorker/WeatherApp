package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.widgets.IconWithText
import kotlin.math.roundToInt

@Composable
fun TodayScreen(vm: MainViewModel, modifier: Modifier, changeBackground: (Color) -> Unit) {
    val weatherState by vm.weatherState.collectAsState()
    val errorState by vm.errorState.collectAsState()
    LaunchedEffect(Unit) {
        vm.getWeatherToday()
    }
    weatherState?.let {
        changeBackground(vm.getBgColor(it.weather.icon))
    }

    if(weatherState!=null) Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = vm.getCurrentFormattedDate(), color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        Text(text = vm.getTime(weatherState!!.dt), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(vertical = 8.dp))
        Text(text = weatherState!!.city, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        Box(modifier = Modifier.weight(1f))
        AsyncImage(
            model = weatherState!!.iconUrl,
            contentDescription = null,
            modifier= Modifier.size(200.dp)
        )
        Text(text = " ${weatherState!!.temp.roundToInt()}°",  color = Color.White, fontSize = 45.sp, fontWeight = FontWeight.W700)
        Box(modifier = Modifier.weight(1f))
        Text(text = "Ощущается как ${weatherState!!.feels_like}", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 40.dp))
        Row (
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconWithText(ImageVector.vectorResource(id = R.drawable.humidity_percentage_24dp_ffffff_fill0_wght400_grad0_opsz24), weatherState!!.humidity.toString())
            IconWithText(ImageVector.vectorResource(id = R.drawable.min), "${weatherState!!.temp_min}")
            IconWithText(ImageVector.vectorResource(id = R.drawable.max), "${weatherState!!.temp_max}")
            Text(text = weatherState!!.weather.description, color = Color.White, fontSize = 20.sp)
        }
    } else if(errorState!=null){
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Text(errorState ?: "")
        }
    } else Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}