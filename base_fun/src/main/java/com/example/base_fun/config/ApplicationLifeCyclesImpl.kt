package com.example.base_fun.config

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.example.base_fun.BuildConfig
import com.example.base_lib.injection.core.AppLifeCycles
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig

import timber.log.Timber


internal class ApplicationLifeCyclesImpl : AppLifeCycles {
    override fun attachBaseContext(base: Context) {
        Timber.i("Application attachBaseContext")
    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {//debug模式下初始化
            Timber.plant(Timber.DebugTree())
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }else {
//            Timber.plant(FileLoggingTree(application))
        }
        ARouter.init(application) // 尽可能早,推荐在Application中初始化

//        initAdapt(application)
    }

    override fun onTerminate(application: Application) {
        Timber.i("${application.javaClass.simpleName} onCreate")
    }

    private fun initAdapt(application: Application) {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(application)
        //以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
        AutoSizeConfig.getInstance().isCustomFragment =
            true//是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
        AutoSizeConfig.getInstance().isExcludeFontScale =
            true //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变 //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false

        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
        //.setUseDeviceSize(true)
        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
        AutoSizeConfig.getInstance().isBaseOnWidth = true
        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
        //.setAutoAdaptStrategy(new AutoAdaptStrategy())


        //是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
        //AutoSizeConfig.instance!!.externalAdaptManager
        //加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity, 详情请看方法注释
        //如果不想放弃三方库页面的适配, 请用 addExternalAdaptInfoOfActivity 方法, 建议对三方库页面进行适配, 让自己的 App 更完美一点
        //.addCancelAdaptOfActivity(DefaultErrorActivity.class) //忽略 需要适配的UI
        //.addExternalAdaptInfoOfActivity(DefaultErrorActivity::class.java, ExternalAdaptInfo(true, 400f)) //加入适配的UI
    }
}