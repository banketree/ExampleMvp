package com.example.mvp.main

import com.example.baselib.ui.activity.MvpActivity
import com.example.mvp.R
import com.example.mvp.main.mvp.DaggerMainComponent
import com.example.mvp.main.mvp.MainContract
import com.example.mvp.main.mvp.MainPresenter

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    override fun getLayoutAny(): Any {
        return R.layout.activity_main
    }

    override fun initView() {

    }

    override fun initData() {
        presenter?.getList(true)
    }

    override fun injectComponent() {
        val mainComponent = DaggerMainComponent.builder().activityComponent(activityComponent).build()
        mainComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showList(data: Any) {

    }
}
