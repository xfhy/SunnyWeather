package com.xfhy.weather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author : xfhy
 * Create time : 2020-04-23 23:51
 * Description : 网络请求
 */
object SunnyWeatherNetwork {

    //因为ServiceCreator封装了2个create函数,且功能相同,所以以下两句代码是相同的作用
    private val placeService = ServiceCreator.create<PlaceService>()
    //private val placeService = ServiceCreator.create(PlaceService::class.java)

    //首先通过placeService.searchPlaces获取Call对象,然后调用扩展了Call的await方法,进行网络请求
    //当外部调用searchPlaces方法时,Retrofit会立即发起网络请求,同时当前协程会被阻塞住.直到服务器响应请求,await函数会将解析出来的数据模型对象取出并返回,同时恢复协程
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    /**
     * 扩展Retrofit的Call  让其拥有await方法
     * 简化网络请求,避免每次写回调
     */
    private suspend fun <T> Call<T>.await(): T {
        //将异步网络请求放入suspendCoroutine,这里会将Lambda表达式的内容在普通线程中执行,当前协程会被挂起,然后调用continuation.resume()才会恢复当前协程
        return suspendCoroutine { continuation ->
            //因为是扩展函数,所以有Call的上下文环境,所以可以直接调用enqueue方法进行异步网络请求
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }
            })
        }
    }

}