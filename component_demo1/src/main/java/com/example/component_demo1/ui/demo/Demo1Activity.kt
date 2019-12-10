package com.example.component_demo1.ui.demo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.MvpApplication
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo1.R
import com.example.component_demo1.http.WeatherApi
import com.example.component_demo1.ui.home.HomeActivity
import com.example.route.AppRoute
import com.tbruyelle.rxpermissions2.RxPermissions
import com.thinkcore.activity.TActivityUtils
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
            (application as MvpApplication).provideCache().put("test", "444444444444444444444")
            provideCache().put("test", "555555555555555555555")
            var test = provideCache().get("test")
            Timber.i("" + test)

            (application as MvpApplication).provideExecutorService().execute(Runnable {
                test = (application as MvpApplication).provideCache().get("test")
                Timber.i("" + test)
            })

            RxPermissions(this).request(
//                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE//,
//                Manifest.permission.ACCESS_FINE_LOCATION
            )
                .subscribe { granted ->
                    Timber.i("申请结果:$granted")
                    AppRoute.gotoTwoDemo2Main()
                }
        }

        home_tv.setOnClickListener {
            TActivityUtils.jumpToActivityForResult(
                this@Demo1Activity,
                HomeActivity::class.java,
                object : TActivityUtils.IActivityResult {
                    override fun onActivityResult(resultCode: Int, intent: Intent?) {
                        if (resultCode != Activity.RESULT_OK) return
                    }
                })
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo1Component.builder().demo1Moudle(Demo1Moudle(this))
                .build()//.activityComponent(activityComponent).build()
        demoComponent?.inject(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: Message) {
        Timber.i("" + message)
    }
}
