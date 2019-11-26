package com.example.baselib.integration


import android.content.Context
import retrofit2.Retrofit

/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 [@IModel] 必要的 Api 做数据处理
 * ================================================
 */
interface IRepositoryManager {

    /**
     * 获取 [Context]
     *
     * @return [Context]
     */
    val context: Context

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service Retrofit service class
     * @param <T>     Retrofit service 类型
     * @return Retrofit service
    </T> */
    fun <T> obtainRetrofitService(service: Class<T>): T


    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache RxCache service class
     * @param <T>   RxCache service 类型
     * @return RxCache service
    </T> */
    fun <T> obtainCacheService(cache: Class<T>): T

    /**
     * 清理所有缓存
     */
    fun clearAllCache()

    interface ObtainServiceDelegate {

        fun <T> createRetrofitService(retrofit: Retrofit, serviceClass: Class<T>): T?
    }
}
