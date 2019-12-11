package com.example.component_demo1.http

import android.os.Looper
import android.text.TextUtils
import com.example.base_fun.http.LibHttpCallback
import com.example.base_fun.http.LibHttpService
import okhttp3.Interceptor
import java.io.IOException
import java.util.HashMap

/**
 * Created by banketree
 * on 2018/12/18.
 */
abstract class ApiService : LibHttpService() {

    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    abstract override fun getUrl(): String

    abstract override fun getService(): Class<out Any>

    @Throws(IOException::class)
    override fun okhttpInterceptor(chain: Interceptor.Chain): okhttp3.Response {
        val builder = chain.request()
            .newBuilder()
            .removeHeader("Content-Type")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

        val request = builder.build()
        return chain.proceed(request)
    }
}
