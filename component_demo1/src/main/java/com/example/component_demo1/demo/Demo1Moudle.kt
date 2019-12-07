package com.example.component_demo1.demo

import android.app.Activity
import android.view.View
import com.example.base_lib.injection.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class Demo1Moudle(private val activity: Activity) {

    @ActivityScope
    @Provides
    fun provideActivity(): Activity {
        return this.activity
    }

    @Provides
    fun provideView(): View {
        return View(activity)
    }
}