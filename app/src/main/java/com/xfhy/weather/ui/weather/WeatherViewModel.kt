package com.xfhy.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.xfhy.weather.logic.Repository
import com.xfhy.weather.logic.model.Location

/**
 * @author : xfhy
 * Create time : 2020-04-29 23:12
 * Description :
 */
class WeatherViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    //观察locationLiveData
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }

}