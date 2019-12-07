package com.example.mvp

import android.app.Application
import com.example.route.AppRoute

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRoute.initRoute(this)
    }
}