package com.example.performance_testing.config

import android.app.Activity
import android.app.Application
import android.os.Bundle
import leakcanary.AppWatcher
import timber.log.Timber

internal class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("${activity.javaClass.simpleName} onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} onActivityDestroyed 开始性能检测")
        AppWatcher.objectWatcher.watch(activity)
    }
}