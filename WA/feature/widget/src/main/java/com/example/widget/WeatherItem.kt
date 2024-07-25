package com.example.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.WeatherItem
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun WeatherItem(weather: WeatherItem){
    Box(modifier = Modifier.padding(4.dp).background(color = Color.White.copy(alpha = 0.15f))) {
        Row (modifier = Modifier.height(IntrinsicSize.Min)){
            Column {
                Text(text = reformatStringDate(weather.dt_txt), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(vertical = 8.dp))
                AsyncImage(
                    model = weather.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = " ${weather.main.temp.roundToInt()}°",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.W700
                    )
                    Text(
                        text = weather.weather.description,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconWithText(
                        ImageVector.vectorResource(id = R.drawable.humidity_percentage_24dp_ffffff_fill0_wght400_grad0_opsz24),
                        weather.main.humidity.toString()
                    )
                    IconWithText(
                        ImageVector.vectorResource(id = R.drawable.min),
                        "${weather.main.temp_min}"
                    )
                    IconWithText(
                        ImageVector.vectorResource(id = R.drawable.max),
                        "${weather.main.temp_max}"
                    )
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun reformatStringDate(date: String): String{
    val inputformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val outputformat = SimpleDateFormat("dd.MM HH:mm")
    val parceDate: Date = inputformat.parse(date)
    return outputformat.format(parceDate)
}