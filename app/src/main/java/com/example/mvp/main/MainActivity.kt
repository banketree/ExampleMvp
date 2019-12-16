package com.example.mvp.main

import android.Manifest
import android.os.Message
import androidx.annotation.Nullable
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.mvp.jetpack.lifecycle.MyLifeObserver
import com.example.route.AppRoute
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.mvp.jetpack.livedata.TestViewModel
import androidx.lifecycle.ViewModelProviders
import android.net.NetworkInfo
import com.example.mvp.jetpack.livedata.NetworkLiveData


@Route(path = AppRoute.ONE_APP_MAIN)
class MainActivity : MvpActivity<MainPresenter>() {
    private lateinit var testViewModel: TestViewModel

    @Inject
    lateinit var kotlinPresenter: KotlinPresenter

    override fun getLayoutAny() = com.example.mvp.R.layout.activity_main

    override fun initView() {
        lifecycle.addObserver(MyLifeObserver())
        presenter?.let {
            lifecycle.addObserver(presenter)
        }
        test_tv.setOnClickListener {
            testViewModel.nameEvent.value = "测试测试"
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

        test_kotlin_tv.setOnClickListener {
            kotlinPresenter?.testCoroutine()
        }

        testViewModel = ViewModelProviders.of(this, TestViewModel.Factory("大大")).get(TestViewModel::class.java)
        val nameEvent = testViewModel.nameEvent
        nameEvent.observe(this, Observer<String> { s ->
            Timber.i("onChanged: s = $s")
            test_tv.text = s
        })

        NetworkLiveData.getInstance(this).observe(this,
            Observer { it ->
                it?.let {
                    Timber.d("onChanged: networkInfo=$it")
                }
            })

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
