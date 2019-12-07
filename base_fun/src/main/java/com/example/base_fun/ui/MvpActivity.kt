package com.example.base_fun.ui


import com.example.base_fun.MvpApplication
import com.example.base_fun.injection.component.DaggerActivityComponent
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import com.example.base_lib.ui.BaseActivity
import javax.inject.Inject

abstract class MvpActivity<T : IPresenter> : BaseActivity(), IView {

    @Inject
    lateinit var presenter: T

    protected var activityComponent: DaggerActivityComponent? = null

    override fun initPlug() {
        initActivityInjection()
        injectComponent()
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