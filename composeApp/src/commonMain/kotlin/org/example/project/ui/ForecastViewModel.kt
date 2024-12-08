package org.example.project.ui

import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.project.data.models.forcastmodel.ForecastData
import org.example.project.data.models.forcastmodel.ForecastResponse
import org.example.project.data.models.repository.WeatherRepository

class ForecastViewModel(private val locationTracker: LocationTracker) {

    private val repository = WeatherRepository()
    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state = _state.asStateFlow()

     suspend fun getForecast(){
        withContext(Dispatchers.Main){
            _state.value = ForecastState.Loading
            val location = getCurrentLocation()
            try {
                val response =  repository.fetchForecast(location)
                val dailyData = getDailyForecast(response)
                val weeklyData = getWeeklyForecast(response).map { it.value.first()  }
                _state.value = ForecastState.Data(dailyData,weeklyData)
            }catch (e:Exception){
                e.printStackTrace()
                _state.value = ForecastState.Error(e)
            }

        }
    }
    private fun getDailyForecast(response: ForecastResponse):List<ForecastData>{
        val sortedData = response.list.sortedBy { it.dt }.map { it.dt_txt.split(" ")[0] }
        val groupData = response.list.groupBy { it.dt_txt.split(" ")[0] }
        return groupData[sortedData[0]]?: emptyList()
    }

    private fun getWeeklyForecast(response: ForecastResponse):Map<String,List<ForecastData>>{
        val groupData = response.list.sortedBy { it.dt }.groupBy { it.dt_txt.split(" ")[0] }
        return groupData
    }
    private suspend fun getCurrentLocation():LatLng{
        locationTracker.startTracking()
        val location = locationTracker.getLocationsFlow().first()
        locationTracker.stopTracking()
        return location

    }

}

sealed class ForecastState{
    object Loading:ForecastState()
    data class Data(val dailyData:List<ForecastData>, val weeklyData:List<ForecastData>):ForecastState()
    data class Error(val error:Throwable):ForecastState()
}