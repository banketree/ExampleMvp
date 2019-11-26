package com.example.baselib.base

import android.os.Bundle
import android.view.InflateException
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.example.baselib.base.delegate.IActivity
import com.example.baselib.integration.cache.Cache
import com.example.baselib.integration.cache.CacheType
import com.example.baselib.integration.lifecycle.ActivityLifecycleable
import com.example.baselib.mvp.IPresenter
import com.example.baselib.utils.MvpUtils
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

import javax.inject.Inject
import android.app.Activity

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@Activity} 的三方库, 那你就需要自己自定义 {@@Activity}
 * 继承于这个特定的 {@Activity}, 然后再按照 {@BaseActivity} 的格式, 将代码复制过去, 记住一定要实现
 * ================================================
 */
abstract class BaseActivity<P : IPresenter> : AppCompatActivity(), IActivity, ActivityLifecycleable {
    protected val TAG = this.javaClass.simpleName
    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    private var cache: Cache<String, Any>? = null
    private var unbinder: Unbinder? = null

    @Inject
    protected var presenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (cache == null) {
            cache = MvpUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE) as Cache<String, Any>
        }
        return cache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return lifecycleSubject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val layoutResID = initView(savedInstanceState)
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID)
                unbinder = ButterKnife.bind(this)//绑定到butterknife
            }
        } catch (e: Exception) {
            if (e is InflateException) throw e
            e.printStackTrace()
        }

        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null && unbinder !== Unbinder.EMPTY)
            unbinder!!.unbind()
        this.unbinder = null
        if (presenter != null)
            presenter!!.onDestroy()//释放资源
        this.presenter = null
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

    /**
     * 这个 [Activity] 是否会使用 [Fragment], 框架会根据这个属性判断是否注册 [android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回 `false`, 那意味着这个 [Activity] 不需要绑定 [Fragment], 那你再在这个 [Activity] 中绑定继承于 [BaseFragment] 的 [Fragment] 将不起任何作用
     *
     * @return 返回 `true` (默认为 `true`), 则需要使用 [Fragment]
     */
    override fun useFragment(): Boolean {
        return true
    }
}
