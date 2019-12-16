package com.example.mvp.jetpack.lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Lifecycle
import timber.log.Timber


class MyLifeObserver : LifecycleObserver {
    private val TAG = "MyLifeObserver"

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