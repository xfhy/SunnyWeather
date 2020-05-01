package com.xfhy.weather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.xfhy.weather.SunnyWeatherApplication
import com.xfhy.weather.logic.model.Place

/**
 * @author : xfhy
 * Create time : 2020-05-01 17:03
 * Description : 存储和读取本地数据
 * 这个是提供给Repository使用的,请勿直接使用
 */
object PlaceDao {

    private val GSON_INSTANCE = Gson()
    private val SP_PLACE_KEY = "place"

    /**
     * 将place对象序列化成json然后保存到SP
     */
    fun sacePlace(place: Place) {
        sharedPreferences().edit {
            putString(SP_PLACE_KEY, GSON_INSTANCE.toJson(place))
        }
    }

    /**
     * 从sp中获取Place(天气)数据
     */
    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString(SP_PLACE_KEY, "") ?: ""
        return GSON_INSTANCE.fromJson(placeJson, Place::class.java)
    }

    /**
     * 之前是否有保存天气数据到本地?
     */
    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

}