package com.example.base_fun.http

import java.io.Serializable

/*
* 返回数据
* */
open class RespBase : Serializable {
    /***
     * 不是有接口获取，是请求的接口的名称
     */
    var serviceName: String? = null

    var errorCode: String? = null

    var isSuccess: Boolean = false

    var message: String? = null //错误信息	否	如果根据上面得code前端字典中有message信息，则输出前端得message,否则输出此message信息
}


fun RespBase.convertHttpRes(): HttpResponse {
    return if (this.isSuccess) {
        Success(this)
    } else {
        Failure(HttpError.USER_EXIST)
    }
}

//fun <T : Any> RespBase<T>.convertHttpRes(): HttpResponse {
//    return if (this.isSuccess) {
//        data?.let {
//            Success(it)
//        } ?: Success(Any())
//    } else {
//        Failure(HttpError.USER_EXIST)
//    }
//}