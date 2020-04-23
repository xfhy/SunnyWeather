package com.xfhy.weather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @author : xfhy
 * Create time : 2020-04-23 00:19
 * Description : 数据
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String,
    val location: Location, @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)