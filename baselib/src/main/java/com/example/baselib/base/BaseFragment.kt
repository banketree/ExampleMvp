package com.example.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.baselib.base.delegate.IFragment
import com.example.baselib.integration.cache.Cache
import com.example.baselib.integration.cache.CacheType
import com.example.baselib.integration.lifecycle.FragmentLifecycleable
import com.example.baselib.mvp.IPresenter
import com.example.baselib.utils.MvpUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

import javax.inject.Inject

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @[@Fragment] 的三方库, 那你就需要自己自定义 @[Fragment]
 * 继承于这个特定的 @[@Fragment], 然后再按照 [BaseFragment] 的格式, 将代码复制过去, 记住一定要实现[IFragment]
 * ================================================
 */
abstract class BaseFragment<P : IPresenter> : Fragment(), IFragment, FragmentLifecycleable {
    protected val TAG = this.javaClass.simpleName
    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var cache: Cache<String, Any>? = null

    @Inject
    protected var presenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (cache == null) {
            cache = MvpUtils.obtainAppComponentFromContext(activity!!).cacheFactory().build(CacheType.FRAGMENT_CACHE) as Cache<String, Any>
        }
        return cache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return lifecycleSubject
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) presenter!!.onDestroy()//释放资源
        this.presenter = null
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 `true` (默认为 `true`), Arms 会自动注册 EventBus
     */
    override fun useEventBus(): Boolean {
        return true
    }
}
