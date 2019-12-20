package com.example.component_demo1.ui.mvvm

import com.example.base_fun.http.RespBase

class RespWeather() : RespBase() {
    var status: String? = ""
    var info: String? = ""
    var infocode: String? = ""
    var province: String? = ""
    var city: String? = ""

    override fun toString(): String {
        return """
            status：$status
            info：$info
            infocode：$infocode
            province：$province
            city：$city
                """
    }
}