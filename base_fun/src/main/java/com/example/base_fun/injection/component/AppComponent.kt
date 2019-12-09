package com.example.base_fun.injection.component

import android.app.Application
import android.content.Context
import com.example.base_fun.cache.Cache
import com.example.base_fun.injection.module.AppMoudle
import com.example.base_fun.injection.module.GlobalConfigModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppMoudle::class, GlobalConfigModule::class])
interface AppComponent {

    fun application(): Application

    /**
     * 用来存取一些整个 App 公用的数据,
     */
    fun extras(): Cache<String, Any>

    /**
     * 用于创建框架所需缓存对象的工厂
     */
    fun cacheFactory(): Cache.Factory
}