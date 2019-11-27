package com.example.mvp.main

import com.example.baselib.ui.activity.MvpActivity
import com.example.mvp.R
import com.example.mvp.main.mvp.DaggerMainComponent
import com.example.mvp.main.mvp.MainContract
import com.example.mvp.main.mvp.MainModule
import com.example.mvp.main.mvp.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    override fun getLayoutAny(): Any {
        return R.layout.activity_main
    }

    override fun initView() {
        test_word_tv.setOnClickListener {
            presenter?.testWord()
        }
    }

    override fun initData() {

    }

    override fun injectComponent() {
        val mainComponent = DaggerMainComponent.builder().activityComponent(activityComponent).mainModule(MainModule(this)).build()
        mainComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showList(data: Any) {

    }
}
