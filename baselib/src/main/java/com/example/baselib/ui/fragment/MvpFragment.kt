package com.example.baselib.ui.fragment

import android.app.Activity
import com.example.baselib.common.MvpApplication
import com.example.baselib.injection.component.DaggerActivityComponent
import com.example.baselib.injection.module.ActivityMoudle
import com.example.baselib.mvp.presenter.IPresenter
import com.example.baselib.mvp.view.IView
import javax.inject.Inject

abstract class MvpFragment<T : IPresenter> : BaseFragment(), IView {

    @Inject
    lateinit var presenter: T

    lateinit var activityComponent: DaggerActivityComponent

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