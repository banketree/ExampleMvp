package com.example.base_fun.config

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber

internal class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("${activity.javaClass.simpleName} onActivityCreated")
//        ImmersionBar.with(activity)
//            .statusBarDarkFont(true)
//            .init()
//        val toolbar: Toolbar? = activity.findViewById(R.id.toolbar)
//        toolbar?.run {
//            ImmersionBar.setTitleBar(activity, this)
//            if (activity is AppCompatActivity) {
//                activity.setSupportActionBar(activity.findViewById(R.id.toolbar))
//                activity.supportActionBar?.setDisplayShowTitleEnabled(false)
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    activity.setActionBar(activity.findViewById(R.id.toolbar))
//                    activity.actionBar?.setDisplayShowTitleEnabled(false)
//                } else {
//
//                }
//            }
//        }
//        //设置title
//        activity.findViewById<TextView>(R.id.toolbar_title)?.text = activity.title
//        //设置左边点击事件
//        activity.findViewById<RelativeLayout>(R.id.public_rl_back)?.setOnClickListener {
//            activity.onBackPressed()
//        }
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
//        ImmersionBar.with(activity).destroy()
    }
}