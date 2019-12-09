package com.example.base_fun.http

import retrofit2.Call
import retrofit2.Response

abstract class LibHttpCallback : retrofit2.Callback<Any> {
    protected var type = TypeString
    var isAsyn = true
    private var isLoadingDialogCancel = false
    var serviceName: String? = null
        private set
    var call: Call<*>? = null
        protected set

    constructor() {
        isAsyn = true
        isLoadingDialogCancel = false
    }

    constructor(serviceName: String) {
        isAsyn = true
        isLoadingDialogCancel = false
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
        val TypeXml = 2
    }
}
