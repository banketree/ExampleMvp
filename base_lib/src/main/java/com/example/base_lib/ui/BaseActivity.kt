package com.example.base_lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.base_lib.utils.EventBusManager
import com.thinkcore.activity.TAppActivity

@Suppress("DEPRECATION")
abstract class BaseActivity : TAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutAny = getLayoutAny()
        layoutAny?.let {
            when (it) {
                is Int -> {
                    val rootView = LayoutInflater.from(this).inflate(it, null)
                    setContentView(rootView)
                }
                is View -> {
                    setContentView(it)
                }
                else -> throw Exception("layout error")
            }

            if (useEventBus()) {
                EventBusManager.instance?.register(this@BaseActivity)
            }

            initPlug()
            initView()
            initData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (useEventBus()) {
            EventBusManager.instance?.unregister(this@BaseActivity)
        }
    }

    /** 设置布局id 或 View*/
    abstract fun getLayoutAny(): Any?

    /**初始化 插件*/
    abstract fun initPlug()

    /**初始化视图*/
    abstract fun initView()

    /** 初始化数据*/
    abstract fun initData()

    /**
     * 是否使用 EventBus
     * 默认不使用
     */
    fun useEventBus(): Boolean = true
}
