package com.example.base_fun.http

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
* 返回数据类
* */
open class RespBase<T> : Serializable {
    /***
     * 不是有接口获取，是请求的接口的名称
     */
    @field:SerializedName("serviceName")
    var serviceName: String? = null

    //错误码
    var code: Int? = null

    //信息	包含错误信息、提示信息
    var msg: String? = null

    //返回的数据
    var data: T? = null

    open fun isSuccess(): Boolean = 0 == code
}