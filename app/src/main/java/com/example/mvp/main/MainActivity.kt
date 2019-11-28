package com.example.mvp.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselib.ui.activity.MvpActivity
import com.example.mvp.R
import com.example.mvp.main.mvp.DaggerMainComponent
import com.example.mvp.main.mvp.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import com.alibaba.android.arouter.launcher.ARouter
import com.example.baselib.mvp.view.IView


@Route(path = "/test1/main")
class MainActivity : MvpActivity<MainPresenter>(), IView {

    override fun getLayoutAny(): Any {
        return R.layout.activity_main
    }

    override fun initView() {
        test_word_tv.setOnClickListener {
            presenter?.testWord(this)

            ARouter.getInstance().build("/test1/home")
                .withLong("key1", 666L)
                .withString("key3", "888")
                .navigation()
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
