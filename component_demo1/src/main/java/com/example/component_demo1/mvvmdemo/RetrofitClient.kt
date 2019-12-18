package com.example.component_demo1.mvvmdemo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    val BASE_URL =  "http://www.baidu.com/"

    val reqApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        return@lazy retrofit.create(RequestService::class.java)
    }

}