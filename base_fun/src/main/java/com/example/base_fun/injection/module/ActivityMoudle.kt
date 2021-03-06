package com.example.base_fun.injection.module

import android.app.Activity
import com.example.base_lib.injection.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityMoudle(private val activity: Activity) {

    @ActivityScope
    @Provides
    fun provideActivity(): Activity {
        return this.activity
    }
}