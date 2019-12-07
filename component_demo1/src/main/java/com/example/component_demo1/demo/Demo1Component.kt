package com.example.component_demo1.demo

import com.example.base_fun.injection.component.ActivityComponent
import com.example.base_fun.injection.scope.PerComponentScope
import dagger.Component

@PerComponentScope
@Component(dependencies = [ActivityComponent::class])
interface Demo1Component {
    fun inject(activity: Demo1Activity)
}