package com.example.base_fun


import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.base_fun.injection.component.DaggerAppComponent
import com.example.base_fun.injection.module.AppMoudle
import com.example.base_lib.injection.core.AppDelegate

open class MvpApplication : Application() {
    lateinit var appComponent: DaggerAppComponent
    private var appDelegate: AppDelegate? = null

    companion object {
        private var application: Application? = null
        fun application() = application ?: throw Throwable("application null")
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        appDelegate?.onCreate(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        initInjection()
        if (appDelegate == null) {
            appDelegate = AppDelegate(base)
        }
        appDelegate?.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
        appDelegate?.onTerminate(this)
    }

    private fun initInjection() {
        appComponent = DaggerAppComponent.builder()
            .appMoudle(AppMoudle(this))
            .build() as DaggerAppComponent
    }
}