package org.example.project.ui

import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.example.project.data.models.model.WeatherResponse
import org.example.project.data.models.repository.WeatherRepository

class HomeViewModel(private val locationTracker: LocationTracker)  {
    private val repository = WeatherRepository()
    private val _state= MutableStateFlow<HomeScreenStates>(HomeScreenStates.Loading)
    val state = _state.asStateFlow()
    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState = _permissionState.asStateFlow()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            _permissionState.value = locationTracker.permissionsController.getPermissionState(Permission.LOCATION)
        }
    }
    private suspend fun fetchWeather(location: LatLng) {
        _state.value = HomeScreenStates.Loading
        try {
            val result = repository.fetchWeather(location)
            _state.value = HomeScreenStates.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            _state.value = HomeScreenStates.Error("Failed to load weather")
        }
    }
    fun provideLocationPermission(){
        CoroutineScope(Dispatchers.IO).launch {
            val isGranted = locationTracker.permissionsController.isPermissionGranted(Permission.LOCATION)
            if (isGranted){
                _permissionState.value = PermissionState.Granted
                return@launch
            }
            try {
                 locationTracker.permissionsController.providePermission(Permission.LOCATION)
                _permissionState.value = PermissionState.Granted
            }catch (e:DeniedAlwaysException){
                _permissionState.value = PermissionState.DeniedAlways
            }catch (e:DeniedException){
                _permissionState.value = PermissionState.Denied
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateLocationData() {
        CoroutineScope(Dispatchers.Main).launch {
            val latLng = getUserLocation()
            fetchWeather(location = latLng)
        }
    }

    private suspend fun getUserLocation(): LatLng {
        locationTracker.startTracking()
        val location = locationTracker.getLocationsFlow().first()
        locationTracker.stopTracking()
        return location
    }

}

sealed class HomeScreenStates{
    data object Loading:HomeScreenStates()
    data class Success(val data: WeatherResponse):HomeScreenStates()
    data class Error(val message:String):HomeScreenStates()

}



