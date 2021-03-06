package com.example.performance_testing.config

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import leakcanary.AppWatcher
import timber.log.Timber


internal class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        Timber.i("${f.javaClass.simpleName} onFragmentDestroyed 开始性能检测")
        AppWatcher.objectWatcher.watch(f)
    }
}