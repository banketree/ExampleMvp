package com.example.component_demo1.http

import com.example.base_fun.http.RetrofitFactory
import com.example.component_demo1.ui.mvvm.RespWeather
import retrofit2.Response
import java.util.HashMap

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

/**
 * author: banketree
 * created on: 2019/6/12
 * description:
 * 天气预报
 */
class WeatherApi : RetrofitFactory() {
    private val key = "4e34d358d9aa062b2c46afd627084f85"
    private val BASE_URL = "https://restapi.amap.com"

    override fun getUrl(): String = BASE_URL

    //通过ip 获取城市编码
    suspend fun getCityCodeByIpByString(): Response<String> {
        val hashMap = HashMap<String, String>()
        hashMap["output"] = "json"
        hashMap["key"] = key
        return stringService(WeatherServiceApi::class.java).getCityCodeByIp(hashMap)
    }

    suspend fun getCityCodeByIpByGson(): Response<RespWeather> {
        val hashMap = HashMap<String, String>()
        hashMap["output"] = "json"
        hashMap["key"] = key
        return gsonService(WeatherServiceApi::class.java).getCityCodeByIp2(hashMap)
    }

    //    设置专属
    interface WeatherServiceApi {
        /**
         * 通过ip 查城市信息
         */
        @Headers("domain-test3: https://www.baidu.com")  // 加上 Domain-Name header
        @GET("/v3/ip")
        suspend fun getCityCodeByIp(@QueryMap map: HashMap<String, String>): Response<String>

        @GET("/v3/ip")
        suspend fun getCityCodeByIp2(@QueryMap map: HashMap<String, String>): Response<RespWeather>
    }
}
