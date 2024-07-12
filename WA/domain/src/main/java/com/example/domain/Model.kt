package com.example.domain

data class WeatherResponce(
    val dt: Int,
    val weather: List<WeatherDescription>,
    val main: MainData,
    val visibility: Int,
    val clouds: Clouds,
    val name: String
)
data class FivedayResponce(
    val cod: String,
    val cnt: Int,
    val list: List<WeatherItemResponce>,
    val city: CIty
)
data class WeatherItemResponce(
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
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val weather: WeatherDescription,
    var iconUrl: String?
){
    object ModelMapper {
        fun from(form: WeatherResponce) =
            WeatherCurrent(form.dt, form.name, form.main.temp, form.main.feels_like, form.main.temp_min, form.main.temp_max, form.main.humidity, form.weather.first(), null)
    }
}

data class WeatherFiveday(
    val city: String,
    val weatherList: List<WeatherItem>,
){
    object ModelMapper {
        fun from(form: FivedayResponce) =
            WeatherFiveday(form.city.name, form.list.map { WeatherItem.ModelMapper.from(it) })
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
        fun from(form: WeatherItemResponce) =
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