package com.example.mvp.main

import android.Manifest
import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.route.AppRoute
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


@Route(path = AppRoute.ONE_APP_MAIN)
class MainActivity : MvpActivity<MainPresenter>() {

    override fun getLayoutAny() = com.example.mvp.R.layout.activity_main

    override fun initView() {
        test_tv.setOnClickListener {

            RxPermissions(this).request(
//                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE//,
//                Manifest.permission.ACCESS_FINE_LOCATION
            )
                .subscribe { granted ->
                    Timber.i("申请结果:$granted")
                    AppRoute.gotoTwoDemo1Main()
                }
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val mainComponent =
            DaggerMainComponent.builder().activityComponent(activityComponent).build()
        mainComponent?.inject(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: Message) {
        Timber.i("" + message)
    }
}
