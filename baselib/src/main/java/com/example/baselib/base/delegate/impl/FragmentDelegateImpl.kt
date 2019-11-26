package com.example.baselib.base.delegate.impl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.baselib.base.delegate.FragmentDelegate
import com.example.baselib.base.delegate.IFragment
import com.example.baselib.integration.EventBusManager
import com.example.baselib.utils.MvpUtils

/**
 * ================================================
 * [FragmentDelegate] 默认实现类
 * ================================================
 */
class FragmentDelegateImpl(private val fragmentManager: FragmentManager, private val fragment: Fragment) :
    FragmentDelegate {
    val iFragment: IFragment = fragment as IFragment

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iFragment.useEventBus()) {//如果要使用eventbus请将此方法返回true
            fragment?.let {
                EventBusManager.instance?.register(fragment)//注册到事件主线
            }
        }

        fragment?.let {
            iFragment.setupFragmentComponent(MvpUtils.obtainAppComponentFromContext(it.requireContext()))
        }
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {
        //绑定到butterknife
        //        if (view != null)
        //            mUnbinder = ButterKnife.bind(mFragment, view);
    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment.initData(savedInstanceState)
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

    override fun onDestroyView() {

    }

    override fun onDestroy() {
        iFragment?.let {
            if (it.useEventBus()) {//如果要使用eventbus请将此方法返回true
                EventBusManager.instance?.unregister(fragment)//注册到事件主线
            }
        }
    }

    override fun onDetach() {

    }

    override val isAdded: Boolean
        get() = fragment != null && fragment.isAdded
}
