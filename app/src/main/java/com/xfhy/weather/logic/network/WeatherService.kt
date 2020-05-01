package com.xfhy.weather.logic.network

import com.xfhy.weather.SunnyWeatherApplication
import com.xfhy.weather.logic.model.DailyResponse
import com.xfhy.weather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author : xfhy
 * Create time : 2020-04-28 22:47
 * Description :
 */
interface WeatherService {

    /**
     * 根据经纬度获取实时天气信息
     */
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    /**
     * 根据经纬度获取未来几天的天气信息
     */
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

}