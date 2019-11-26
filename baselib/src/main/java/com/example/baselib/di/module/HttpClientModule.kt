package com.example.baselib.di.module


import android.app.Application
import android.content.Context

import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

import javax.inject.Named
import javax.inject.Singleton
import com.example.baselib.http.GlobalHttpHandler
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
object HttpClientModule {
    private val TIME_OUT = 10

    /**
     * 提供 [Retrofit]
     *
     * @param application   [Application]
     * @param configuration [RetrofitConfiguration]
     * @param builder       [Retrofit.Builder]
     * @param client        [OkHttpClient]
     * @param httpUrl       [HttpUrl]
     * @param gson          [Gson]
     * @return [Retrofit]
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(
        application: Application,
        configuration: RetrofitConfiguration?,
        builder: Retrofit.Builder,
        client: OkHttpClient,
        httpUrl: HttpUrl,
        gson: Gson
    ): Retrofit {
        builder
            .baseUrl(httpUrl)//域名
            .client(client)//设置 OkHttp

        configuration?.configRetrofit(application, builder)

        builder
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 RxJava
            .addConverterFactory(GsonConverterFactory.create(gson))//使用 Gson
        return builder.build()
    }

    /**
     * 提供 [OkHttpClient]
     *
     * @param application     [Application]
     * @param configuration   [OkhttpConfiguration]
     * @param builder         [OkHttpClient.Builder]
     * @param intercept       [Interceptor]
     * @param interceptors    [<]
     * @param handler         [GlobalHttpHandler]
     * @param executorService [ExecutorService]
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    internal fun provideClient(
        application: Application,
        configuration: OkhttpConfiguration?,
        builder: OkHttpClient.Builder,
        intercept: Interceptor,
        interceptors: List<Interceptor>?,
        handler: GlobalHttpHandler?,
        executorService: ExecutorService
    ): OkHttpClient {
        builder
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(intercept)

        if (handler != null)
            builder.addInterceptor { chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())) }

        //如果外部提供了 Interceptor 的集合则遍历添加
        if (interceptors != null) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        //为 OkHttp 设置默认的线程池
        builder.dispatcher(Dispatcher(executorService))

        configuration?.configOkhttp(application, builder)
        return builder.build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    internal fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    //    @Binds
    //    abstract Interceptor bindInterceptor(RequestInterceptor interceptor);

    /**
     * 提供 [RxCache]
     *
     * @param application    [Application]
     * @param configuration  [RxCacheConfiguration]
     * @param cacheDirectory RxCache 缓存路径
     * @param gson           [Gson]
     * @return [RxCache]
     */
    @Singleton
    @Provides
    internal fun provideRxCache(
        application: Application,
        configuration: RxCacheConfiguration?, @Named("RxCacheDirectory") cacheDirectory: File,
        gson: Gson
    ): RxCache {
        val builder = RxCache.Builder()
        var rxCache: RxCache? = null
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder)
        }
        return rxCache ?: builder
            .persistence(cacheDirectory, GsonSpeaker(gson))
    }

    /**
     * 需要单独给 [RxCache] 提供子缓存文件
     *
     * @param cacheDir 框架缓存文件
     * @return [File]
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    internal fun provideRxCacheDirectory(cacheDir: File): File? {
        //        File cacheDirectory = new File(cacheDir, "RxCache");
        //        return DataHelper.makeDirs(cacheDirectory);
        return null
    }

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application [Application]
     * @param listener    [ResponseErrorListener]
     * @return [RxErrorHandler]
     */
    @Singleton
    @Provides
    internal fun proRxErrorHandler(application: Application, listener: ResponseErrorListener): RxErrorHandler {
        return RxErrorHandler
            .builder()
            .with(application)
            .responseErrorListener(listener)
            .build()
    }

    /**
     * [Retrofit] 自定义配置接口
     */
    interface RetrofitConfiguration {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    /**
     * [OkHttpClient] 自定义配置接口
     */
    interface OkhttpConfiguration {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

    /**
     * [RxCache] 自定义配置接口
     */
    interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson
         * 请 `return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());`, 否则请 `return null;`
         *
         * @param context [Context]
         * @param builder [RxCache.Builder]
         * @return [RxCache]
         */
        fun configRxCache(context: Context, builder: RxCache.Builder): RxCache
    }
}