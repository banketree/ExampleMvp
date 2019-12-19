package com.example.component_demo1.ui.mvvm

import com.example.component_demo1.ui.mvvm.bean.BaseResp
import com.example.component_demo1.ui.mvvm.bean.BaseResp2
import com.example.component_demo1.ui.mvvm.bean.Fiction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.HashMap

interface ApiService {

    @GET("https://www.apiopen.top/novelApi")
    suspend fun getFictions(): BaseResp<List<Fiction>>

    /**
     * 通过ip 查城市信息
     */
    @GET("/v3/ip")
    suspend fun getCityCodeByIp(@QueryMap map: HashMap<String, String>): BaseResp2

    /**
     * 通过ip 查城市信息
     */
    @GET("https://www.baidu.com")
    suspend fun getHtml(@QueryMap map: HashMap<String, String>): String
}