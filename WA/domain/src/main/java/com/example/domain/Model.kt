package com.example.domain

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

data class WeatherResponse(
    val dt: Int,
    val weather: List<WeatherDescription>,
    val main: MainData,
    val visibility: Int,
    val clouds: Clouds,
    val name: String
)
data class FiveDayResponse(
    val cod: String,
    val cnt: Int,
    val list: List<WeatherItemResponse>,
    val city: CIty
)
data class WeatherItemResponse(
    val dt: Int,
    val main: MainData,
    val weather: List<WeatherDescription>,
    val clouds: Clouds,
    val visibility: Int,
    val dt_txt: String
)

data class WeatherCurrent(
    val dt: Int,
    val city: String,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val weather: WeatherDescription,
    var iconUrl: String?
){
    object ModelMapper {
        fun from(form: WeatherResponse) =
            WeatherCurrent(form.dt, form.name, form.main.temp, form.main.feels_like, form.main.temp_min, form.main.temp_max, form.main.humidity, form.weather.first(), null)
    }
}

data class WeatherFiveDay(
    val city: String,
    val weatherList: List<WeatherItem>,
){
    object ModelMapper {
        fun from(form: FiveDayResponse) =
            WeatherFiveDay(form.city.name, form.list.map { WeatherItem.ModelMapper.from(it) })
    }
}

data class WeatherDescription(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainData(
    val temp:Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class WeatherItem(
    val dt: Int,
    val main: MainData,
    val weather: WeatherDescription,
    val dt_txt: String,
    var iconUrl: String?
){
    object ModelMapper {
        fun from(form: WeatherItemResponse) =
            WeatherItem(form.dt, form.main, form.weather.first(), form.dt_txt, null)
    }
}

class Clouds(
    val all: Int
)

data class CIty(
    val id: Int,
    val name: String,
    val timeZone: Int
)

inline fun <reified T : Any, reified R : Any> T.convertTo(
    targetClass: KClass<R>,
    extraParamName: String? = null,
    extraParamValue: Any? = null
): R {
    val constructor = targetClass.primaryConstructor ?: throw IllegalArgumentException("No primary constructor found for $targetClass")
    val parameterMap = constructor.parameters.mapNotNull { param ->
        when (param.name) {
            extraParamName -> param to extraParamValue
            else -> {
                val value = this::class.declaredMemberProperties
                    .firstOrNull { it.name == param.name }?.call(this)
                if (value != null) param to value else null
            }
        }
    }.toMap()
    return constructor.callBy(parameterMap)
}