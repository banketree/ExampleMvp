package com.example.component_demo1.ui.mvvm


import com.example.base_fun.http.RespBase
import com.example.base_fun.http.RetrofitFactory
import java.io.IOException
import java.util.HashMap

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import timber.log.Timber

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
    suspend fun getCityCodeByIpByString(): RespBase {
        val hashMap = HashMap<String, String>()
        hashMap["output"] = "json"
        hashMap["key"] = key
        val data = stringService(WeatherServiceApi::class.java).getCityCodeByIp(hashMap)
        val result = RespBase()
        result.isSuccess = true
        result.message = data
        return result
//        asynNet((stringService() as WeatherServiceApi).getCityCodeByIp(hashMap), "getCityCodeByIp", callback)
    }

    suspend fun getCityCodeByIpByGson(): RespWeather {
        val hashMap = HashMap<String, String>()
        hashMap["output"] = "json"
        hashMap["key"] = key
        val data = gsonService(WeatherServiceApi::class.java).getCityCodeByIp2(hashMap)
        data.isSuccess = true
        Timber.i("$data")
        return data
//        asynNet((stringService() as WeatherServiceApi).getCityCodeByIp(hashMap), "getCityCodeByIp", callback)
    }

    //    设置专属
    interface WeatherServiceApi {
        /**
         * 通过ip 查城市信息
         */
        @GET("/v3/ip")
        suspend fun getCityCodeByIp(@QueryMap map: HashMap<String, String>): String

        @GET("/v3/ip")
        suspend fun getCityCodeByIp2(@QueryMap map: HashMap<String, String>): RespWeather
    }
}
