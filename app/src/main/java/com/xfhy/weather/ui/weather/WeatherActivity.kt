package com.xfhy.weather.ui.weather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xfhy.weather.R
import com.xfhy.weather.logic.model.Weather
import com.xfhy.weather.logic.model.getSky
import com.xfhy.weather.ui.common.toast
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 天气详情页
 */
class WeatherActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val LOCATION_LNG_KEY = "location_lng"
        const val LOCATION_LAT_KEY = "location_lat"
        const val PLACE_NAME_KEY = "place_name"

        fun startWeatherActivity(context: Context?, lng: String, lat: String, placeName: String) {
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra(LOCATION_LNG_KEY, lng)
                putExtra(LOCATION_LAT_KEY, lat)
                putExtra(PLACE_NAME_KEY, placeName)
            }
            context?.startActivity(intent)
        }

    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initStatusBar()

        setContentView(R.layout.activity_weather)

        initView()
        initIntent()
        initData()
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
    }

    private fun initIntent() {
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra(LOCATION_LNG_KEY) ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra(LOCATION_LAT_KEY) ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra(PLACE_NAME_KEY) ?: ""
        }
    }

    private fun initStatusBar() {
        //Activity的布局展示到状态栏上
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun initData() {
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather == null) {
                toast("无法成功获取天气信息")
                result.exceptionOrNull()?.printStackTrace()
            } else {
                showWeatherInfo(weather)
            }
            swipeRefresh.isRefreshing = false
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

}
