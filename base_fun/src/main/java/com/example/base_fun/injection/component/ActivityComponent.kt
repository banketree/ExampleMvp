package com.example.base_fun.injection.component

import android.app.Activity
import android.content.Context
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_fun.injection.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityMoudle::class]
)
interface ActivityComponent {

    fun context(): Context

    fun activity(): Activity
}