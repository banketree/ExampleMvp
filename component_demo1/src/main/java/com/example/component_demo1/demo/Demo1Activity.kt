package com.example.component_demo1.demo

import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo1.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo1_activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


@Suppress("DEPRECATION")
@Route(path = AppRoute.TWO_DEMO1_MAIN)
class Demo1Activity : MvpActivity<Demo1Presenter>() {

    override fun getLayoutAny() = R.layout.demo1_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            AppRoute.gotoTwoDemo2Main()
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo1Component.builder().demo1Moudle(Demo1Moudle(this)).build()//.activityComponent(activityComponent).build()
        demoComponent?.inject(this)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: Message) {
        Timber.i("" + message)
    }
}
