package com.example.baselib.base.delegate.impl


import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.ComponentCallbacks2
import android.content.ContentProvider
import android.content.Context
import android.content.res.Configuration
import com.example.baselib.base.App
import com.example.baselib.base.delegate.AppLifecycles
import com.example.baselib.di.component.AppComponent
import com.example.baselib.di.component.DaggerAppComponent
import com.example.baselib.di.module.ConfigModule
import com.example.baselib.di.module.GlobalConfigModule
import com.example.baselib.utils.ManifestParser

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Named

/**
 * ================================================
 * AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口),框架就能照常运行
 * ================================================
 */
class AppDelegate(context: Context) : App, AppLifecycles {
    private var application: Application? = null
    private var appComponent: AppComponent? = null

    @Inject
    @Named("ActivityLifecycle")
    protected var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null
    @Inject
    @Named("ActivityLifecycleForRxLifecycle")
    protected var activityLifecycleForRxLifecycle: Application.ActivityLifecycleCallbacks? = null

    private var configModuleList: List<ConfigModule>? = null //各个模块的配置

    private var mAppLifecycles: List<AppLifecycles>? = ArrayList() //各个模块的应用生命周期
    private var mActivityLifecycles: List<Application.ActivityLifecycleCallbacks>? = ArrayList()
    private var mComponentCallback: ComponentCallbacks2? = null

    init {
        //用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        this.configModuleList = ManifestParser(context).parse()

        //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        for (module in configModuleList!!) {
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mAppLifecycles!!)
            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLifecycles!!)
        }
    }

    override fun attachBaseContext(base: Context) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        for (lifecycle in mAppLifecycles!!) {
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        this.application = application
        appComponent = DaggerAppComponent
            .builder()
            .application(application)//提供application
            //                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))//全局配置
            .build()
        appComponent!!.inject(this)

        this.configModuleList = null

        //注册框架内部已实现的 Activity 生命周期逻辑
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

        //注册框架内部已实现的 RxLifecycle 逻辑
        application.registerActivityLifecycleCallbacks(activityLifecycleForRxLifecycle)

        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        //每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
        //也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        for (lifecycle in mActivityLifecycles!!) {
            application.registerActivityLifecycleCallbacks(lifecycle)
        }

        mComponentCallback = AppComponentCallbacks(application, appComponent!!)

        //注册回掉: 内存紧张时释放部分内存
        application.registerComponentCallbacks(mComponentCallback)

        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (lifecycle in mAppLifecycles!!) {
            lifecycle.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        if (activityLifecycleCallbacks != null) {
            application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        }
        if (activityLifecycleForRxLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(activityLifecycleForRxLifecycle)
        }
        if (mComponentCallback != null) {
            application.unregisterComponentCallbacks(mComponentCallback)
        }
        if (mActivityLifecycles != null && mActivityLifecycles!!.size > 0) {
            for (lifecycle in mActivityLifecycles!!) {
                application.unregisterActivityLifecycleCallbacks(lifecycle)
            }
        }
        if (mAppLifecycles != null && mAppLifecycles!!.size > 0) {
            for (lifecycle in mAppLifecycles!!) {
                lifecycle.onTerminate(application)
            }
        }
        this.appComponent = null
        this.activityLifecycleCallbacks = null
        this.activityLifecycleForRxLifecycle = null
        this.mActivityLifecycles = null
        this.mComponentCallback = null
        this.mAppLifecycles = null
        this.application = null
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     *
     * @return GlobalConfigModule
     */
    private fun getGlobalConfigModule(context: Context, modules: List<ConfigModule>): GlobalConfigModule? {
        //        GlobalConfigModule.Builder builder = GlobalConfigModule
        //                .builder();
        //
        //        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        //        for (ConfigModule module : modules) {
        //            module.applyOptions(context, builder);
        //        }
        //
        //        return builder.build();
        return null
    }

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, [AppComponent] 接口中声明的方法返回的实例, 在 [.getAppComponent] 拿到对象后都可以直接使用
     *
     * @return AppComponent
     * @see .
     */
    override fun getAppComponent(): AppComponent {
        //        Preconditions.checkNotNull(appComponent,
        //                "%s == null, first call %s#onCreate(Application) in %s#onCreate()",
        //                AppComponent.class.getName(), getClass().getName(), application == null
        //                        ? Application.class.getName() : application.getClass().getName());
        return appComponent!!
    }

    /**
     * [ComponentCallbacks2] 是一个细粒度的内存回收管理回调
     * [Application]、[Activity]、[Service]、[ContentProvider]、[] 实现了 [ComponentCallbacks2] 接口
     * 开发者应该实现 [ComponentCallbacks2.onTrimMemory] 方法, 细粒度 release 内存, 参数的值不同可以体现出不同程度的内存可用情况
     * 响应 [ComponentCallbacks2.onTrimMemory] 回调, 开发者的 App 会存活的更持久, 有利于用户体验
     * 不响应 [ComponentCallbacks2.onTrimMemory] 回调, 系统 kill 掉进程的几率更大
     */
    private class AppComponentCallbacks internal constructor(
        private val application: Application,
        private val appComponent: AppComponent
    ) : ComponentCallbacks2 {

        /**
         * 在你的 App 生命周期的任何阶段, onTrimMemory 发生的回调都预示着你设备的内存资源已经开始紧张
         * 你应该根据 条件 发生回调时的内存级别来进一步决定释放哪些资源
         */
        override fun onTrimMemory(level: Int) {
            //状态1. 当开发者的 App 正在运行
            //设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发杀死 LRU 列表中的进程的机制
            //                case TRIM_MEMORY_RUNNING_MODERATE:


            //设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用来提升系统性能 (但是这也会直接影响到你的 App 的性能)
            //                case TRIM_MEMORY_RUNNING_LOW:


            //设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该立即释放所有非必须的资源
            //如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
            //                case TRIM_MEMORY_RUNNING_CRITICAL:


            //状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
            //                case TRIM_MEMORY_UI_HIDDEN:


            //状态3. 当前的 App 进程被置于 Background LRU 列表中
            //进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉 LRU 列表中的其他进程了
            //你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速恢复
            //                case TRIM_MEMORY_BACKGROUND:


            //系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张, 你的进程是有可能被杀死的
            //                case TRIM_MEMORY_MODERATE:


            //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
            //低于 API 14 的 App 可以使用 onLowMemory 回调
            //                case TRIM_MEMORY_COMPLETE:
        }

        override fun onConfigurationChanged(newConfig: Configuration) {}

        /**
         * 当系统开始清除 LRU 列表中的进程时, 尽管它会首先按照 LRU 的顺序来清除, 但是它同样会考虑进程的内存使用量, 因此消耗越少的进程则越容易被留下来
         *
         * @see .TRIM_MEMORY_COMPLETE
         */
        override fun onLowMemory() {
            //系统正运行于低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
        }
    }
}

