package com.example.component_demo1.ui.mvvm

import android.os.Parcel
import android.os.Parcelable
import com.example.base_fun.http.RespBase

class RespWeather() : RespBase() {
    var status: String? = ""
    var info: String? = ""
    var infocode: String? = ""
    var province: String? = ""
    var city: String? = ""
}