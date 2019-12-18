package com.example.mvp.main

import android.Manifest
import android.os.Message
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
import me.jessyan.autosize.internal.CustomAdapt
import javax.xml.datatype.DatatypeConstants.SECONDS
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


@Route(path = AppRoute.ONE_APP_MAIN)
class MainActivity : MvpActivity<MainPresenter>(), CustomAdapt {
    private lateinit var testViewModel: TestViewModel

    @Inject
    lateinit var kotlinPresenter: KotlinPresenter

    override val isBaseOnWidth: Boolean  //是否按照宽度进行等比例适配
        get() = false
    override val sizeInDp: Float  //设计图上的设计尺寸, 单位 dp
        get() = 667f

    override fun getLayoutAny() = com.example.mvp.R.layout.activity_main

    override fun initView() {
        lifecycle.addObserver(MyLifeObserver())
        presenter?.let {
            lifecycle.addObserver(presenter)
        }

        presenter?.addDisposable(RxView.clicks(test_tv)
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                Timber.i("clicks:点击了按钮：两秒内防抖")
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
            })

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

        //定时触发
        presenter?.addDisposable(Observable.timer(3, TimeUnit.SECONDS)
            .subscribe { })
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
