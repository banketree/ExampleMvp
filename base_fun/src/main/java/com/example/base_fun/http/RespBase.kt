package com.example.base_fun.http

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
* 返回数据
* */
open class RespBase : Serializable {
    /***
     * 不是有接口获取，是请求的接口的名称
     */
    @field:SerializedName("serviceName")
    var serviceName: String? = null

    var errorCode: String? = null

    var isSuccess: Boolean = false

    var message: String? = null //错误信息	否	如果根据上面得code前端字典中有message信息，则输出前端得message,否则输出此message信息
}