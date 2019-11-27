package com.example.baselib.ui.activity

import com.example.baselib.common.MvpApplication
import com.example.baselib.injection.component.DaggerActivityComponent
import com.example.baselib.injection.module.ActivityMoudle
import com.example.baselib.mvp.presenter.IPresenter
import com.example.baselib.mvp.view.IView
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