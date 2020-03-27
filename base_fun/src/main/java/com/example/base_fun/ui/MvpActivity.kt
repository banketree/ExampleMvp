package com.example.base_fun.ui

import androidx.annotation.NonNull
import com.example.base_fun.MvpApplication
import com.example.base_fun.cache.Cache
import com.example.base_fun.cache.CacheType
import com.example.base_fun.injection.component.DaggerActivityComponent
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.mvp.BasePresenter
import com.example.base_lib.ui.LibActivity
import javax.inject.Inject


abstract class MvpActivity<T : BasePresenter> : LibActivity() {

    @Inject
    lateinit var presenter: T

    protected var activityComponent: DaggerActivityComponent? = null
    private var cache: Cache<String, Any>? = null

    @NonNull
    @Synchronized
    fun provideCache(): Cache<String, Any> {
        if (cache == null) {
            cache =
                (activityComponent!!.application() as MvpApplication).appComponent.cacheFactory().build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return cache as Cache<String, Any>
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.release()
    }

    override fun initPlug() {
        initActivityInjection()
        injectComponent()
    }

    override fun initView() {
        presenter?.init()
    }

    private fun initActivityInjection() {
        activityComponent = DaggerActivityComponent.builder()
            .activityMoudle(ActivityMoudle(this))
            .appComponent((application as MvpApplication).appComponent)
            .build() as DaggerActivityComponent
    }

    /**
     * 注册依赖对象
     */
    abstract fun injectComponent()
}