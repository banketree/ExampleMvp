package com.example.base_fun.injection.component

import android.app.Activity
import android.app.Application
import com.example.base_fun.injection.module.ActivityMoudle
import com.example.base_lib.injection.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ActivityMoudle::class]
)
interface ActivityComponent {

    fun application(): Application

    fun activity(): Activity
}