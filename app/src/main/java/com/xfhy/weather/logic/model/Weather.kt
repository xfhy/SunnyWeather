package com.xfhy.weather.logic.model

/**
 * @author : xfhy
 * Create time : 2020-04-28 22:46
 * Description : 封装一些天气信息
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)