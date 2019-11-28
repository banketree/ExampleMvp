package com.example.mvp.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.baselib.ui.activity.MvpActivity
import com.example.mvp.R
import com.example.mvp.main.mvp.DaggerHomeComponent
import com.example.mvp.main.mvp.HomeContract
import com.example.mvp.main.mvp.HomeModule
import com.example.mvp.main.mvp.HomePresenter
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/test1/home")
class HomeActivity : MvpActivity<HomePresenter>(), HomeContract.View {

    override fun getLayoutAny(): Any {
        return R.layout.activity_main
    }

    override fun initView() {
        test_word_tv.text = "test1/home"
        test_word_tv.setOnClickListener {
            ARouter.getInstance().build("/test2/main2")
                .withLong("key1", 666L)
                .withString("key3", "888")
                .navigation()

//            finish()
        }
    }

    override fun initData() {

    }

    override fun injectComponent() {
        val mainComponent =
            DaggerHomeComponent.builder().activityComponent(activityComponent).homeModule(HomeModule(this)).build()
        mainComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showList(data: Any) {

    }
}
