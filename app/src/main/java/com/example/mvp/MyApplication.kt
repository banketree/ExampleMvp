package com.example.mvp

import android.app.Activity
import com.example.base_fun.MvpApplication
import me.jessyan.autosize.core.AutoSize
import me.jessyan.autosize.core.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import timber.log.Timber
import java.util.*

class MyApplication : MvpApplication() {
    override fun onCreate() {
        super.onCreate()
        initAutoSize()
    }

    fun initAutoSize() {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this)
        //以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
        AutoSizeConfig.instance!!
            .setCustomFragment(true)//是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //.setExcludeFontScale(true) //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变 //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
            .setOnAdaptListener(object : onAdaptListener {
                //屏幕适配监听器
                override fun onAdaptBefore(target: Any, activity: Activity) {
                    //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                    //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                    //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                    //AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                    //AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                    Timber.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.javaClass.name))
                }

                override fun onAdaptAfter(target: Any, activity: Activity) {
                    Timber.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.javaClass.name))
                }
            })//是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
        //.setLog(false)
        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
        //.setUseDeviceSize(true)
        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
        //.setBaseOnWidth(false)
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