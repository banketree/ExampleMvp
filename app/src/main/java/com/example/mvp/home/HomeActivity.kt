package com.example.mvp.main

import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.baselib.ui.activity.MvpActivity
import com.example.mvp.R
import com.example.mvp.main.mvp.DaggerHomeComponent
import com.example.mvp.main.mvp.HomePresenter
import com.example.mvp.presenter.TestPresenter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@Route(path = "/test1/home")
class HomeActivity : MvpActivity<HomePresenter>() {
    @Inject
    lateinit var testPresenter: TestPresenter

    override fun getLayoutAny(): Any = R.layout.activity_main

    override fun initView() {
        test_word_tv.text = "test1/home"
        test_word_tv.setOnClickListener {
            ARouter.getInstance().build("/test2/main2")
                .withLong("key1", 666L)
                .withString("key3", "888")
                .navigation()

            presenter?.wordPresenter?.otherCheck { 1 }
            testPresenter?.printTest()
            Log.i("", "")
//            finish()
        }
    }

    override fun initData() {
        presenter?.init(this)
    }

    override fun injectComponent() {
        val mainComponent =
            DaggerHomeComponent.builder().activityComponent(activityComponent).build()
        mainComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}
