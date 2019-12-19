package com.example.component_demo1.ui.mvvm

import com.example.component_demo1.ui.mvvm.bean.BaseResp
import com.example.component_demo1.ui.mvvm.bean.BaseResp2


/*数据解析扩展函数*/
fun <T> BaseResp<T>.dataConvert(): T {

    if (code == 200) {
        return data
    } else {
        throw Exception(msg)
    }
}