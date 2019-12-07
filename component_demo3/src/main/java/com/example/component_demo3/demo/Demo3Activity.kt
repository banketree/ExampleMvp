package com.example.component_demo3.demo

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo3.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo3_activity_main.*


@Route(path = AppRoute.TWO_DEMO3_MAIN)
class Demo3Activity : MvpActivity<Demo3Presenter>() {

    override fun getLayoutAny() = R.layout.demo3_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
//            presenter?.showLoading()
            AppRoute.gotoTwoDemo1Main()
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo3Component.builder().activityComponent(activityComponent).build()
        demoComponent?.inject(this)
    }

    override fun showLoading() {
        Log.i("", "")
    }

    override fun hideLoading() {
        Log.i("", "")
    }
}
