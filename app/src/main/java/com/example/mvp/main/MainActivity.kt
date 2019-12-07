package com.example.mvp.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.mvp.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.activity_main.*


@Route(path = AppRoute.ONE_APP_MAIN)
class MainActivity : MvpActivity<MainPresenter>() {

    override fun getLayoutAny() = R.layout.activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo1Main()
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val mainComponent =
            DaggerMainComponent.builder().activityComponent(activityComponent).build()
        mainComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}
