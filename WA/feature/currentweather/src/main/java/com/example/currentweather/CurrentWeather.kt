package com.example.currentweather

import android.util.DisplayMetrics
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.util.TypedValueCompat.pxToDp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.widget.IconWithText
import com.example.widget.R
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TodayScreen(modifier: Modifier, changeBackground: (Color) -> Unit) {
    val vm: CurrentWeatherVM = hiltViewModel()
    val weatherState by vm.weatherState.collectAsState()
    val errorState by vm.errorState.collectAsState()
    val connectivityState by vm.connectivityState.collectAsState()

    var expanded by remember{ mutableStateOf(false) }
    val timesState by vm.timesState.collectAsState()
    var selectedOption by remember { mutableIntStateOf(0) }
    if(timesState.isNotEmpty()&&selectedOption==0)selectedOption=timesState.first()

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
        var offset = Offset.Zero
        if(connectivityState)Text(text = vm.getCurrentFormattedDate(), color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        else Card(
            colors = CardColors(containerColor = Color.Transparent, contentColor = Color.Transparent, disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent ),
            modifier = Modifier
            .clickable { expanded = true }
                .align(Alignment.CenterHorizontally)
            .pointerInteropFilter {
                offset = Offset(it.x, it.y)
                false
            }
            ) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = vm.getDate(selectedOption), color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                Icon(Icons.Sharp.KeyboardArrowDown,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset((pxToDp(offset.x, DisplayMetrics())).dp, (pxToDp(offset.y, DisplayMetrics())).dp),
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false)
            ) {
                timesState.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = vm.getDate(option))},
                        onClick = {
                            selectedOption = option
                            expanded = false
                            vm.loadSavedWeatherByDate(option)
                        }
                    )
                }
            }
        }
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
        Text(text = "Ощущается как ${weatherState!!.feelsLike}", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 40.dp))
        Row (
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconWithText(
                ImageVector.vectorResource(id = R.drawable.humidity_percentage_24dp_ffffff_fill0_wght400_grad0_opsz24),
                weatherState!!.humidity.toString()
            )
            IconWithText(
                ImageVector.vectorResource(id = R.drawable.min),
                "${weatherState!!.tempMin}"
            )
            IconWithText(
                ImageVector.vectorResource(id = R.drawable.max),
                "${weatherState!!.tempMax}"
            )
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