package com.example.baselib.base.delegate


import android.app.Activity
import android.os.Bundle
import com.example.baselib.di.component.AppComponent

/**
 * ================================================
 * 框架要求框架中的每个 [Activity] 都需要实现此类,以满足规范
 * ================================================
 */
interface IActivity {

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    fun setupActivityComponent(appComponent: AppComponent)

    /**
     * 是否使用 EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 初始化 View
     */
    fun initView(savedInstanceState: Bundle?): Int

    /**
     * 初始化数据
     */
    fun initData(savedInstanceState: Bundle?)
}
