package com.example.component_demo1.mvvmdemo.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


open class BaseRepository {

    suspend fun <T : Any> request(call: suspend () -> ResponseData<T>): ResponseData<T> {
        return withContext(Dispatchers.IO){ call.invoke()}
    }

}