package com.xfhy.weather.logic

import androidx.lifecycle.liveData
import com.xfhy.weather.logic.model.Place
import com.xfhy.weather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

/**
 * @author : xfhy
 * Create time : 2020-04-25 22:26
 * Description : 仓库层  操作本地数据或者网络数据
 */
object Repository {

    //这里liveData是一个函数,是lifecycle-livedata-ktx库提供的,它会自动构建一个LiveData对象并返回
    //在liveData函数的代码块中提供了挂起函数的上下文(LiveDataScope),可以在里面调用任意挂起函数
    //因为是网络请求  所以放子线程里面去
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                //用Kotlin内置的Result包装一下结果
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }

        //这里的emit方法类似于LiveData的setValue方法
        //将包装结果发射出去
        emit(result)
    }

}