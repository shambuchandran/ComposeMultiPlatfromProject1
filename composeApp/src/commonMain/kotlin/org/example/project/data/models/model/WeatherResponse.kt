package org.example.project.data.models.model
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
//    val base: String?,
//    val clouds: Clouds? = null,
//    val cod: Int?,
//    val coord: Coord? = null,
//    val dt: Int?,
//    val id: Int?,
    val main: Main? ,
    val name: String?,
//    val rain: Rain? = null,
//    val sys: Sys?,
//    val timezone: Int?,
    val visibility: Int?,
    val weather: List<Weather> ,
    val wind: Wind?
)