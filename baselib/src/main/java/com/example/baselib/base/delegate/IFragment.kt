package com.example.baselib.base.delegate


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.example.baselib.di.component.AppComponent
import com.example.baselib.integration.cache.Cache

/**
 * ================================================
 * 框架要求框架中的每个 Fragment 都需要实现此类,以满足规范
 * ================================================
 */
interface IFragment {

    /**
     * 提供在 [Fragment] 生命周期内的缓存容器, 可向此 [Fragment] 存取一些必要的数据
     * 此缓存容器和 [Fragment] 的生命周期绑定, 如果 [Fragment] 在屏幕旋转或者配置更改的情况下
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
    fun setupFragmentComponent(appComponent: AppComponent)

    /**
     * 是否使用 EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 初始化 View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)
}