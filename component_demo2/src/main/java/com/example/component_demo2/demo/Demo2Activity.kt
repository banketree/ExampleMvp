package com.example.component_demo2.demo

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo2.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo2_activity_main.*


@Route(path = AppRoute.TWO_DEMO2_MAIN)
class Demo2Activity : MvpActivity<Demo2Presenter>() {

    override fun getLayoutAny() = R.layout.demo2_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo3Main()
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo2Component.builder().activityComponent(activityComponent).build()
        demoComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}
