package com.example.baselib.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.baselib.BuildConfig
import com.example.baselib.injection.component.DaggerAppComponent
import com.example.baselib.injection.module.AppMoudle

open class MvpApplication : Application() {
    lateinit var appComponent: DaggerAppComponent

    override fun onCreate() {
        super.onCreate()
        context = this
        initInjection()
//        if (BuildConfig.DEBUG) {
//            // 打印日志
//            ARouter.openLog()
//            //开启调试模式
//            ARouter.openDebug()
//        }
//        ARouter.init(this)
    }

    private fun initInjection() {
        appComponent = DaggerAppComponent.builder()
            .appMoudle(AppMoudle(this))
            .build() as DaggerAppComponent
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}