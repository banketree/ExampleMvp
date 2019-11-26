package com.example.baselib.base.delegate.impl

import android.app.Activity
import android.os.Bundle
import com.example.baselib.base.delegate.ActivityDelegate
import com.example.baselib.base.delegate.IActivity
import com.example.baselib.integration.EventBusManager
import com.example.baselib.utils.MvpUtils

/**
 * ================================================
 * {默认实现类
 * ================================================
 */
class ActivityDelegateImpl(private var activity: Activity?) : ActivityDelegate {
    private var iActivity: IActivity? = null

    init {
        this.iActivity = activity as IActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        iActivity?.let {
            if (it.useEventBus()) {//如果要使用 EventBus 请将此方法返回 true
                if (activity != null) {
                    EventBusManager.instance?.register(activity!!)//注册到事件主线
                }
            }

            if (activity != null) {//这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
                it.setupActivityComponent(MvpUtils.obtainAppComponentFromContext(activity!!))
            }
        }
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        iActivity?.let {
            if (it.useEventBus() && activity != null) {
                EventBusManager.instance?.unregister(activity!!)
            }
        }

        this.iActivity = null
        this.activity = null
    }
}