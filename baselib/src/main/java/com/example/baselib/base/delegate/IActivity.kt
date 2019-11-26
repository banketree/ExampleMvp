package com.example.baselib.base.delegate

import android.app.Activity
import android.os.Bundle
import androidx.annotation.NonNull
import com.example.baselib.di.component.AppComponent
import com.example.baselib.integration.cache.Cache
import com.example.baselib.integration.ActivityLifecycle
import com.example.baselib.base.BaseFragment


/**
 * ================================================
 * 框架要求框架中的每个 [Activity] 都需要实现此类,以满足规范
 * ================================================
 */
interface IActivity {

    /**
     * 提供在 [Activity] 生命周期内的缓存容器, 可向此 [Activity] 存取一些必要的数据
     * 此缓存容器和 [Activity] 的生命周期绑定, 如果 [Activity] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [@LruCache]
     */
    @NonNull
    fun provideCache(): Cache<String, Any>

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

    /**
     * 这个 Activity 是否会使用 Fragment,框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回`false`,那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 [BaseFragment] 的 Fragment 将不起任何作用
     * @see ActivityLifecycle.registerFragmentCallbacks
     * @return
     */
    fun useFragment(): Boolean
}