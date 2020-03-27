package com.example.base_fun.http

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/*
* Retrofit 工厂
* */
abstract class RetrofitFactory {
    companion object {
        const val TIME_OUT_SECOND = 30L  //超时时间 30秒
    }

    private var okHttpClient: OkHttpClient? = null //请求端
    private var gsonRetrofit: Retrofit? = null
    private var stringRetrofit: Retrofit? = null

    abstract fun getUrl(): String //请求域名

    fun resetHttpClient() {
        okHttpClient = null
        gsonRetrofit = null
        stringRetrofit = null
    }

    //gson
    protected fun <T> gsonService(service: Class<T>): T {
        getOkHttpClient()//确保已经实例化
        if (gsonRetrofit == null) {
            synchronized(RetrofitFactory::class.java) {
                if (gsonRetrofit == null) {
                    gsonRetrofit = Retrofit.Builder()
                        .baseUrl(getUrl())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getOkHttpClient())
                        .build()
                }
            }
        }

        return gsonRetrofit!!.create(service)
    }

    //String
    protected fun <T> stringService(service: Class<T>): T {
        getOkHttpClient()//确保已经实例化
        if (stringRetrofit == null) {
            synchronized(RetrofitFactory::class.java) {
                stringRetrofit = Retrofit.Builder()
                    .baseUrl(getUrl())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(getOkHttpClient())
                    .build()
            }
        }
        return stringRetrofit!!.create(service)
    }

    ///////////////////////////////////////okhttp3//////////////////////////////////////////////////////
    protected open fun handleOkHttpClient(builder: OkHttpClient.Builder) {
        builder.apply {
            connectTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
            readTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
//            addInterceptor(NetInterceptor())
            // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
            RetrofitUrlManager.getInstance().with(this)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            synchronized(RetrofitFactory::class.java) {
                if (okHttpClient == null) {
                    okHttpClient = LocalOkHttpClient.getOkHttpClient {
                        handleOkHttpClient(this)
                    }
                }
            }
        }
        return okHttpClient!!
    }
}