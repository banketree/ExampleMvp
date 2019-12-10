package com.example.component_demo1.http


import java.io.IOException
import java.util.HashMap

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * author: banketree
 * created on: 2019/6/12
 * description:
 * 天气预报
 */
class WeatherApi : ApiService() {
    private val key = "4e34d358d9aa062b2c46afd627084f85"
    private val BASE_URL = "https://restapi.amap.com"

    override fun getUrl() = BASE_URL

    override fun getService(): Class<out Any> = WeatherServiceApi::class.java

    //添加头信息()
    @Throws(IOException::class)
    override fun okhttpInterceptor(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
            .removeHeader("Content-Type")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
            .addHeader("charset", "UTF-8") //登录前 必传

        val request = builder.build()
        return chain.proceed(request)
    }

    //通过ip 获取城市编码
    private fun getCityCodeByIp(callback: HttpCallback<*>) {
        val hashMap = HashMap<String, String>()
        hashMap["output"] = "json"
        hashMap["key"] = key
        doNet("getCityCodeByIp", hashMap, callback)
    }

    //获取天气预报
//步骤1：通过ip 获取城市信息
//步骤2：通过城市编码 获取天气信息
    fun getWeather(callback: HttpCallback<*>) {
        getCityCodeByIp(callback)
    }

    //    设置专属
    interface WeatherServiceApi {
        /**
         * 通过ip 查城市信息
         */
        @GET("/v3/ip")
        fun getCityCodeByIp(@QueryMap map: HashMap<String, String>): Call<String>
    }
}
