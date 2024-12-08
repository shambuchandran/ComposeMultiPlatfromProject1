package org.example.project.data.models.repository

import dev.icerock.moko.geo.LatLng
import org.example.project.data.models.network.ApiService

class WeatherRepository {

    private val apiService = ApiService()
    suspend fun fetchWeather(location: LatLng) = apiService.getWeather(location)
    suspend fun fetchForecast(location: LatLng) = apiService.getForecast(location)
}