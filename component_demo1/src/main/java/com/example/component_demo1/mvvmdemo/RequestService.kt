package com.example.component_demo1.mvvmdemo

import com.example.component_demo1.mvvmdemo.base.ResponseData
import com.example.component_demo1.mvvmdemo.databean.Data
import retrofit2.http.GET

interface RequestService {
    @GET("/")
    suspend fun getDatas(): ResponseData<List<Data>>
}