package org.example.project.data.models.forcastmodel

import kotlinx.serialization.Serializable
import org.example.project.data.models.model.Clouds
import org.example.project.data.models.model.Main
import org.example.project.data.models.model.Rain
import org.example.project.data.models.model.Sys
import org.example.project.data.models.model.Weather
import org.example.project.data.models.model.Wind

@Serializable
data class ForecastData(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain? = null,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)