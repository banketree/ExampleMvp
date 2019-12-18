package com.example.component_demo1.mvvmdemo.repository

import com.example.component_demo1.mvvmdemo.RetrofitClient
import com.example.component_demo1.mvvmdemo.base.BaseRepository
import com.example.component_demo1.mvvmdemo.base.ResponseData
import com.example.component_demo1.mvvmdemo.databean.Data

class ArticleRepository : BaseRepository() {

    suspend fun getDatas(): ResponseData<List<Data>> = request {
        RetrofitClient.reqApi.getDatas()
    }
}