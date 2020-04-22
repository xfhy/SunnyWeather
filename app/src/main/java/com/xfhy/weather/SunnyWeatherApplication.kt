package com.xfhy.weather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author : xfhy
 * Create time : 2020-04-23 00:00
 * Description :
 */
class SunnyWeatherApplication : Application() {

    companion object {
        const val TOKEN = ""// 填入你申请到的令牌值
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}