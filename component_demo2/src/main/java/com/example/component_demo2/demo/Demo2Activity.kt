package com.example.component_demo2.demo

import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_fun.ui.MvpActivity
import com.example.component_demo2.R
import com.example.component_demo2.http.WeatherApi
import com.example.route.AppRoute
import kotlinx.android.synthetic.main.demo2_activity_main.*
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


@Route(path = AppRoute.TWO_DEMO2_MAIN)
class Demo2Activity : MvpActivity<Demo2Presenter>() {

    override fun getLayoutAny() = R.layout.demo2_activity_main

    override fun initView() {
        test_tv.setOnClickListener {
            provideCache().put("test", "22222")
            val test = provideCache().get("test")
            Timber.i("" + test)

            AppRoute.gotoTwoDemo3Main()

            // 全局 BaseUrl 的优先级低于 Domain-Name header 中单独配置的,其他未配置的接口将受全局 BaseUrl 的影响
            RetrofitUrlManager.getInstance().setGlobalDomain("https://www.youku.com")
            WeatherApi().getWeather(null)
        }
    }

    override fun initData() {
    }

    override fun injectComponent() {
        val demoComponent =
            DaggerDemo2Component.builder().activityComponent(activityComponent).build()
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
