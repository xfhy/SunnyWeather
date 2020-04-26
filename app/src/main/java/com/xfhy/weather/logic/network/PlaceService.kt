package com.xfhy.weather.logic.network

import com.xfhy.weather.SunnyWeatherApplication
import com.xfhy.weather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author : xfhy
 * Create time : 2020-04-23 00:21
 * Description : 接口
 */
interface PlaceService {

    /**
     * 搜索城市数据
     */
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

    //Retrofit 2.6之后是直接支持协程的

}