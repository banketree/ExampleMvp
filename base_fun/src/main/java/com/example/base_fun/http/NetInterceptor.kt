package com.example.base_fun.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * A {@see RequestInterceptor} that adds an cache to requests
 */
class NetInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().removeHeader("Content-Type")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").build()
        return chain.proceed(request)
    }
}
