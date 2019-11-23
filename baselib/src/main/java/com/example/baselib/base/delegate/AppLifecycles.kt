package com.example.baselib.base.delegate


import android.app.Application
import android.content.Context

/**
 * ================================================
 * 用于代理应用的生命周期
 * ================================================
 */
interface AppLifecycles {
    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)
}
