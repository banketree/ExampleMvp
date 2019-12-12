package com.example.performance_testing.config

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.base_lib.injection.core.AppLifeCycles
import com.example.base_lib.injection.core.ConfigModule

/***
 * 该类是整个module可以对整个App的Application/Activity/Fragment的生命周期进行逻辑注入
 * 例如我们平时的第三方代码就可以在这里去进行实现
 **/

class Configuration : ConfigModule {
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
