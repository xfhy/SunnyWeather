package com.xfhy.weather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author : xfhy
 * Create time : 2020-04-23 23:22
 * Description : Retrofit构建器
 */
object ServiceCreator {

    private const val BASE_URl = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //构建serviceClass的动态代理对象
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 泛型实化,,首先是内联函数,其次得加reified关键字,然后可以在函数内获取当前指定泛型的实际类型
     * 内联函数是替换到被调用的地方,所以不存在泛型擦除(泛型对于类型的约束只在编译期存在)问题
     */
    inline fun <reified T> create(): T = create(T::class.java)

}