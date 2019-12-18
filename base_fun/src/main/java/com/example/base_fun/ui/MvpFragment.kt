package com.example.base_fun.ui


import android.app.Activity
import androidx.annotation.NonNull
import com.example.base_fun.MvpApplication
import com.example.base_fun.cache.Cache
import com.example.base_fun.cache.CacheType
import com.example.base_fun.injection.component.DaggerActivityComponent
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import com.example.base_lib.ui.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class MvpFragment<T : BasePresenter> : BaseFragment(), IView {

    @Inject
    lateinit var presenter: T

    lateinit var activityComponent: DaggerActivityComponent
    private var cache: Cache<String, Any>? = null

    @NonNull
    @Synchronized
    fun provideCache(): Cache<String, Any> {
        if (cache == null) {
            cache =
                (activityComponent!!.application() as MvpApplication).appComponent.cacheFactory().build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
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

    /** 注册依赖关系 */
    abstract fun injectComponent()

    private fun initActivityInjection() {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent((activity?.application as MvpApplication).appComponent)
            .activityMoudle(ActivityMoudle(activity as Activity))
            .build() as DaggerActivityComponent
    }
}