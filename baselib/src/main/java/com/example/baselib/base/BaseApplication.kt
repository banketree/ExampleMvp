package com.example.baselib.base


import android.app.Application
import android.content.Context
import com.example.baselib.base.delegate.AppLifecycles
import com.example.baselib.base.delegate.impl.AppDelegate
import com.example.baselib.di.component.AppComponent
import dagger.internal.Preconditions

/**
 * ================================================
 * 应用基类
 * ================================================
 */
class BaseApplication : Application(), App {
    private var mAppDelegate: AppLifecycles? = null

    /**
     * 这里会在 [BaseApplication.onCreate] 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null)
            this.mAppDelegate = AppDelegate(base)
        this.mAppDelegate!!.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (mAppDelegate != null)
            this.mAppDelegate!!.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        if (mAppDelegate != null)
            this.mAppDelegate!!.onTerminate(this)
    }

    /**
     * 将 [AppComponent] 返回出去, 供其它地方使用, {
     * @link AppComponent} 接口中声明的方法所返回的实例,
     * 在 [.getAppComponent] 拿到对象后都可以直接使用
     * @return AppComponent
     */
    override fun getAppComponent(): AppComponent {
        Preconditions.checkNotNull(mAppDelegate!!, "%s cannot be null", AppDelegate::class.java.name)
        //        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", mAppDelegate.getClass().getName(), App.class.getName());
        return (mAppDelegate as App).getAppComponent()
    }
}
