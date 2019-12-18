package com.example.base_fun.ui


import android.app.Activity
import androidx.annotation.NonNull
import com.example.base_fun.MvpApplication
import com.example.base_fun.cache.Cache
import com.example.base_fun.cache.CacheType
import com.example.base_fun.injection.component.DaggerActivityComponent
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import com.example.base_lib.ui.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class MvpFragment<T : IPresenter> : BaseFragment(), IView {

    @Inject
    lateinit var presenter: T

    lateinit var activityComponent: DaggerActivityComponent
    private var cache: Cache<String, Any>? = null
    private var compositeDisposable: CompositeDisposable? = null

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
        rleaseDisposable()
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

    //添加订阅
    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)//将所有 Disposable 放入容器集中处理
    }

    //释放订阅
    fun rleaseDisposable() {
        compositeDisposable?.clear()//保证 Activity 结束时取消所有正在执行的订阅
        compositeDisposable = null
    }
}