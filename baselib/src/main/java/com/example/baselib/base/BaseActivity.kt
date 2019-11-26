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
}
