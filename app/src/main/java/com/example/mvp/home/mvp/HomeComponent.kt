package com.example.mvp.main.mvp

import com.example.baselib.injection.component.ActivityComponent
import com.example.baselib.injection.scope.PerComponentScope
import com.example.mvp.main.HomeActivity
import dagger.Component

@PerComponentScope
@Component(modules = [HomeModule::class], dependencies = [ActivityComponent::class])
interface HomeComponent {
    fun inject(activity: HomeActivity)
}