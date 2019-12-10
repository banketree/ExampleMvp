package com.example.base_fun.http

import retrofit2.Call
import retrofit2.Response

abstract class LibHttpCallback : retrofit2.Callback<Any> {
    //请求类型
    var type = TypeString
    //同步还是异步
    var isAsyn = true
    //服务名称  具体业务
    var serviceName: String? = null
        private set
    //记录每个请求的call
    var call: Call<*>? = null

    constructor() {
        isAsyn = true
    }

    constructor(serviceName: String) {
        isAsyn = true
        this.serviceName = serviceName
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>) {}

    override fun onFailure(call: Call<Any>, t: Throwable) {}

    protected fun setServiceNameString(serviceName: String) {
        this.serviceName = serviceName
    }

    companion object {
        val TypeString = 0
        val TypeGson = 1
    }
}
