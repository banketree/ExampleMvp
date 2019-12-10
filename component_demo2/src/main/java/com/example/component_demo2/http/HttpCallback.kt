package com.example.component_demo2.http

import android.content.Context
import com.example.base_fun.http.LibHttpCallback
import org.json.JSONException
import retrofit2.Call
import retrofit2.Response

abstract class HttpCallback<T> : LibHttpCallback {
    var context: Context? = null
        private set

    constructor()

    constructor(serviceName: String) : super(serviceName)

    constructor(context: Context, showProgress: Boolean) {
        this.context = context
    }

    constructor(context: Context, showProgress: Boolean, serviceName: String) : super(serviceName) {
        this.context = context
    }

    final override fun onResponse(call: Call<Any>, response: Response<Any>) {
        if (call.isCanceled) {
            onCancel()
            return
        }

        if (response?.body() == null || !response.isSuccessful) {
            onFaile(Exception("BNET404"))//网络请求异常
            return
        }

        var t: T? = null
        try {
            t = getBean(response)
        } catch (e: Exception) {
            t = null
        }


        if (t == null) {
            onFaile(Exception("net_inner_method")) //内部方法出错
            return
        }
        //            t.setServiceName(getServiceName());
        //            if (t.isSuccess()) {
        //                onSucess(t);
        //                return;
        //            }

        onFaile(Exception("")) //服务器返回的错误
    }

    final override fun onFailure(call: Call<Any>, t: Throwable) {
        if (call.isCanceled) {
            onCancel()
            return
        }

        onFaile(Exception(t))
    }

    open fun onSucess(t: T) {
        dismiss()
    }

    open fun onFaile(ex: Exception) {
        dismiss()
    }

    open fun onCancel() {
        dismiss()
    }

    @Throws(JSONException::class)
    protected open fun getBean(response: Response<*>): T? {
        //        if (getType() == Companion.getTypeGson()) {
        //            return ((Resp<T>) response.body()).getResponse();
        //        } else if (getType() == Companion.getTypeXml()) {
        //            return (T) response.body();
        //        } else if (getType() == Companion.getTypeString()) {//需要重写方法
        //        }

        return null
    }

    fun setServiceName(serviceName: String): HttpCallback<T> {
        super.setServiceNameString(serviceName)
        return this
    }

    fun asyn(asyn: Boolean): HttpCallback<T> {
        super.isAsyn = asyn
        return this
    }

    fun dismiss() {}
}