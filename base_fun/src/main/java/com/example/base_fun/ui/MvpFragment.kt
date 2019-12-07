package com.example.base_fun.ui


import android.app.Activity
import com.example.base_fun.MvpApplication
import com.example.base_fun.injection.component.DaggerActivityComponent
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.mvp.IPresenter
import com.example.base_fun.mvp.IView
import com.example.base_lib.ui.BaseFragment
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