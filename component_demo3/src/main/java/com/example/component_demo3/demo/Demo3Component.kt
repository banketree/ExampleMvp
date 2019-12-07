package com.example.component_demo3.demo

import com.example.base_fun.injection.component.ActivityComponent
import com.example.base_fun.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(dependencies = [ActivityComponent::class])
interface Demo3Component {
    fun inject(activity: Demo3Activity)
}