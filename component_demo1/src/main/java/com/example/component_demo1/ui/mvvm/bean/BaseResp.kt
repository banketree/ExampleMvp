package com.example.component_demo1.ui.mvvm.bean


data class BaseResp<T>(
    var code: Int = 0,
    var msg: String = "",
    var `data`: T
)