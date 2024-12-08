package org.example.project.data.models.network

import dev.icerock.moko.geo.LatLng
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.data.models.forcastmodel.ForecastResponse
import org.example.project.data.models.model.WeatherResponse

class ApiService {

    private val httpClient = HttpClient{
        install(ContentNegotiation){
            json(Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "https://api.openweathermap.org/data/2.5"

    suspend fun getWeather(location : LatLng): WeatherResponse {
        try {
            return httpClient.get("$baseUrl/weather"){
                parameter("appid", apikey)
                parameter("units", "metric")
                parameter("lat",location.latitude)
                parameter("lon",location.longitude)
            }.body()
        }catch (e:ClientRequestException){
            throw Exception("Error fetching weather data: ${e.response.status}")
        }

    }
    suspend fun getForecast(location: LatLng):ForecastResponse{
        try {
            return httpClient.get("$baseUrl/forecast"){
                parameter("appid", apikey)
                parameter("units", "metric")
                parameter("lat",location.latitude)
                parameter("lon",location.longitude)
            }.body()
        }catch (e:ClientRequestException){
            throw Exception("Error fetching forecast data: ${e.response.status}")
        }

    }
}