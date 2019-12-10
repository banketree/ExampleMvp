package com.example.component_demo1.ui.home

import com.example.base_fun.ui.MvpActivity
import com.example.component_demo1.R
import kotlinx.android.synthetic.main.demo1_activity_home.*

class HomeActivity : MvpActivity<HomePresenter>() {

    override fun injectComponent() {
        var homeComponent = DaggerHomeComponent.builder().build()
        homeComponent?.inject(this)
    }

    override fun getLayoutAny(): Any? = R.layout.demo1_activity_home

    override fun initView() {
        presenter?.init(this)
        presenter?.getWeather()
    }

    override fun initData() {
    }

    fun initData(value: String) {
        data_tv.text = value
    }
}