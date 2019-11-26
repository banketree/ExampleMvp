package com.example.baselib.di.module


import android.app.Application
import android.content.Context

import java.util.ArrayList

import javax.inject.Named
import javax.inject.Singleton
import androidx.fragment.app.FragmentManager
import com.example.baselib.integration.*
import com.example.baselib.integration.cache.Cache
import com.example.baselib.integration.cache.CacheType
import com.example.baselib.integration.lifecycle.ActivityLifecycleForRxLifecycle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager

    @Binds
    @Named("ActivityLifecycle")
    internal abstract fun bindActivityLifecycle(activityLifecycle: ActivityLifecycle): Application.ActivityLifecycleCallbacks

    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    internal abstract fun bindActivityLifecycleForRxLifecycle(activityLifecycleForRxLifecycle: ActivityLifecycleForRxLifecycle): Application.ActivityLifecycleCallbacks

    @Binds
    internal abstract fun bindFragmentLifecycle(fragmentLifecycle: FragmentLifecycle): FragmentManager.FragmentLifecycleCallbacks

    interface GsonConfiguration {
        fun configGson(context: Context, builder: GsonBuilder)
    }

    companion object {

        @Singleton
        @Provides
        internal fun provideGson(application: Application, configuration: GsonConfiguration?): Gson {
            val builder = GsonBuilder()
            configuration?.configGson(application, builder)
            return builder.create()
        }

        /**
         * 之前 [AppManager] 使用 Dagger 保证单例, 只能使用 [@AppComponent#appManager()][@AppComponent.appManager] 访问
         * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 [AppManager.getAppManager] 访问, 更加方便
         * 但为了不影响之前使用 [@AppComponent#appManager()][@AppComponent.appManager] 获取 [AppManager] 的项目, 所以暂时保留这种访问方式
         *
         * @param application
         * @return
         */
        @Singleton
        @Provides
        internal fun provideAppManager(application: Application): AppManager {
            return AppManager.getAppManager().init(application)
        }

        @Singleton
        @Provides
        internal fun provideExtras(cacheFactory: Cache.Factory): Cache<String, Any> {
            return cacheFactory.build(CacheType.EXTRAS) as Cache<String, Any>
        }

        @Singleton
        @Provides
        internal fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
            return ArrayList()
        }
    }
}
