package com.xfhy.weather.logic

import androidx.lifecycle.liveData
import com.xfhy.weather.logic.model.Place
import com.xfhy.weather.logic.model.Weather
import com.xfhy.weather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * @author : xfhy
 * Create time : 2020-04-25 22:26
 * Description : 仓库层  操作本地数据或者网络数据
 */
object Repository {

    //这里liveData是一个函数,是lifecycle-livedata-ktx库提供的,它会自动构建一个LiveData对象并返回
    //在liveData函数的代码块中提供了挂起函数的上下文(LiveDataScope),可以在里面调用任意挂起函数
    //因为是网络请求  所以放子线程里面去
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            //用Kotlin内置的Result包装一下结果
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /**
     * 刷新天气信息
     * 仓库层这里并没有提供获取实时天气信息和未来天气信息的方法,而是提供一个refreshWeather统一请求,统一拿到结果,然后封装到Weather里面
     * 这样调用方就不需要调用2次请求才能拿到结果. 最好的方式就是放仓库层进行一次统一的封装
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        //创建子协程 阻塞当前协程
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            //这里需要同时拿到结果才行,所以用async的await
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    /**
     * 将try..catch封装起来  免得每次都去写一下
     * 这里加上suspend是因为传入的Lambda表达式也需要在挂起函数中运行   拥有挂起函数上下文
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }

        //这里的emit方法类似于LiveData的setValue方法
        //将包装结果发射出去
        emit(result)
    }


}