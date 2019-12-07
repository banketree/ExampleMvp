package com.example.component_demo1.demo

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo1.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo1_activity_main.*


@Route(path = AppRoute.TWO_DEMO1_MAIN)
class Demo1Activity : MvpActivity<Demo1Presenter>() {

    override fun getLayoutAny() = R.layout.demo1_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo1Main()
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo1Component.builder().activityComponent(activityComponent).build()
        demoComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}
