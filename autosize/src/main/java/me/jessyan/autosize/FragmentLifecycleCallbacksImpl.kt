package me.jessyan.autosize

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * [FragmentLifecycleCallbacksImpl] 可用来代替在 BaseFragment 中加入适配代码的传统方式
 * [FragmentLifecycleCallbacksImpl] 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 [@Fragment]
 */
class FragmentLifecycleCallbacksImpl(
    /**
     * 屏幕适配逻辑策略类
     */
    private var mAutoAdaptStrategy: AutoAdaptStrategy?
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy!!.applyAdapt(f, f.activity!!)
        }
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy [AutoAdaptStrategy]
     */
    fun setAutoAdaptStrategy(autoAdaptStrategy: AutoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy
    }
}
