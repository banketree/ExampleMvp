package com.example.baselib.injection.component

import android.app.Activity
import android.content.Context
import com.example.baselib.injection.module.ActivityMoudle
import com.example.baselib.injection.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class],
        modules = [ActivityMoudle::class])
interface ActivityComponent {

//    fun context(): Context
//
//    fun activity(): Activity
}