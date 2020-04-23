
## 1. Retrofit构建器

首先是常规操作,利用retrofit构建interface动态代理对象
```kotlon
//构建serviceClass的动态代理对象
fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
```

然后是利用泛型实化,因为传入的泛型T,我们是能够在内联函数(加了reified关键字)中获取到T的实际类型的,所以可以简写成如下形式

```kotlin
/**
 * 泛型实化,,首先是内联函数,其次得加reified关键字,然后可以在函数内获取当前指定泛型的实际类型
 * 内联函数是替换到被调用的地方,所以不存在泛型擦除(泛型对于类型的约束只在编译期存在)问题
 */
inline fun <reified T> create(): T = create(T::class.java)
```

## 2. 利用协程简化网络请求

首先是构建interface动态代理对象,

```kotlin
private val placeService = ServiceCreator.create(PlaceService::class.java)
```

因为平时我们使用Retrofit是下面这样的:
```kotlin
val appService = ServiceCreator.create<AppService>()
appService.getAppData().enqueue(object : Callback<List<App>> {
    override fun onResponse(call: Call<List<App>>, response: Response<List<App>>){
        //得到服务器返回的数据
    }
    
    override fun onFailure(call: Call<List<App>>,t: Throwable) {
        //在这里对异常情况进行处理
    }
})
```

这里其实我们可以用协程进行简化,我们扩展Call,给Call扩展一个await方法

```kotlin
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
```
这里扩展了Call,搞了一个await的挂起函数,因为我们扩展的是Call<T>,这样所有返回值是Call<T>类型的都可以调用await方法了.这里用suspendCoroutine来挂起当前协程,
将异步网络请求放入suspendCoroutine,这里会将Lambda表达式的内容(就是传入的参数continuation)在普通线程中执行,当前协程会被挂起,然后调用continuation.resume()才会恢复当前协程.
在suspendCoroutine里面进行了网络网络请求,等网络请求完了调用resume恢复当前协程执行,且有返回值.这里的返回值包含了成功和失败的时候.