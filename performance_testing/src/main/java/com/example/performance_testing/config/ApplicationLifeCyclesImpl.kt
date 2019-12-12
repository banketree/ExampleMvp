package com.example.performance_testing.config

import android.app.Application
import android.content.Context
import com.example.base_lib.injection.core.AppLifeCycles
import leakcanary.AppWatcher

import timber.log.Timber
import com.example.performance_testing.AppBlockCanaryContext
import com.github.moduth.blockcanary.BlockCanary


internal class ApplicationLifeCyclesImpl : AppLifeCycles {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        Timber.i("${application.javaClass.simpleName} onCreate 初始化 + 泄漏检测 + 卡顿检测")
        AppWatcher.config = AppWatcher.config.copy()
        BlockCanary.install(application, AppBlockCanaryContext()).start()
    }

    override fun onTerminate(application: Application) {
    }
}