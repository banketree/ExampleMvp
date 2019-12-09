package com.example.component_demo3.demo

import android.os.Message
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.base_lib.utils.EventBusManager
import com.example.component_demo3.R
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo3_activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

@Route(path = AppRoute.TWO_DEMO3_MAIN)
class Demo3Activity : MvpActivity<Demo3Presenter>() {

    override fun getLayoutAny() = R.layout.demo3_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            //            presenter?.showLoading()
            AppRoute.gotoOneAppMain()

            EventBusManager.instance?.post(Message())
            EventBusManager.instance?.postSticky(Message())
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: Message) {
        Timber.i("" + message)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetStickyEvent(message: Message) {
        Timber.i("" + message)
    }
}
