package com.example.base_fun.http

import androidx.lifecycle.MutableLiveData
import org.json.JSONException

abstract class HttpCallback<T> {
    //同步还是异步
    var isAsyn = true
    //服务名称  具体业务
    var serviceName: String = ""

//    var state: MutableLiveData<Int> = MutableLiveData()
    open fun onSucess(t: T) {
    }

    open fun onFaile(ex: Exception) {
    }

    open fun onComplete() {
    }

    @Throws(JSONException::class)
    open fun getBean(response: Any): T? {
        return null
    }
}