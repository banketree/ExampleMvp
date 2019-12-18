package com.example.mvp.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.base_fun.mvp.BasePresenter
import com.example.base_fun.mvp.IView
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor() : LifecycleObserver, BasePresenter(), IView {
    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun init() {
        super.init()
    }

    override fun release() {
        super.release()
    }

    // OnLifecycleEvent()内的注解Lifecycle.Event.XXX 对应不同的生命周期方法，你可以根据需要监听不同的生命周期方法。
    // 方法名可以随意，这里为了方便理解定义为onResumeListener()。
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeListener() {
        Timber.d("onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseListener() {
        Timber.d("onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestoryListener() {
        Timber.d("onDestory: ")
    }
}