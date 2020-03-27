package com.example.route

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

object AppRoute {
    const val ONE_APP_MAIN = "/one/main"
    const val TWO_DEMO1_MAIN = "/two/main1"
    const val TWO_DEMO2_MAIN = "/three/main2"
    const val TWO_DEMO3_MAIN = "/four/main3"

    fun initRoute(application: Application) {
        if (BuildConfig.DEBUG) {//debug模式下初始化
//            Timber.plant(Timber.DebugTree())
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application) // 尽可能早,推荐在Application中初始化
    }

    fun gotoOneAppMain() {
        ARouter.getInstance().build(ONE_APP_MAIN).navigation()
    }

    fun gotoTwoDemo1Main() {
        ARouter.getInstance().build(TWO_DEMO1_MAIN).navigation()
    }

    fun gotoTwoDemo2Main() {
        ARouter.getInstance().build(TWO_DEMO2_MAIN).navigation()
    }

    fun gotoTwoDemo3Main() {
        ARouter.getInstance().build(TWO_DEMO3_MAIN).navigation()
    }
}