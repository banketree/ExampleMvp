package com.example.base_fun.injection.module

import android.app.Application
import androidx.annotation.NonNull
import com.example.base_fun.cache.Cache
import com.example.base_fun.cache.CacheType
import com.example.base_fun.cache.IntelligentCache
import com.example.base_fun.cache.LruCache
import dagger.Module
import dagger.Provides
import java.util.concurrent.*
import javax.inject.Singleton

@Module
class GlobalConfigModule {

    private val cacheFactory: Cache.Factory? = null
    private val executorService: ExecutorService? = null

    /*
    * 缓存
    * */
    @Singleton
    @Provides
    fun provideCacheFactory(application: Application): Cache.Factory {
        return cacheFactory ?: object : Cache.Factory {
            @NonNull
            override fun build(type: CacheType): Cache<String, Any> {
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
                return when (type.cacheTypeId) {
                    //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                    CacheType.EXTRAS_TYPE_ID,
                    CacheType.ACTIVITY_CACHE_TYPE_ID,
                    CacheType.FRAGMENT_CACHE_TYPE_ID -> IntelligentCache(
                        type.calculateCacheSize(application)
                    )
                    //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    else -> LruCache(type.calculateCacheSize(application))
                }
            }
        }
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     */
    @Singleton
    @Provides
    fun provideExecutorService(): ExecutorService {
        return executorService ?: ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue<Runnable>(), threadFactory("Arms Executor", false)
        )
    }

    private fun threadFactory(name: String, daemon: Boolean): ThreadFactory {
        return ThreadFactory { runnable ->
            val result = Thread(runnable, name)
            result.isDaemon = daemon
            result
        }
    }
}