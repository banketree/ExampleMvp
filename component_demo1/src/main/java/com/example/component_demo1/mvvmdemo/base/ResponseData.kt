package com.example.component_demo1.mvvmdemo.base


data class ResponseData<out T>(val errorCode: Int, val errorMsg: String, val data: T)