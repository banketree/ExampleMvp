package com.example.baselib.base.delegate.impl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.baselib.base.delegate.FragmentDelegate
import com.example.baselib.base.delegate.IFragment
import com.example.baselib.utils.EventBusManager

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
            EventBusManager . getInstance ().register(fragment)//注册到事件主线
        }
        //        iFragment.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(mFragment.getActivity()));
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
        //        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
        //            try {
        //                mUnbinder.unbind();
        //            } catch (IllegalStateException e) {
        //                e.printStackTrace();
        //                //fix Bindings already cleared
        //                Timber.w("onDestroyView: " + e.getMessage());
        //            }
        //        }
    }

    override fun onDestroy() {
        if (iFragment != null && iFragment.useEventBus()) {//如果要使用eventbus请将此方法返回true
            EventBusManager.getInstance().unregister(fragment)//注册到事件主线
        }
        //        this.mUnbinder = null;
    }

    override fun onDetach() {

    }

    override val isAdded: Boolean
        get() = fragment != null && fragment.isAdded
}
