package com.example.performance_testing.config

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.example.base_fun.BuildConfig
import com.example.base_lib.injection.core.AppLifeCycles
import leakcanary.AppWatcher

import timber.log.Timber


internal class ApplicationLifeCyclesImpl : AppLifeCycles {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        AppWatcher.config = AppWatcher.config.copy()
    }

    override fun onTerminate(application: Application) {
    }
}