package com.example.base_fun.config

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.base_lib.injection.core.AppLifeCycles
import com.example.base_lib.injection.core.ConfigModule

class GlobalConfiguration : ConfigModule {
    init {
        //
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//            layout.setPrimaryColorsId(R.color.public_backgroundColor)//全局设置主题颜色
//            ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//        }
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
//            //指定为经典Footer，默认是 BallPulseFooter
//            ClassicsFooter(context).setDrawableSize(20f)
//        }
    }

    override fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>) {
        lifeCycles.add(ApplicationLifeCyclesImpl())
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
        lifeCycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
        lifeCycles.add(FragmentLifecycleCallbacksImpl())
    }

}