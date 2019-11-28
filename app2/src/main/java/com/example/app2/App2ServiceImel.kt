package com.example.app2

import android.util.Log
import com.mvp.provider.router.service.App2Service
import javax.inject.Inject

class App2ServiceImel @Inject constructor() : App2Service {
    override fun getApp2(): Any {
        Log.i("", "getApp2")
        Log.i("", "getApp2")
        Log.i("", "getApp2")
        Log.i("", "getApp2")
        return 222
    }
}