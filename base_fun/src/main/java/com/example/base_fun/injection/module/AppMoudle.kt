package com.example.base_fun.injection.module

import android.app.Application
import com.example.base_fun.cache.Cache
import com.example.base_fun.cache.CacheType
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppMoudle(private val application: Application) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return this.application
    }

    @Singleton
    @Provides
    fun provideExtras(cacheFactory: Cache.Factory): Cache<String, Any> {
        return cacheFactory.build(CacheType.EXTRAS) as Cache<String, Any>
    }
}