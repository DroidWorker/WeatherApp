package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.searchweather.R
import com.example.searchweather.SearchVM
import com.example.widgets.IconWithText
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier, changeBackground: (Color) -> Unit) {
    val vm: SearchVM = hiltViewModel()
    val weatherState by vm.searchedWeatherState.collectAsState()
    val searchText by vm.searchText.collectAsState()
    val inSearch by vm.inSearchState.collectAsState()
    val errorState by vm.inError.collectAsState()
    val isFirstLaunch by vm.isFirstLaunch.collectAsState()

    weatherState?.let {
        changeBackground(vm.getBgColor(it.weather.icon))
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SearchBar(
            query = searchText, // текст, отображаемый в SearchBar
            onQueryChange = { q -> vm.onSearchTextChange(q) }, // обновление значения searchText
            onSearch = vm::onPressSearch, // обратный вызов, который будет вызван, когда служба ввода активирует действие ImeAction.Search
            active = inSearch, //выполняет ли пользователь поиск или нет
            onActiveChange = { }, //обратный вызов, который будет вызван при изменении активного состояния этой строки поиска
            trailingIcon = {
                TextButton(
                    onClick = {
                    vm.onPressSearch(searchText)
                }
                ) {
                    Icon(Icons.Rounded.Search, contentDescription = "")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){}
        if(weatherState!=null){
            Column (
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp)) {
                    Text(text = vm.getTime(weatherState!!.dt), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = weatherState!!.city, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                }
                Row( verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = weatherState!!.iconUrl,
                        contentDescription = null,
                        modifier= Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(text = " ${weatherState!!.temp.roundToInt()}°",  color = Color.White, fontSize = 45.sp, fontWeight = FontWeight.W700)
                        Text(text = "Ощущается как ${weatherState!!.feels_like}", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
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
            }
        }else if(errorState!=null){
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Text(errorState ?: "")
            }
        } else if(!isFirstLaunch) CircularProgressIndicator()
    }
}