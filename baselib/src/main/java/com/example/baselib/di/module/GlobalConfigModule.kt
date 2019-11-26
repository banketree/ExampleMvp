package com.example.baselib.di.module

import android.app.Application
import android.text.TextUtils

import java.io.File
import java.util.ArrayList
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import javax.inject.Singleton
import com.example.baselib.http.BaseUrl
import com.example.baselib.http.GlobalHttpHandler
import com.example.baselib.http.imageloader.BaseImageLoaderStrategy
import com.example.baselib.integration.IRepositoryManager
import com.example.baselib.integration.cache.Cache
import com.example.baselib.integration.cache.CacheType
import com.example.baselib.integration.cache.IntelligentCache
import com.example.baselib.utils.Preconditions
import dagger.Module
import dagger.Provides
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.internal.Util

/**
 * ================================================
 * 框架独创的建造者模式 [Module],可向框架中注入外部配置的自定义参数
 * ================================================
 */
@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mApiUrl: HttpUrl?
    private val mBaseUrl: BaseUrl?
    private val mLoaderStrategy: BaseImageLoaderStrategy<*>?
    private val mHandler: GlobalHttpHandler?
    private val mInterceptors: List<Interceptor>?
    private val mErrorListener: ResponseErrorListener?
    private val mCacheFile: File?
    private val mRetrofitConfiguration: HttpClientModule.RetrofitConfiguration?
    private val mOkhttpConfiguration: HttpClientModule.OkhttpConfiguration?
    private val mRxCacheConfiguration: HttpClientModule.RxCacheConfiguration?
    private val mGsonConfiguration: AppModule.GsonConfiguration?
    //    private RequestInterceptor.Level mPrintHttpLogLevel;
    //    private FormatPrinter mFormatPrinter;
    private val mCacheFactory: Cache.Factory?
    private val mExecutorService: ExecutorService?
    private val mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate?

    init {
        this.mApiUrl = builder.apiUrl
        this.mBaseUrl = builder.baseUrl
        this.mLoaderStrategy = builder.loaderStrategy
        this.mHandler = builder.handler
        this.mInterceptors = builder.interceptors
        this.mErrorListener = builder.responseErrorListener
        this.mCacheFile = builder.cacheFile
        this.mRetrofitConfiguration = builder.retrofitConfiguration
        this.mOkhttpConfiguration = builder.okhttpConfiguration
        this.mRxCacheConfiguration = builder.rxCacheConfiguration
        this.mGsonConfiguration = builder.gsonConfiguration
        //        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        //        this.mFormatPrinter = builder.formatPrinter;
        this.mCacheFactory = builder.cacheFactory
        this.mExecutorService = builder.executorService
        this.mObtainServiceDelegate = builder.obtainServiceDelegate
    }

    @Singleton
    @Provides
    internal fun provideInterceptors(): List<Interceptor>? {
        return mInterceptors
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideBaseUrl(): HttpUrl? {
        if (mBaseUrl != null) {
            val httpUrl = mBaseUrl.url()
            if (httpUrl != null) {
                return httpUrl
            }
        }
        return mApiUrl ?: HttpUrl.parse("https://api.github.com/")
    }

    /**
     * 提供图片加载框架,默认使用 [@Glide]
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideImageLoaderStrategy(): BaseImageLoaderStrategy<*>? {
        return mLoaderStrategy
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideGlobalHttpHandler(): GlobalHttpHandler? {
        return mHandler
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    internal fun provideCacheFile(application: Application): File? {
        return null
        //        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }

    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideResponseErrorListener(): ResponseErrorListener {
        return mErrorListener ?: ResponseErrorListener.EMPTY
    }

    @Singleton
    @Provides
    internal fun provideRetrofitConfiguration(): HttpClientModule.RetrofitConfiguration? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    internal fun provideOkhttpConfiguration(): HttpClientModule.OkhttpConfiguration? {
        return mOkhttpConfiguration
    }

    @Singleton
    @Provides
    internal fun provideRxCacheConfiguration(): HttpClientModule.RxCacheConfiguration? {
        return mRxCacheConfiguration
    }

    @Singleton
    @Provides
    internal fun provideGsonConfiguration(): AppModule.GsonConfiguration? {
        return mGsonConfiguration
    }

    //    @Singleton
    //    @Provides
    //    RequestInterceptor.Level providePrintHttpLogLevel() {
    //        return mPrintHttpLogLevel == null ? RequestInterceptor.Level.ALL : mPrintHttpLogLevel;
    //    }
    //
    //    @Singleton
    //    @Provides
    //    FormatPrinter provideFormatPrinter() {
    //        return mFormatPrinter == null ? new DefaultFormatPrinter() : mFormatPrinter;
    //    }

    @Singleton
    @Provides
    internal fun provideCacheFactory(application: Application): Cache.Factory {
        return mCacheFactory ?: Cache.Factory { type ->
            //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
            //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
            when (type.cacheTypeId) {//Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                CacheType.EXTRAS_TYPE_ID,
                CacheType.ACTIVITY_CACHE_TYPE_ID,
                CacheType.FRAGMENT_CACHE_TYPE_ID -> {
                    IntelligentCache<Cache.Factory>(
                        type.calculateCacheSize(application)
                    )
                }
                else -> {   //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    LruCache<String, Any>(type.calculateCacheSize(application))
                }
            }
        }
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [Executor]
     */
    @Singleton
    @Provides
    internal fun provideExecutorService(): ExecutorService {
        return mExecutorService ?: ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue(), Util.threadFactory("Arms Executor", false)
        )
    }

    @Singleton
    @Provides
    internal fun provideObtainServiceDelegate(): IRepositoryManager.ObtainServiceDelegate? {
        return mObtainServiceDelegate
    }

    class Builder constructor() {
        var apiUrl: HttpUrl? = null
        var baseUrl: BaseUrl? = null
        var loaderStrategy: BaseImageLoaderStrategy<*>? = null
        var handler: GlobalHttpHandler? = null
        var interceptors: MutableList<Interceptor>? = null
        var responseErrorListener: ResponseErrorListener? = null
        var cacheFile: File? = null
        var retrofitConfiguration: HttpClientModule.RetrofitConfiguration? = null
        var okhttpConfiguration: HttpClientModule.OkhttpConfiguration? = null
        var rxCacheConfiguration: HttpClientModule.RxCacheConfiguration? = null
        var gsonConfiguration: AppModule.GsonConfiguration? = null
        //         RequestInterceptor.Level printHttpLogLevel;
        //         FormatPrinter formatPrinter;
        var cacheFactory: Cache.Factory? = null
        var executorService: ExecutorService? = null
        var obtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate? = null

        fun baseurl(baseUrl: String): Builder {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.apiUrl = HttpUrl.parse(baseUrl)
            return this
        }

        fun baseurl(baseUrl: BaseUrl): Builder {
            this.baseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl::class.java.canonicalName + "can not be null.")
            return this
        }

        fun imageLoaderStrategy(loaderStrategy: BaseImageLoaderStrategy<*>): Builder {//用来请求网络图片
            this.loaderStrategy = loaderStrategy
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {//用来处理http响应结果
            this.handler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {//动态添加任意个interceptor
            if (interceptors == null)
                interceptors = ArrayList()
            this.interceptors!!.add(interceptor)
            return this
        }

        fun responseErrorListener(listener: ResponseErrorListener): Builder {//处理所有RxJava的onError逻辑
            this.responseErrorListener = listener
            return this
        }

        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun retrofitConfiguration(retrofitConfiguration: HttpClientModule.RetrofitConfiguration): Builder {
            this.retrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okhttpConfiguration(okhttpConfiguration: HttpClientModule.OkhttpConfiguration): Builder {
            this.okhttpConfiguration = okhttpConfiguration
            return this
        }

        fun rxCacheConfiguration(rxCacheConfiguration: HttpClientModule.RxCacheConfiguration): Builder {
            this.rxCacheConfiguration = rxCacheConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: AppModule.GsonConfiguration): Builder {
            this.gsonConfiguration = gsonConfiguration
            return this
        }

        //        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) {//是否让框架打印 Http 的请求和响应信息
        //            this.printHttpLogLevel = Preconditions.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.");
        //            return this;
        //        }
        //
        //        public Builder formatPrinter(FormatPrinter formatPrinter) {
        //            this.formatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter.class.getCanonicalName() + "can not be null.");
        //            return this;
        //        }

        fun cacheFactory(cacheFactory: Cache.Factory): Builder {
            this.cacheFactory = cacheFactory
            return this
        }

        fun executorService(executorService: ExecutorService): Builder {
            this.executorService = executorService
            return this
        }

        fun obtainServiceDelegate(obtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate): Builder {
            this.obtainServiceDelegate = obtainServiceDelegate
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
