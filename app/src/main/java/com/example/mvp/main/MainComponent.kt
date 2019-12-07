package com.example.mvp.main

import com.example.base_fun.injection.component.ActivityComponent
import com.example.base_fun.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(dependencies = [ActivityComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}