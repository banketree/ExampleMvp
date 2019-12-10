package com.example.component_demo1.http

import android.os.Looper
import android.text.TextUtils
import com.example.base_fun.http.LibHttpCallback
import com.example.base_fun.http.LibHttpService
import okhttp3.Interceptor
import java.io.IOException
import java.util.HashMap

/**
 * Created by banketree
 * on 2018/12/18.
 */
abstract class ApiService : LibHttpService() {

    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    abstract override fun getUrl(): String

    abstract override fun getService(): Class<out Any>

    fun doNetByGson(svceName: String, hashMap: HashMap<String, String>) {
        doNet(LibHttpCallback.TypeGson, svceName, hashMap)
    }

    fun doNetByGson(svceName: String, hashMap: HashMap<String, String>, callback: HttpCallback<*>) {
        doNet(LibHttpCallback.TypeGson, svceName, hashMap, callback)
    }

/*    fun doNetByXml(svceName: String, hashMap: HashMap<String, String>) {
        doNet(LibHttpCallback.TypeXml, svceName, hashMap)
    }

    fun doNetByXml(svceName: String, hashMap: HashMap<String, String>, callback: HttpCallback<*>) {
        doNet(LibHttpCallback.TypeXml, svceName, hashMap, callback)
    }*/

    fun doNet(svceName: String, hashMap: HashMap<String, String>) {
        doNet(LibHttpCallback.TypeString, svceName, hashMap)
    }

    fun doNet(svceName: String, hashMap: HashMap<String, String>, callback: HttpCallback<*>) {
        doNet(LibHttpCallback.TypeString, svceName, hashMap, callback)
    }

    @JvmOverloads
    fun doNet(
        doType: Int,
        svceName: String,
        hashMap: HashMap<String, String>,
        callback: HttpCallback<*> = object : HttpCallback<Any>() {
        }
    ) {
        if (TextUtils.isEmpty(callback.serviceName))
            callback.setServiceName(svceName) //接口名--》路径反应
        //        if (callback.canShowProgress() && isMainThread())//需要主线程
        //            callback.showProgressDialog();
        try {
            if (doType == LibHttpCallback.TypeString) {
                if (callback.isAsyn)
                    asynNetString(hashMap, callback)
                else
                    synNetString(hashMap, callback)
            } else if (doType == LibHttpCallback.TypeGson) {
                if (callback.isAsyn)
                    asynNetGson(hashMap, callback)
                else
                    synNetGson(hashMap, callback)
            }
//            else if (doType == LibHttpCallback.TypeXml) {
//                if (callback.isAsyn)
//                    asynNetXml(hashMap, callback)
//                else
//                    synNetXml(hashMap, callback)
//            }
        } catch (e: Exception) {
            callback.onFaile(e)
        }
        //        catch (UceError e) {
        //            callback.onFaile(e);
        //        }
    }

    @Throws(IOException::class)
    override fun okhttpInterceptor(chain: Interceptor.Chain): okhttp3.Response {
        val builder = chain.request()
            .newBuilder()
            .removeHeader("Content-Type")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

        val request = builder.build()
        return chain.proceed(request)
    }
}
